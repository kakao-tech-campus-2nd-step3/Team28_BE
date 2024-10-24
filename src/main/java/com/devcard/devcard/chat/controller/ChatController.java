package com.devcard.devcard.chat.controller;

import com.devcard.devcard.chat.dto.SendingMessageRequest;
import com.devcard.devcard.chat.dto.SendingMessageResponse;
import com.devcard.devcard.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 1. 메세지 전송
    @PostMapping("")
    public ResponseEntity<SendingMessageResponse> sendMessage(@RequestBody SendingMessageRequest sendingMessageRequest) {
        return ResponseEntity.status(201).body(chatService.sendMessage(sendingMessageRequest));
    }
}
