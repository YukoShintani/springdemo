package mrs.domain.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * [ReservableRoom]の複合キーオブジェクト
 * */
@Embeddable
public class ReservableRoomId implements Serializable{

	// [ROOM_ID]に対応するフィールド
	@Column(name="ROOM_ID")
	private Integer roomId;

	// [RESERVED_DATE]に対応するフィールド
	@Column(name="RESERVED_DATE")
	private LocalDate reservedDate;

	// コンストラクタ(フィールドを指定して生成)
	public ReservableRoomId(Integer roomId, LocalDate reservedDate){
		this.roomId = roomId;
		this.reservedDate = reservedDate;
	}

	// コンストラクタ(フィールドを制定せずに生成)
	public ReservableRoomId(){

	}

	// 主キーとして一意性を保証するために、equals, hashCodeを実装する
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reservedDate == null) ? 0 :reservedDate.hashCode());
		return result;
	}

	public boolean equals(Object obj){
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		ReservableRoomId other = (ReservableRoomId) obj;
		if(reservedDate == null){
			if(other.reservedDate != null) return false;
		}else if(!reservedDate.equals(other.reservedDate))
			return false;
		if(roomId == null){
			if(other.roomId != null) return false;
		}else if(!roomId.equals(other.roomId))
			return false;
		return true;

	}


	// 以下、getter, setter
	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public LocalDate getReservedDate() {
		return reservedDate;
	}

	public void setReservedDate(LocalDate reservedDate) {
		this.reservedDate = reservedDate;
	}


}
