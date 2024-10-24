package com.devcard.devcard.chat.controller;

import com.devcard.devcard.chat.dto.ChatRoomListResponse;
import com.devcard.devcard.chat.dto.ChatRoomResponse;
import com.devcard.devcard.chat.dto.CreateRoomRequest;
import com.devcard.devcard.chat.dto.CreateRoomResponse;
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

/**
 * 채팅방과 관련된 CRUD 기능을 관리
 * 채팅방 생성, 목록 조회, 특정 채팅방 조회 및 삭제 기능 제공
 */
@RestController
@RequestMapping("/chats")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    /**
     * 새로운 채팅방을 생성
     * @param createRoomRequest 채팅방 생성에 필요한 정보를 담은 요청 객체
     * @return 생성된 채팅방 정보와 상태 코드 201 반환
     */
    @PostMapping("/create")
    public ResponseEntity<CreateRoomResponse> createChatRoom(@RequestBody CreateRoomRequest createRoomRequest) {
        return ResponseEntity.status(201).body(chatRoomService.createChatRoom(createRoomRequest));
    }


    /**
     * 모든 채팅방 목록을 조회
     * @return 전체 채팅방 목록을 리스트 형태 반환
     */
    @GetMapping("")
    public ResponseEntity<List<ChatRoomListResponse>> getChatRoomList() {
        return ResponseEntity.ok(chatRoomService.getChatRoomList());
    }

    /**
     * 특정 ID의 채팅방을 조회
     * @param chatId 조회하려는 채팅방의 ID
     * @return 요청한 채팅방의 상세 정보 반환
     */
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatRoomResponse> getChatRoomById(@PathVariable String chatId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomById(chatId));
    }

    /**
     * 특정 ID의 채팅방을 삭제
     * @param chatId 삭제하려는 채팅방의 ID
     * @return 삭제가 완료되면 상태 코드 200 반환
     */
    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable String chatId) {
        chatRoomService.deleteChatRoom(chatId);
        return ResponseEntity.ok().build();
    }
}
