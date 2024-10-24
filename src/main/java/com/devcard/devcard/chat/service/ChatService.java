package com.devcard.devcard.chat.service;

import com.devcard.devcard.chat.dto.SendingMessageRequest;
import com.devcard.devcard.chat.dto.SendingMessageResponse;
import com.devcard.devcard.chat.repository.ChatRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

@Service
@Transactional
public class ChatService {

    // id를 통해 채팅방을 관리 및 id 와 List<WebSocketSession>을 통해 해당 채팅방의 각 세션 즉 사용자 관리 아래 세션리스트 serivice계층으로 이동
    private static final ConcurrentMap<String, List<WebSocketSession>> chatRoomSessions = new ConcurrentHashMap<>();

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    // 메세지 보내기
    public SendingMessageResponse sendMessage(SendingMessageRequest sendingMessageRequest) {
        // 메세지 전송 로직( jpa, h2-db, 소켓 등...)
        return new SendingMessageResponse(123L, LocalDateTime.now());
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
