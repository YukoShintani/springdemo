package mrs.domain.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/*
 * 予約(RESERVATION)に対応するEntity
 *
 * */
@Entity
public class Reservation {

	// [RESERVATION_ID]カラムに対応するフィールド。PK。自動生成。
	@Id
	@GeneratedValue()
	@Column(name="RESERVATION_ID")
	private Integer reservationId;

	// [START_TIME]カラムに対応するフィールド
	@Column(name="START_TIME")
	private String startTime;
	//private LocalTime startTime;

	// [END_TIME]カラムに対応するフィールド
	private String endTime;
	//private LocalTime endTime;

	// 「ReservableRoom」Entityとの関連を定義
	// 関連するカラムはDBのカラムIDでnameに指定？？
	@ManyToOne
	@JoinColumns({@JoinColumn(name = "RESERVED_DATE"),
		@JoinColumn(name = "ROOM_ID")})
	private ReservableRoom reservableRoom;

	// 「User」Entityとの関連を定義
	// 関連するカラムはDBのカラムIDでnameに指定？？
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	// 以下、getter, sertter
	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public String getStartTime() {
		if(startTime != null)
			return startTime.substring(0, 2) + ":" + startTime.substring(2);
		else
			return "";
	}

	public void setStartTime(String startTime) {

		this.startTime = startTime.replaceAll(":", "");
	}

	public String getEndTime() {
		if(startTime != null)
			return endTime.substring(0, 2) + ":" + endTime.substring(2);
		else
			return "";
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime.replaceAll(":", "");
	}

	public ReservableRoom getReservableRoom() {
		return reservableRoom;
	}

	public void setReservableRoom(ReservableRoom reservableRoom) {
		this.reservableRoom = reservableRoom;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/*
	 * 予約の重複があるかどうか。
	 * */
	public  boolean overlap(Reservation target){

		if(!Objects.equals(reservableRoom.getReservableRoomId(),target.reservableRoom.getReservableRoomId())){
			return false;
		}

		if(startTime.equals(target.startTime) && endTime.equals(target.endTime)){
			return true;
		}

		int this_start = Integer.parseInt(startTime);
		int this_end = Integer.parseInt(endTime);
		int target_start = Integer.parseInt(target.startTime);
		int target_end = Integer.parseInt(target.endTime);
		if(this_start <= target_end && this_end >= target_start )
			return true;
		else
			return false;

	}

}
