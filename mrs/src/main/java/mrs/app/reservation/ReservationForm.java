package mrs.app.reservation;

import java.io.Serializable;

import javax.validation.constraints.NotNull;


//import org.springframework.format.annotation.DateTimeFormat;

/*
 * 予約フォームクラス
 * */
public class ReservationForm implements Serializable{

	//@DateTimeFormat(pattern = "HH:mm")
	@NotNull(message = "必須です")
	private String startTime;

	//@DateTimeFormat(pattern = "HH:mm")
	@NotNull(message = "必須です")
	private String endTime;

	// 以下、getter, setter
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


}
