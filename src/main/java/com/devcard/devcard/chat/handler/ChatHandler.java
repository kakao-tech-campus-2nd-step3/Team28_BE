package com.devcard.devcard.chat.handler;

import com.devcard.devcard.chat.service.ChatRoomService;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatHandler extends TextWebSocketHandler {

    // service 계층 사용
    private final ChatRoomService chatRoomService;

    // 의존성 주입
    public ChatHandler(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        String chatId = chatRoomService.extractChatId(payload);
        String message = chatRoomService.extractMessage(payload);
        List<WebSocketSession> chatRoom = chatRoomService.getChatRoomSessions(chatId);

        if (chatRoom != null) { // 채팅방이 있을 때
            for (WebSocketSession webSocketSession : chatRoom) { // 해당 채팅방의 모든 세션에 대해서 메세지 보내기
                if (webSocketSession.isOpen()) { // 세션이 열려있다면
                    try {
                        webSocketSession.sendMessage(new TextMessage(message)); // 세션에 메세지 보내기
                    } catch (IOException e) { // 오류 일시적 처리
                        // 추가적으로 로그를 남길지? 고민하기
                    }
                }
            }
        }
    }

    // 클라이언트 접속 시 호출=> id를 추출하여 채팅방이 이미 존재하면 기존에 추가 없다면 생성후 추가
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String chatId = chatRoomService.extractChatIdFromSession(session);
        // 채팅방 존재 여부 확인
        if (!chatRoomService.existsChatRoom(chatId)) {// 채팅방이 존재 하지 않을 시 DB에 채팅방 생성
            // chatRoomService.createChatRoom(new CreateRoomRequest(chatId)); => 어떻게 참여자 id를 가져와 추가할 것인지 생각후
        }
        // 세션 추가
        chatRoomService.addSessionToChatRoom(chatId, session);
    }

    // 클라이언트 접속 해제 시 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String chatId = chatRoomService.extractChatIdFromSession(session);
        List<WebSocketSession> sessions = chatRoomService.getChatRoomSessions(chatId);
        if (sessions != null) {
            sessions.remove(session);
            // 채팅방을 제거하지 않고 해당 세션만 제거
        }
    }
}
