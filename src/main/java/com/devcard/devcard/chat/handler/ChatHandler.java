package com.devcard.devcard.chat.handler;

import com.devcard.devcard.chat.service.ChatService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket을 통해 실시간 메시지 전송을 관리하는 핸들러
 * 클라이언트의 메시지 전송 및 접속/해제 이벤트 처리
 */
@Component
public class ChatHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final ChatService chatService;

    public ChatHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 클라이언트로부터 메시지를 수신했을 때 호출되는 메소드
     * @param session     WebSocketSession 객체
     * @param textMessage 수신한 텍스트 메시지
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        Long chatId = chatService.extractChatIdFromSession(session);  // URI에서 chatId,
        Long userId = chatService.extractUserIdFromSession(session);  // URI에서 userId 추출
        String message = chatService.extractMessage(payload);  // 페이로드에서 message만 추출

        chatService.handleIncomingMessage(chatId, userId, message);
    }

    /**
     * WebSocket 연결이 성공적으로 이루어졌을 때 호출
     * id를 추출하여 채팅방이 이미 존재하면 기존에 추가 없다면 생성후 추가
     * @param session WebSocketSession 객체
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            Long chatId = chatService.extractChatIdFromSession(session);
            Long userId = chatService.extractUserIdFromSession(session);
            if (chatId == null || userId == null) {
                session.close(CloseStatus.BAD_DATA);
                logger.error("잘못된 chatId 또는 userId로 WebSocket 연결 시도: {}", session.getUri());
                return;
            }
            chatService.addSessionToChatRoom(chatId, session);
        } catch (Exception e) {
            logger.error("WebSocket 연결 처리 중 오류 발생: {}", e.getMessage());
            try {
                session.close(CloseStatus.BAD_DATA);
            } catch (IOException ioException) {
                logger.error("WebSocket 연결 종료 실패: {}", ioException.getMessage());
            }
        }
    }

    /**
     * WebSocket 연결이 종료되었을 때 호출
     * @param session WebSocketSession 객체
     * @param status  연결 종료 상태
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long chatId = chatService.extractChatIdFromSession(session);
        chatService.removeSessionFromChatRoom(chatId, session);
    }
}
