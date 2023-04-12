package kr.co.jhta.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// stomp 접속 주소 url ==> /ws-stomp
		registry.addEndpoint("/ws-stomp") // 연결될 end point
				.withSockJS();			  // SockJS 를 연결 
				
	}
	
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		// 메세지를 구독 요청 url ==> /sub
		registry.enableSimpleBroker("/sub");
		
		// 메세지를 발행하는 요청 url ==> /pub
		registry.setApplicationDestinationPrefixes("/pub");
		
	}
	
	
	
}
