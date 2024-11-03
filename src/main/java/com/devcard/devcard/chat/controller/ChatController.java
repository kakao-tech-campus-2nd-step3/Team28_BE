package com.devcard.devcard.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 클라이언트에서 접근할 수 있는 채팅 관련 페이지들을 관리
 * <p></p>
 * 채팅 목록과 특정 채팅방 페이지로의 라우팅을 담당<br>
 * 서버에서 뷰 템플릿을 렌더링하여 반환
 */
@Controller
public class ChatController {

    /**
     * 채팅 목록 페이지로 이동하는 엔드포인트
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 채팅 목록 페이지 템플릿 (chat-list.html)의 이름
     */
    @GetMapping("/chats")
    public String getChatList(Model model) {
        return "chat-list";  // 채팅 목록 페이지를 반환합니다.
    }

    /**
     * 특정 채팅방 페이지로 이동하는 엔드포인트
     * @param chatId 조회하려는 채팅방의 ID
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 특정 채팅방 페이지 템플릿 (chat-room.html)의 이름
     */
    @GetMapping("/chats/{chatId}")
    public String getChatRoom(@PathVariable Long chatId, Model model) {
        model.addAttribute("chatId", chatId);
        return "chat-room";
    }
}
