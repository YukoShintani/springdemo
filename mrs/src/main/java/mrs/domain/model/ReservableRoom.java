package mrs.domain.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/*
 * 予約可能会議室テーブルに対応するEntity
 *
 * */
@Entity
@Table(name = "RESERVABLE_ROOM")
public class ReservableRoom implements Serializable {

	// 複合キーを示すフィールド
	@EmbeddedId
	private ReservableRoomId reservableRoomId;

	// このEntityとMeetingRoom Entityとの関連を定義。多対1。[ROOM_ID]列による。[roomId]フィールドが対応する
	@ManyToOne
	@JoinColumn(name = "ROOM_ID", insertable = false, updatable = false)
	@MapsId("roomId")
	private MeetingRoom meetingRoom;

	// コンストラクタ(メンバ(これはたまたま複合キーのみ)に設定値を引き渡すもの）
	public ReservableRoom(ReservableRoomId reservableRoomId){
		this.reservableRoomId = reservableRoomId;
	}

	// コンストラクタ
	public ReservableRoom(){
	}

	// 以下、getter, setter
	public ReservableRoomId getReservableRoomId() {
		return reservableRoomId;
	}

	public void setReservableRoomId(ReservableRoomId reservableRoomId) {
		this.reservableRoomId = reservableRoomId;
	}

	public MeetingRoom getMeetingRoom() {
		return meetingRoom;
	}

	public void setMeegtingRoom(MeetingRoom meetingRoom) {
		this.meetingRoom = meetingRoom;
	}


}
