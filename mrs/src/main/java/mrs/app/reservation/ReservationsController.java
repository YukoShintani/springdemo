package mrs.app.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.model.User;
import mrs.domain.service.reservation.AlreadyReservedException;
import mrs.domain.service.reservation.ReservationService;
import mrs.domain.service.reservation.UnavailableReservationException;
import mrs.domain.service.room.RoomService;
import mrs.domain.service.user.ReservationUserDetails;


/*
 * 「Reservationコントローラクラス」
 *
 *
 * */
@Controller
@RequestMapping("reservations/{date}/{roomId}")
public class ReservationsController {

	/*
	 * 使用するサービスクラスをInjection(主処理をサービスクラスに委譲する)
	 * */
	@Autowired
	RoomService roomService;
	@Autowired
	ReservationService reservationService;

	/*
	 * [@ModelAttribute]を付けたメソッドでは
	 * 　・受け取りたいFormクラスを初期化してReturnする
	 * 　・このアノテーションがついているメソッドは、自クラス内で[@RequestMapping]がついたメソッドの
	 * 　　実行前に呼び出される
	 * 　・戻り値はRequestScopeに設定され、Modelにクラス名の先頭小文字の属性名として追加される
	 * */
	@ModelAttribute
	ReservationForm setUpForm() {
		// Formクラスのインスタンスを生成
		ReservationForm form = new ReservationForm();
		// デフォルト値を設定
		form.setStartTime(LocalTime.of(9,0).toString());
		form.setEndTime(LocalTime.of(10,0).toString());
		// Formクラスを返却
		return form;
	}

	/*
	 *予約Formの内容(日付、会議室ID)をURLパラメータで受け取る
	 *必要な情報を取得、生成してModelにセット
	 *　・会議室IDから取得した会議室情報をセット
	 *　・日付と会議室IDから取得した予約情報をセット
	 *　・開始終了の時刻DropDownの候補値をセット
	 *URL文字列を返却
	 * */
	@RequestMapping(method = RequestMethod.GET)
	String reserveForm(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@PathVariable("date") LocalDate date,
	@PathVariable("roomId") Integer roomId, Model model){
		ReservableRoomId reservableRoomId = new ReservableRoomId(roomId, date);
		List<Reservation> reservations = reservationService.findReservations(reservableRoomId);

		List<LocalTime> timeList =
				Stream.iterate(LocalTime.of(0, 0), t -> t.plusMinutes(30))
				.limit(24*2)
				.collect(Collectors.toList());

		model.addAttribute("room", roomService.findMeetingRoom(roomId));
		model.addAttribute("reservations", reservations);
		model.addAttribute("timeList", timeList);
		//model.addAttribute("user", dummyUser()); user認証を実装したのでコメントアウト

		return "reservation/reserveForm";

	}

/*
	 * dummy user を生成して返却する
	 *
	private User dummyUser() {
		User user = new User();
		user.setUserId("taro-yamada");
		user.setFirstName("太郎");
		user.setLastName("山田");
		user.setRoleName(RoleName.USER);
		return user;
	}
*/
	/*
	 * 予約処理
	 * ・予約FormオブジェクトとBind結果、ログインユーザーの情報、URLパラメータを受け取る
	 *
	 *
	 * */
	@RequestMapping(method = RequestMethod.POST)
	String reserve(@Validated ReservationForm form, BindingResult bindingResult,
			@AuthenticationPrincipal ReservationUserDetails userDetails,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			@PathVariable("roomId") Integer roomId, Model model){

		// Formクラスで指定済みValidationを元にBindする
		//   エラーがあれば予約フォームに元の入力値を設定して返却する
		if(bindingResult.hasErrors()){
			return reserveForm(date, roomId, model);
		}

		// 予約可能会議室Entityを生成（主キーとして取得値を設定）
		ReservableRoom reservableRoom = new ReservableRoom(new ReservableRoomId(roomId, date));

		// 予約Entityを生成し、各フィールドをセット
		Reservation reservation = new Reservation();
		reservation.setStartTime(form.getStartTime());
		reservation.setEndTime(form.getEndTime());
		reservation.setReservableRoom(reservableRoom);
		reservation.setUser(userDetails.getUser());
		try{
			// 予約サービスクラス.予約処理を呼び出し
			reservationService.reserve(reservation);

		}catch(UnavailableReservationException | AlreadyReservedException e){
			// 無効な予約例外、又は予約済例外を捕捉したら、エラーメッセージをセット
			// 予約フォームに元の入力値を設定して返却する
			model.addAttribute("error", e.getMessage());
			return reserveForm(date, roomId, model);
		}

		// 予約画面にRedirect(RequestPostGetパターン：完了後遷移画面F5での2重submitを防ぐ)
		return "redirect:/reservations/{date}/{roomId}";
	}

	/*
	 * cancel処理
	 * ・認証ユーザー情報を受け取る
	 * ・予約IDを受け取る
	 * ・URLパラメータとして会議室ID、日付を受け取る
	 * */
	@RequestMapping(method=RequestMethod.POST, params = "cancel")
	String cancel(@AuthenticationPrincipal ReservationUserDetails userDetails,
			@RequestParam("reservationId") Integer reservationId,
			@PathVariable("roomId") Integer roomId,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
			Model model){

		// 認証ユーザー情報から、ユーザーEntityを取得
		User user = userDetails.getUser();

		try{
			// 予約サービスクラス.キャンセル処理を呼び出す
			reservationService.cancel(reservationId,user);

		}catch(AccessDeniedException e){

			// アクセス非認可例外を捕捉
			// 例外メッセージをModel属性としてセット
			// 元のRequestから受け取ったパラメータを設定して予約Formオブジェクトを返却する
			model.addAttribute("error", e.getMessage());
			return reserveForm(date, roomId, model);

		}

		// 予約画面にRedirect
		return "redirect:/reservations/{date}/{roomId}";
	}



}
