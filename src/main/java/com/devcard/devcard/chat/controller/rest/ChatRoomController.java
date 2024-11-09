package com.devcard.devcard.chat.controller.rest;

import com.devcard.devcard.chat.dto.ChatRoomListResponse;
import com.devcard.devcard.chat.dto.ChatRoomResponse;
import com.devcard.devcard.chat.dto.ChatUserResponse;
import com.devcard.devcard.chat.dto.CreateRoomRequest;
import com.devcard.devcard.chat.dto.CreateRoomResponse;
import com.devcard.devcard.chat.service.ChatRoomService;
import com.devcard.devcard.chat.service.ChatService;
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
 * <p></p>
 * 채팅방 생성, 목록 조회, 특정 채팅방 조회 및 삭제 기능 제공
 */
@RestController
@RequestMapping("/api/chats")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    public ChatRoomController(ChatRoomService chatRoomService, ChatService chatService) {
        this.chatRoomService = chatRoomService;
        this.chatService = chatService;
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
     * @return 전체 채팅방 목록을 리스트 형태로 반환
     */
    @GetMapping("")
    public ResponseEntity<List<ChatRoomListResponse>> getChatRoomList() {
        return ResponseEntity.ok(chatRoomService.getChatRoomList());
    }

    /**
     * 특정 유저가 참여하고 있는 채팅방 목록을 조회
     * @return 해당 채팅방 목록을 리스트 형태로 반환
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoomListResponse>> getChatRoomsByUser(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomsByUser(userId));
    }

    /**
     * 특정 ID의 채팅방을 상세 조회
     * @param chatId 조회하려는 채팅방의 ID
     * @return 요청한 채팅방의 상세 정보 반환
     */
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatRoomResponse> getChatRoomById(@PathVariable("chatId") String chatId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomById(chatId));
    }

    /**
     * 특정 ID의 채팅방을 삭제
     * @param chatId 삭제하려는 채팅방의 ID
     * @return 삭제가 완료되면 상태 코드 204 반환
     */
    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable("chatId") String chatId) {
        chatRoomService.deleteChatRoom(chatId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 사용자의 프로필 정보를 조회
     * @param userId 프로필 정보를 조회하려는 사용자의 ID
     * @return 해당 사용자의 닉네임과 프로필 이미지
     */
    @GetMapping("/user/{userId}/profile")
    public ResponseEntity<ChatUserResponse> getUserProfile(@PathVariable("userId") String userId) {
        ChatUserResponse userProfile = chatService.getUserProfileById(userId);
        return ResponseEntity.ok(userProfile);
    }
}
