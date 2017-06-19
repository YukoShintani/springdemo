package mrs.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 会議室テーブル(MEETING_ROOM)に対応するEntity
 *
 * */
@Entity
@Table(name="MEETING_ROOM")
public class MeetingRoom implements Serializable{
	// [ROOM_ID]カラムに対応するフィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ROOM_ID")
	private Integer roomId;

	// [ROOM_NAME]に対応するフィールド
	@Column(name="ROOM_NAME")
	private String roomName;

	// 以下getter/setter
	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

}
