package mrs.domain.service.reservation;

/*
 * 無効な予約例外クラス
 * */
public class UnavailableReservationException extends RuntimeException{
	/*
	 * メッセージを引数にするコンストラクタ
	 * */
	public UnavailableReservationException(String message){
		super(message);
	}
}
