package kr.co.jhta.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import kr.co.jhta.dto.ChatDTO;
import kr.co.jhta.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
	
	// simple message Protocol에 대한 spring framework 지원 메서드를 갖는 메세지센더조작 인터페이스
	private final SimpMessageSendingOperations template;
	
	@Autowired
	ChatRepository chatRepository;
	
	// MessageMapping을 통해서 webSocket으로 들어오는 메세지를 발신처리
	
	// /pub/chat/message 요청하게되고 이걸 controller 가 받아서 처리한다.
	
	// 처리가 완료되면 /sub/chat/room/roomId로 메세지가 전송된다.
	
	@MessageMapping("/chat/enterUser")
	public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor) {
		
		// 채팅방 유저 +1
		chatRepository.plusUserCnt(chat.getRoomId());
		
		// 채팅방에 유저 추가 및 UserUUID 반환
		String userUUID = chatRepository.addUser(chat.getRoomId(), chat.getSender());
		
		// 반환 결과를 socket session에 userUUID로저장
		headerAccessor.getSessionAttributes().put("userUUID", userUUID);
		headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
		
		chat.setMessage(chat.getSender()+"님 입장");
		
		log.info("chat {}", chat);
		
		template.convertAndSend("/sub/chat/room/"+chat.getRoomId(), chat);
		
	}
	
	@EventListener
	// 유저 퇴장시에 EventListener을 통해서 유저 퇴장을 확인
	public void webSocketDisconnectListener(SessionDisconnectEvent event) {
		log.info("접속 종료 : {}", event);
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		// stomp 세션에 있던 uuid와 roomid를 확인해서 채팅방 유저리스트와 room 에서 해당 유저를 삭제해주자.
		String userUUID =  (String) headerAccessor.getSessionAttributes().get("userUUID");
		String roomId =  (String) headerAccessor.getSessionAttributes().get("roomId");
		
		log.info("헤더 접속자 : {}" , headerAccessor);
		
		// 채팅방 유저 -1
		// chatRepository.minusUserCnt(roomId);
		
		String username = chatRepository.getUserName(roomId,userUUID);
		
		chatRepository.deleteUser(roomId, userUUID);
		
		if(username != null) {
			log.info("{} 사용자 접속해제", username);
			ChatDTO chat = ChatDTO.builder()
								  .type(ChatDTO.MessageType.LEVAE)
								  .sender(username)
								  .message(username + "님 퇴장")
								  .build();
			template.convertAndSend("/sub/chat/room/"+roomId, chat);
			
		}
		
	}
	
	
	//메세지 전송
	@MessageMapping("/chat/sendMessage")
	public void sendMessage(@Payload ChatDTO chat) {
		log.info("chat {}" , chat);
		chat.setMessage(chat.getMessage());
		template.convertAndSend("/sub/chat/room/"+chat.getRoomId(), chat);
	}
	
	// 채팅에 참여한 유저 리스트 반환
	@GetMapping("/chat/userlist")
	@ResponseBody
	public ArrayList<String> userList(String roomId){
		return chatRepository.getUserList(roomId);
	}
	
	// 채팅에 참여한 유저 닉네임 중복 확인
	@GetMapping("/chat/duplicateName")
	@ResponseBody
	public String isDuplicateName(@RequestParam("roomId")String roomId, 
			@RequestParam("username")String username) {
		//유저 이름 확인
		String userName = chatRepository.isDuplicateName(roomId,username);
		log.info("중복확인 : {}", userName);
		return userName;
	}
	
	
	
}
