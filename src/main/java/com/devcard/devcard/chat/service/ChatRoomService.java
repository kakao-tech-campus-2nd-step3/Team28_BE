package com.devcard.devcard.chat.service;

import static com.devcard.devcard.chat.util.Constants.CHAT_ROOM_NOT_FOUND;

import com.devcard.devcard.chat.dto.ChatMessageResponse;
import com.devcard.devcard.chat.dto.ChatRoomListResponse;
import com.devcard.devcard.chat.dto.ChatRoomResponse;
import com.devcard.devcard.chat.dto.CreateRoomRequest;
import com.devcard.devcard.chat.dto.CreateRoomResponse;
import com.devcard.devcard.chat.dto.SendingMessageRequest;
import com.devcard.devcard.chat.dto.SendingMessageResponse;
import com.devcard.devcard.chat.exception.room.ChatRoomNotFoundException;
import com.devcard.devcard.chat.model.ChatMessage;
import com.devcard.devcard.chat.model.ChatRoom;
import com.devcard.devcard.chat.model.ChatUser;
import com.devcard.devcard.chat.repository.ChatMessageRepository;
import com.devcard.devcard.chat.repository.ChatRoomRepository;
import com.devcard.devcard.chat.repository.ChatUserRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

@Service
@Transactional
public class ChatRoomService {

    // id를 통해 채팅방을 관리 및 id 와 List<WebSocketSession>을 통해 해당 채팅방의 각 세션 즉 사용자 관리 아래 세션리스트 serivice계층으로 이동
    private static final ConcurrentMap<String, List<WebSocketSession>> chatRoomSessions = new ConcurrentHashMap<>();

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatUserRepository chatUserRepository;

    public ChatRoomService(
        ChatRoomRepository chatRoomRepository,
        ChatMessageRepository chatMessageRepository,
        ChatUserRepository chatUserRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatUserRepository = chatUserRepository;
    }

    // 채팅방 생성
    public CreateRoomResponse createChatRoom(CreateRoomRequest createRoomRequest) {
        // jpa를 이용해 ChatUser 리스트 가져오기
        List<ChatUser> participants = chatUserRepository.findByIdIn(createRoomRequest.getParticipantsId());
        ChatRoom chatRoom = new ChatRoom(participants, LocalDateTime.now()); // chatRoom생성
        chatRoomRepository.save(chatRoom); // db에 저장
        return makeCreateChatRoomResponse(chatRoom); // Response로 변환
    }

    // 1. 메세지 보내기
    public SendingMessageResponse sendMessage(SendingMessageRequest sendingMessageRequest) {
        // 메세지 전송 로직( jpa, h2-db, 소켓 등...)
        return new SendingMessageResponse(123L, LocalDateTime.now());
    }

    // 2. 전체 채팅방 목록 조회
    public List<ChatRoomListResponse> getChatRoomList() {
        // 채팅방 목록을 가져와 알맞는 Response 변경 후 리턴
        return chatRoomRepository.findAll().stream().map(chatRoom -> new ChatRoomListResponse(
            chatRoom.getId(),
            chatRoom.getParticipantsName(),
            chatRoom.getLastMessage(),
            chatRoom.getLastMessageTime()
        )).toList();
    }

    // 3. 특정 채팅방 조회
    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoomById(String chatId) {
        Long chatRoomId = extractChatRoomId(chatId);

        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new ChatRoomNotFoundException(CHAT_ROOM_NOT_FOUND + chatRoomId));

        // 메시지 조회
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
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

    // 4. 채팅방 삭제
    public void deleteChatRoom(String chatId) {
        Long chatRoomId = extractChatRoomId(chatId);

        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new ChatRoomNotFoundException(CHAT_ROOM_NOT_FOUND + chatRoomId));

        // 관련된 메시지를 먼저 삭제
        chatMessageRepository.deleteByChatRoomId(chatRoomId);

        // 채팅방 삭제
        chatRoomRepository.deleteById(chatRoomId);
    }

    public boolean existsChatRoom(String chatId) {
        return chatRoomRepository.existsById(Long.parseLong(chatId)); // Long으로 변환=> 올바른 로직인가
    }

    // chatId에 해당되는 값이 있으면 해당값에 추가 없으면 생성 후 추가
    public void addSessionToChatRoom(String chatId, WebSocketSession session) {
        // computIfAbsent메소드: 키값이 없으면 해당되는 키값으로 생성 해 리턴, 있다면 해당 값 리턴
        chatRoomSessions.computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>()).add(session);
    }

    // 해당되는 세션 리스트 리턴
    public List<WebSocketSession> getChatRoomSessions(String chatId) {
        return chatRoomSessions.get(chatId);
    }

    // chatId 에서 숫자만 추출하는 메서드
    private Long extractChatRoomId(String chatId) {
        return Long.parseLong(chatId.replace("chat_", ""));
    }

    // CreateChatRoomResponse를 만드는 메소드
    public CreateRoomResponse makeCreateChatRoomResponse(ChatRoom chatRoom) {
        return new CreateRoomResponse(
            "chat_" + chatRoom.getId(),
            chatRoom.getParticipantsName(),
            chatRoom.getCreatedAt()
        );
    }

    public String extractChatId(String payload) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(payload);
            return jsonObject.getAsString("chatId");
        } catch (ParseException e) {
            e.printStackTrace(); // 예외를 로깅
            return null; // 예외 발생 시 null 반환
        }
    }

    // 메세지 payload로 부터 message 추출하는 메소드
    public String extractMessage(String payload) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(payload);
            return jsonObject.getAsString("message");
        } catch (ParseException e) {
            e.printStackTrace(); // 예외를 로깅
            return null; // 예외 발생 시 null 반환
        }
    }

    // 세션으로 부터 chatId 추출
    public String extractChatIdFromSession(WebSocketSession session) {
        String uri = session.getUri().toString();
        return extractChatIdFromUri(uri);
    }

    // uri 로부터 전달된 chatId 추출
    private String extractChatIdFromUri(String uri) {
        // uir가 ws://localhost:8080/ws?chatId=12345&userId=67890로 요청이 들어온다면
        try {
            return Stream
                .of(new URI(uri).getQuery()
                    .split("&")) //URI에서 쿼리문자열에서 &로 구분 지어 매개변수 분리 및 스트림으로 변환 ex) chatId=12345&userId=67890
                .map(param -> param.split("=")) // 매개변수에서 =을 제거하여 key - value로 변경 [["chatId", "12345"], ["userId", "67890"]]
                .filter(values -> values.length == 2
                    && "chatId".equals(values[0])) // 요소가 2개 이면서 chatId인 것만 가져오기 ["chatId","12345"]]
                .map(pair -> pair[1])// 2번째인 value 값가져와 chatId 추출 ["12345"]
                .findFirst() // chatId 가져오기 "12345"
                .orElse(null); // 없다면 null값 리턴
        } catch (URISyntaxException e) { // 오류 임시적으로 처리
            return null;
        }
    }
}
