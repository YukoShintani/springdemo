package mrs.domain.repository.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;

/*
 * Reservationのリポジトリインターフェース
 * */
public interface ReservationRepository extends JpaRepository<Reservation, Integer>{

	/*
	 *「予約」を「予約可能会議室複合キー」を条件に、「利用開始時間」順に取得する
	 * */
	List<Reservation> findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc
		(ReservableRoomId reservableRoomId);
}
