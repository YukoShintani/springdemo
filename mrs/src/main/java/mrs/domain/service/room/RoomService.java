package mrs.domain.service.room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.domain.model.MeetingRoom;
import mrs.domain.model.ReservableRoom;
import mrs.domain.repository.room.MeetingRoomRepository;
import mrs.domain.repository.room.ReservableRoomReporitory;

/*
 * 「ReservableRoom」サービスクラス
 *
 *
 * */
@Service
@Transactional
public class RoomService {

	@Autowired
	ReservableRoomReporitory reservableRoomRepository;

	@Autowired
	MeetingRoomRepository meetingRoomRepository;

	/*
	 * 指定日付に予約可能な会議室を取得する
	 * */
	public List<ReservableRoom> findReservableRooms(LocalDate date) {
		return reservableRoomRepository.findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(date);
	}


	/*
	 * 会議室IDから会議室を取得する
	 * */
	public MeetingRoom findMeetingRoom(Integer roomId){
		return meetingRoomRepository.findOne(roomId);
	}



}
