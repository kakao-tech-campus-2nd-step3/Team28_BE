package com.devcard.devcard.chat.controller;

import com.devcard.devcard.chat.handler.ChatHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


/**
 * WebSocket 연결을 관리
 * 클라이언트가 WebSocket을 통해 실시간 채팅 기능을 사용할 수 있는 엔드포인트 제공
 */
@RestController
@EnableWebSocket
public class WebSocketController implements WebSocketConfigurer {

    private final ChatHandler chatHandler;

    public WebSocketController(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    /**
     * WebSocket 연결 테스트를 위한 엔드포인트
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        // WebSocket 연결을 위한 엔드포인트 설정
        webSocketHandlerRegistry.addHandler(chatHandler, "/ws").setAllowedOrigins("*");
    }
}
