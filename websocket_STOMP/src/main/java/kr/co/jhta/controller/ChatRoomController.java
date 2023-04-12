package kr.co.jhta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.jhta.dto.ChatRoom;
import kr.co.jhta.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ChatRoomController {
	
	@Autowired
	private ChatRepository chatRepository;
	
	// /로 요청이 들어오면 전체 채팅룸 리스를 담아서 return
	@GetMapping("/")
	public String chatRoomList(Model model) {
		model.addAttribute("list", chatRepository.findAllRoom());
		
		log.info("채팅방 목록 {}", chatRepository.findAllRoom());
		
		return "roomList";
	}
	
	// 채팅방 생성
	@PostMapping("/chat/createroom")
	public String createRoom(@RequestParam("roomname")String name, RedirectAttributes attributes) {
		ChatRoom room = chatRepository.createChatRoom(name);
		log.info("채팅방이 생성 {} ", room);
		
		attributes.addFlashAttribute("roomName",room);
		return "redirect:/";
		
	}
	
	// 채팅방 입장
	@GetMapping("/chat/room")
	public String detailRoom(Model model,@RequestParam("roomId")String roomId) {
		log.info("roomId {}", roomId);
		model.addAttribute("room",chatRepository.findRoomById(roomId));
		return "chatroom";
	}
	
	
	
}
