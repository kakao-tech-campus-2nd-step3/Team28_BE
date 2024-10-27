package com.devcard.devcard.chat.service;

import static com.devcard.devcard.chat.util.Constants.CHAT_ROOM_NOT_FOUND;

import com.devcard.devcard.chat.dto.ChatMessageResponse;
import com.devcard.devcard.chat.dto.ChatRoomListResponse;
import com.devcard.devcard.chat.dto.ChatRoomResponse;
import com.devcard.devcard.chat.dto.CreateRoomRequest;
import com.devcard.devcard.chat.dto.CreateRoomResponse;
import com.devcard.devcard.chat.exception.room.ChatRoomNotFoundException;
import com.devcard.devcard.chat.model.ChatMessage;
import com.devcard.devcard.chat.model.ChatRoom;
import com.devcard.devcard.chat.model.ChatUser;
import com.devcard.devcard.chat.repository.ChatRepository;
import com.devcard.devcard.chat.repository.ChatRoomRepository;
import com.devcard.devcard.chat.repository.ChatUserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 채팅방과 관련된 로직을 처리
 * 채팅방 생성, 조회, 삭제 로직
 */
@Service
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;

    public ChatRoomService(
        ChatRoomRepository chatRoomRepository,
        ChatRepository chatRepository,
        ChatUserRepository chatUserRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRepository = chatRepository;
        this.chatUserRepository = chatUserRepository;
    }

    /**
     * 새로운 채팅방 생성
     * @param createRoomRequest 채팅방 생성 요청 객체
     * @return 생성된 채팅방 정보
     */
    public CreateRoomResponse createChatRoom(CreateRoomRequest createRoomRequest) {
        // jpa를 이용해 ChatUser 리스트 가져오기
        List<ChatUser> participants = chatUserRepository.findByIdIn(createRoomRequest.getParticipantsId());
        ChatRoom chatRoom = new ChatRoom(participants, LocalDateTime.now()); // chatRoom생성
        chatRoomRepository.save(chatRoom); // db에 저장
        return makeCreateChatRoomResponse(chatRoom); // Response로 변환
    }


    /**
     * 모든 채팅방 목록 조회
     * @return 전체 채팅방 목록
     */
    public List<ChatRoomListResponse> getChatRoomList() {
        // 채팅방 목록을 가져와 알맞는 Response 변경 후 리턴
        return chatRoomRepository.findAll().stream().map(chatRoom -> new ChatRoomListResponse(
            chatRoom.getId(),
            chatRoom.getParticipantsName(),
            chatRoom.getLastMessage(),
            chatRoom.getLastMessageTime()
        )).toList();
    }

    /**
     * 특정 채팅방의 세부 정보 조회
     * @param chatId 채팅방 ID
     * @return 채팅방 세부 정보
     */
    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoomById(String chatId) {
        Long chatRoomId = extractChatRoomId(chatId);

        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new ChatRoomNotFoundException(CHAT_ROOM_NOT_FOUND + chatRoomId));

        // 메시지 조회
        List<ChatMessage> messages = chatRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
        List<ChatMessageResponse> messageResponses = messages.stream()
            .map(message -> new ChatMessageResponse(
                message.getSender(),
                message.getContent(),
                message.getTimestamp()
            ))
            .collect(Collectors.toList());

        // 참가자 이름 조회
        List<String> participants = chatRoom.getParticipants().stream()
            .map(ChatUser::getName)
            .collect(Collectors.toList());

        return new ChatRoomResponse(
            "chat_" + chatRoomId,
            participants,
            messageResponses
        );
    }

    /**
     * 채팅방 삭제
     * @param chatId 채팅방 ID
     */
    public void deleteChatRoom(String chatId) {
        Long chatRoomId = extractChatRoomId(chatId);

        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new ChatRoomNotFoundException(CHAT_ROOM_NOT_FOUND + chatRoomId));

        // 관련된 메시지를 먼저 삭제
        chatRepository.deleteByChatRoomId(chatRoomId);

        // 채팅방 삭제
        chatRoomRepository.deleteById(chatRoomId);
    }

    /**
     * 특정 채팅방이 존재하는지 확인
     * @param chatId 채팅방 ID
     * @return 존재 여부
     */
    public boolean existsChatRoom(Long chatId) {
        return chatRoomRepository.existsById(chatId);
    }

    /**
     * chatId에서 숫자 부분만 추출
     * @param chatId 채팅방 ID
     * @return 추출된 숫자 ID
     */
    private Long extractChatRoomId(String chatId) {
        return Long.parseLong(chatId.replace("chat_", ""));
    }

    /**
     * 생성된 채팅방 정보를 응답 객체로 변환
     * @param chatRoom 생성된 채팅방
     * @return 채팅방 생성 응답 객체
     */
    public CreateRoomResponse makeCreateChatRoomResponse(ChatRoom chatRoom) {
        return new CreateRoomResponse(
            "chat_" + chatRoom.getId(),
            chatRoom.getParticipantsName(),
            chatRoom.getCreatedAt()
        );
    }
}
