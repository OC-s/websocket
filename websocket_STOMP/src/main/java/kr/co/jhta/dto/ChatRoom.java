package kr.co.jhta.dto;

import java.util.HashMap;
import java.util.UUID;

import lombok.Data;

@Data
public class ChatRoom {
	private String roomId;		// 채팅방 아이디
	private String roomName;	// 채팅방 이름
	private int userCount; 		// 인원수
	
	private HashMap<String, String> userlist = new HashMap<String, String>();
	
	public ChatRoom create(String roomName) {
		
		ChatRoom chatRoom = new ChatRoom();
		
		// UUID (Universally Unique Identifier) : 범용 고유 식별자
		// 고유한 128 비트 값
		// 룸번호가 중복되는일을 방지하기 위해서 식별자
		chatRoom.roomId=UUID.randomUUID().toString();
		chatRoom.roomName=roomName;
		
		return chatRoom;
		
	}
	
}
