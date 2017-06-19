package mrs.domain.service.reservation;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.model.RoleName;
import mrs.domain.model.User;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.room.ReservableRoomReporitory;


/*
 * 「Reservation」サービスクラス
 *
 * */
@Service
@Transactional
public class ReservationService {
	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	ReservableRoomReporitory reservableRoomRepository;

	/*
	 * 「予約」を「予約可能会議室キー」を条件に「利用開始時間」の昇順に取得
	 * */
	public List<Reservation> findReservations(ReservableRoomId reservableRoomId){
		return reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(reservableRoomId);
	}

	/*
	 * 予約
	 * */
	public Reservation reserve(Reservation reservation){
		ReservableRoomId reservableRoomId = reservation.getReservableRoom().getReservableRoomId();

		ReservableRoom reservable = reservableRoomRepository.findOne(reservableRoomId);

		if(reservable == null){
			throw new UnavailableReservationException("入力の日付・部屋の組み合わせは予約できません。");
		}

		boolean overlap = reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc
				(reservableRoomId)
				.stream()
				.anyMatch(x -> x.overlap(reservation));
		if(overlap){
			throw new AlreadyReservedException("入力の時間帯はすでに予約済です。");
		}

		reservationRepository.save(reservation);
		return reservation;

	}

	/*
	 * キャンセル
	 * */
	public void cancel(Integer reservationId, User requestUser){
		// 予約IDから予約を取得
		Reservation reservation = reservationRepository.findOne(reservationId);
		// ユーザーが管理者権限を保持しておらず、かつ登録ユーザー本人でない場合、キャンセルは付加
		if(RoleName.ADMIN != requestUser.getRoleName() &&
				!Objects.equals(reservation.getUser().getUserId(), requestUser.getUserId())){
			throw new AccessDeniedException("要求されたキャンセルは許可できません。");
		}

		// (上記でなく、操作可能な場合)予約を削除する
		reservationRepository.delete(reservation);

	}

}
