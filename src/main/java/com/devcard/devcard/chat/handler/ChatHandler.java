package com.devcard.devcard.chat.handler;

import com.devcard.devcard.chat.service.ChatRoomService;
import java.util.ArrayList;
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
        for (WebSocketSession webSocketSession : webSocketSessionList) {
            webSocketSession.sendMessage(textMessage);
        }
    }

    // 클라이언트 접속 시 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketSessionList.add(session);
    }

    // 클라이언트 접속 해제 시 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessionList.remove(session);
    }
}
