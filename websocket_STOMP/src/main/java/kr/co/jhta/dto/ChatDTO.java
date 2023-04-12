package kr.co.jhta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatDTO {
	
	// 고정된 값이 있을때 사용 (숫자,오타방지,관리편함) 
	// 메세지 타입 : ENTER : 입장, LEVAE : 퇴장, TALK : 대화 (채팅방에 구독하고 있는 모든 클라이언트에게 전달)
	public enum MessageType{
		ENTER, LEVAE, TALK;
	}
	
	private MessageType type; // 메세지 타입
	private String roomId;	// 방번호
	private String sender;	// 보낸사람
	private String message;	// 메세지
	private String time;	// 발송 시간
}
