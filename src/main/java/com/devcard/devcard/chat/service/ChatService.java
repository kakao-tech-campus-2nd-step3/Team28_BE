package com.devcard.devcard.chat.service;

import com.devcard.devcard.chat.dto.SendingMessageRequest;
import com.devcard.devcard.chat.dto.SendingMessageResponse;
import com.devcard.devcard.chat.repository.ChatRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

/**
 * 실시간 채팅 관련 로직을 처리
 * 사용자가 채팅방에 참여하는 로직
 * 메시지를 주고받는 로직
 */
@Service
@Transactional
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    // id를 통해 채팅방을 관리 및 id 와 List<WebSocketSession>을 통해 해당 채팅방의 각 세션 즉 사용자 관리 아래 세션리스트 serivice계층으로 이동
    private static final ConcurrentMap<Long, List<WebSocketSession>> chatRoomSessions = new ConcurrentHashMap<>();

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    /**
     * 메시지를 전송
     * @param sendingMessageRequest 전송하려는 메시지 정보를 담은 요청 객체
     * @return 전송된 메시지 정보와 타임스탬프를 포함하는 응답 객체
     */
    public SendingMessageResponse sendMessage(SendingMessageRequest sendingMessageRequest) {
        // 메세지 전송 로직( jpa, h2-db, 소켓 등...)
        return new SendingMessageResponse(123L, LocalDateTime.now());
    }

    /**
     * 특정 채팅방에 WebSocketSession 추가
     * @param chatId  채팅방 ID
     * @param session 추가할 WebSocketSession
     */
    public void addSessionToChatRoom(Long chatId, WebSocketSession session) {
        // computIfAbsent메소드: 키값이 없으면 해당되는 키값으로 생성 해 리턴, 있다면 해당 값 리턴
        chatRoomSessions.computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>()).add(session);
    }

    /**
     * 특정 채팅방에 연결된 모든 WebSocketSession 리스트 반환
     * @param chatId 채팅방 ID
     * @return 해당 채팅방에 연결된 세션 목록
     */
    public List<WebSocketSession> getChatRoomSessions(Long chatId) {
        return chatRoomSessions.get(chatId);
    }

    /**
     * 메시지 payload에서 chatId 추출
     * @param payload 메시지의 payload
     * @return 추출된 chatId 값 (존재하지 않으면 null 반환)
     */
    public Long extractChatId(String payload) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(payload);
            String chatIdStr = jsonObject.getAsString("chatId");
            if (chatIdStr != null) {
                return Long.parseLong(chatIdStr);
            }
            return null;
        } catch (ParseException | NumberFormatException e) {
            logger.error("payload에서 chatId 추출 실패: {}", payload, e);
            return null; // 예외 발생 시 null 반환
        }
    }

    /**
     * 메시지 payload에서 message 값 추출
     * @param payload 메시지의 payload
     * @return 추출된 메시지 내용 (존재하지 않으면 null 반환)
     */
    public String extractMessage(String payload) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(payload);
            return jsonObject.getAsString("message");
        } catch (ParseException e) {
            logger.error("payload에서 message 추출 실패: {}", payload, e);
            return null; // 예외 발생 시 null 반환
        }
    }

    /**
     * WebSocketSession에서 chatId를 추출
     * @param session WebSocketSession 객체
     * @return 추출된 chatId 값
     */
    public Long extractChatIdFromSession(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        return extractChatIdFromUri(uri);
    }

    /**
     * URI에서 chatId를 추출
     * @param uri WebSocket URI
     * @return URI에 포함된 chatId 값 (존재하지 않으면 null 반환)
     */
    private Long extractChatIdFromUri(String uri) {
        // uir가 ws://localhost:8080/ws?chatId=12345&userId=67890로 요청이 들어온다면
        try {
            return Stream
                .of(new URI(uri).getQuery()
                    .split("&")) //URI에서 쿼리문자열에서 &로 구분 지어 매개변수 분리 및 스트림으로 변환 ex) chatId=12345&userId=67890
                .map(param -> param.split("=")) // 매개변수에서 =을 제거하여 key - value로 변경 [["chatId", "12345"], ["userId", "67890"]]
                .filter(values -> values.length == 2
                    && "chatId".equals(values[0])) // 요소가 2개 이면서 chatId인 것만 가져오기 ["chatId","12345"]]
                .map(pair -> Long.parseLong(pair[1])) // 2번째인 value 값가져와 chatId 추출 ["12345"], chatId를 Long으로 변환
                .findFirst() // chatId 가져오기 "12345"
                .orElse(null); // 없다면 null값 리턴
        } catch (URISyntaxException | NumberFormatException e) {
            logger.error("URI에서 chatId 추출 실패: {}", uri, e);
            return null;
        }
    }

    /**
     * WebSocketSession에서 userId를 추출
     * @param session WebSocketSession 객체
     * @return 추출된 userId 값
     */
    public Long extractUserIdFromSession(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        return extractUserIdFromUri(uri);
    }

    /**
     * URI에서 userId를 추출
     * @param uri WebSocket URI
     * @return URI에 포함된 userId 값 (존재하지 않으면 null 반환)
     */
    private Long extractUserIdFromUri(String uri) {
        try {
            return Stream
                .of(new URI(uri).getQuery().split("&")) // 쿼리 문자열에서 &로 분리
                .map(param -> param.split("=")) // =으로 key-value 분리
                .filter(values -> values.length == 2 && "userId".equals(values[0])) // userId 필터링
                .map(pair -> Long.parseLong(pair[1])) // userId 값 추출 후 Long으로 변환
                .findFirst() // 첫 번째 값 반환
                .orElse(null); // 없으면 null 반환
        } catch (URISyntaxException e) {
            logger.error("URI에서 userId 추출 실패: {}", uri, e);
            return null;
        }
    }
}
