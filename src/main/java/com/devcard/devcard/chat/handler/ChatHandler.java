package com.devcard.devcard.chat.handler;

import com.devcard.devcard.chat.dto.CreateRoomRequest;
import com.devcard.devcard.chat.service.ChatRoomService;
import com.devcard.devcard.chat.service.ChatService;
import java.io.IOException;
import java.util.List;
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

    private static final Logger logger = LoggerFactory.getLogger(ChatHandler.class);

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    public ChatHandler(ChatRoomService chatRoomService, ChatService chatService) {
        this.chatRoomService = chatRoomService;
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
        Long chatId = chatService.extractChatId(payload);
        String message = chatService.extractMessage(payload);
        List<WebSocketSession> chatRoom = chatService.getChatRoomSessions(chatId);

        if (chatRoom != null) {
            for (WebSocketSession webSocketSession : chatRoom) { // 해당 채팅방의 모든 세션에 대해서 메세지 보내기
                if (webSocketSession.isOpen()) { // 세션이 열려있다면
                    try {
                        webSocketSession.sendMessage(new TextMessage(message)); // 세션에 메세지 보내기
                    } catch (IOException e) { // 오류 일시적 처리
                        logger.error("세션에 메시지 보내기 실패: {}", webSocketSession.getId(), e);
                    }
                }
            }
        } else {
            logger.warn("다음의 chatId로 채팅방 찾기 실패: {}", chatId);
        }
    }

    /**
     * WebSocket 연결이 성공적으로 이루어졌을 때 호출
     * id를 추출하여 채팅방이 이미 존재하면 기존에 추가 없다면 생성후 추가
     * @param session WebSocketSession 객체
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long chatId = chatService.extractChatIdFromSession(session);
        Long userId = chatService.extractUserIdFromSession(session);

        // 채팅방 존재 여부 확인 ( ## 검토 필요 )
        if (!chatRoomService.existsChatRoom(chatId)) {
            // 참여자 ID 리스트를 CreateRoomRequest에 추가
            List<Long> participantsId = List.of(userId); // userId를 Long 타입으로 변환하여 리스트로 추가
            CreateRoomRequest createRoomRequest = new CreateRoomRequest();
            createRoomRequest.setParticipantsId(participantsId);

            // 새로운 채팅방 생성
            chatRoomService.createChatRoom(createRoomRequest);
        }

        // 세션 추가
        chatService.addSessionToChatRoom(chatId, session);
    }

    /**
     * WebSocket 연결이 종료되었을 때 호출
     * @param session WebSocketSession 객체
     * @param status  연결 종료 상태
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long chatId = chatService.extractChatIdFromSession(session);
        List<WebSocketSession> sessions = chatService.getChatRoomSessions(chatId);
        if (sessions != null) {
            sessions.remove(session);
            // 채팅방을 제거하지 않고 해당 세션만 제거
        }
    }
}
