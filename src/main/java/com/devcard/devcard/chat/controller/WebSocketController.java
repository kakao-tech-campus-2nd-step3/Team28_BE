package com.devcard.devcard.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * WebSocket 연결을 관리
 * 클라이언트가 WebSocket을 통해 실시간 채팅 기능을 사용할 수 있는 엔드포인트 제공
 */
@RestController
public class WebSocketController {

    /**
     * WebSocket 연결 테스트를 위한 엔드포인트
     * @return 연결 성공 시 "chat" 문자열 반환
     */
    @GetMapping("/ws")
    public String chatGET() {
        return "chat";  // WebSocket 연결 테스트용, 실제 WebSocket 연결 로직은 별도로 처리
    }
}
