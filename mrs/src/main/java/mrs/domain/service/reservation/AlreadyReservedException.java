package mrs.domain.service.reservation;

/*
 * 既に予約済例外
 * */
public class AlreadyReservedException extends RuntimeException{

	/*
	 * メッセージを引数に持つコンストラクタ
	 * */
	public AlreadyReservedException(String message){
		super(message);
	}
}
