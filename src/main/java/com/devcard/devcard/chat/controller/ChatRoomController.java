package com.devcard.devcard.chat.controller;

import com.devcard.devcard.chat.dto.ChatRoomListResponse;
import com.devcard.devcard.chat.dto.ChatRoomResponse;
import com.devcard.devcard.chat.dto.CreateRoomRequest;
import com.devcard.devcard.chat.dto.CreateRoomResponse;
import com.devcard.devcard.chat.dto.SendingMessageRequest;
import com.devcard.devcard.chat.dto.SendingMessageResponse;
import com.devcard.devcard.chat.service.ChatRoomService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    // 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<CreateRoomResponse> createChatRoom(@RequestBody CreateRoomRequest createRoomRequest) {
        return ResponseEntity.status(201).body(chatRoomService.createChatRoom(createRoomRequest));
    }

    // 1. 메세지 전송
    @PostMapping("")
    public ResponseEntity<SendingMessageResponse> sendMessage(@RequestBody SendingMessageRequest sendingMessageRequest) {
        return ResponseEntity.status(201).body(chatRoomService.sendMessage(sendingMessageRequest));
    }

    // 2. 전체 채팅 목록 조회
    @GetMapping("")
    public ResponseEntity<List<ChatRoomListResponse>> getChatRoomList() {
        return ResponseEntity.ok(chatRoomService.getChatRoomList());
    }

    // 3. 특정 채팅방 조회
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatRoomResponse> getChatRoomById(@PathVariable String chatId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomById(chatId));
    }

    // 4. 특정 채팅방 삭제
    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable String chatId) {
        chatRoomService.deleteChatRoom(chatId);
        return ResponseEntity.ok().build();
    }
}
