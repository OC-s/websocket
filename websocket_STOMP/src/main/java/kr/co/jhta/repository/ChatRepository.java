package kr.co.jhta.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import kr.co.jhta.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ChatRepository {
	
	private Map<String, ChatRoom> chatRoomMap;
	
	// bean을 초기화 한 이후에 한번만 호출하고 싶을때
	@PostConstruct
	private void init() {
		chatRoomMap = new LinkedHashMap<>();
	}
	
	// 전체 채팅방 조회
	public List<ChatRoom> findAllRoom(){
		List chatRooms = new ArrayList<>(chatRoomMap.values());
		Collections.reverse(chatRooms);
		return chatRooms;
	}
	
	// roomid로 채팅방 찾기
	public ChatRoom findRoomById(String roomId) {
		return chatRoomMap.get(roomId);
	}
	
	// roomName 으로 채팅방 만들기
	public ChatRoom createChatRoom(String roomName) {
		ChatRoom chatRoom = new ChatRoom().create(roomName);
		
		// map에 채팅룸 아이디와 만들어진 채팅룸을 맵에 담기
		chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
		
		return chatRoom;
	}
	
	// 채팅방 인원 +1
	public void plusUserCnt(String roomId) {
		ChatRoom room = chatRoomMap.get(roomId);
		room.setUserCount(room.getUserCount()+1);
	}
	
	// 채팅방 유저 리스트에 유저 추가
	public String addUser(String roomId, String userName) {
		ChatRoom room = chatRoomMap.get(roomId);
		String userUUID = UUID.randomUUID().toString();
		
		//아이디 중복 확인후 userList에 추가
		room.getUserlist().put(userUUID, userName);
		
		return userUUID;
	}

	public ArrayList<String> getUserList(String roomId) {
		ArrayList<String> list = new ArrayList<String>();
		ChatRoom room = chatRoomMap.get(roomId);
		
		// hashmap 을 for문을 돌린후 value 값만 뽑아내서 list에 저장한후에 리턴
		
		HashMap<String, String> hm = room.getUserlist();
		Set<String> set = hm.keySet();
		Iterator<String> it = set.iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			list.add(hm.get(key));
		}
		
		return list;
	}

	// 채팅방 유저 이름 중복확인
	public String isDuplicateName(String roomId, String username) {
		ChatRoom room = chatRoomMap.get(roomId);
		String tmp = username;
		
		// 만약 사용자이름이 중복되었다면 랜덤한 숫자 붙여주기
		while(room.getUserlist().containsValue(tmp)) {
			int rnd = (int)(Math.random()*100)+1;
			tmp = username+rnd;
		}
		return tmp;
	}
	
	// 채팅방에 특정 사용자를 유저 목록에서 삭제
	public void deleteUser(String roomId, String userUUID) {
	 	ChatRoom room = chatRoomMap.get(roomId);
	 	room.getUserlist().remove(userUUID);
	}
	
	
	//
	public String getUserName(String roomId, String userUUID) {
		ChatRoom room = chatRoomMap.get(roomId);
		return room.getUserlist().get(userUUID);
	}
	
	
}
