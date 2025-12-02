package com.example.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //웹소켓 선언
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:3000",
                        "https://test-react-delta-woad.vercel.app",
                        "https://*.onrender.com")

                .withSockJS(); //브라우저 호환성으로 인한 SockJS 사용
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //(수신) 주소 접두사
        registry.enableSimpleBroker("/topic");
        //(송신) 주소 접두사
        registry.setApplicationDestinationPrefixes("/app");
    }
}
