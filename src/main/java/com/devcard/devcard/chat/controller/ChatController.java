package com.devcard.devcard.chat.controller;

import com.devcard.devcard.chat.dto.SendingMessageRequest;
import com.devcard.devcard.chat.dto.SendingMessageResponse;
import com.devcard.devcard.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 실시간 채팅(메시지 전송) 기능 관리
 * 메시지 전송  API 제공
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 클라이언트로부터 메시지를 받아 해당 채팅방으로 전송
     * @param sendingMessageRequest 전송하려는 메시지 정보를 담은 요청 객체
     * @return 전송된 메시지 정보와 상태 코드 201 반환
     */
    @PostMapping("")
    public ResponseEntity<SendingMessageResponse> sendMessage(@RequestBody SendingMessageRequest sendingMessageRequest) {
        return ResponseEntity.status(201).body(chatService.sendMessage(sendingMessageRequest));
    }
}
