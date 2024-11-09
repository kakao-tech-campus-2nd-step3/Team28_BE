package com.devcard.devcard.chat.controller.page;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.repository.MemberRepository;
import com.devcard.devcard.chat.service.ChatRoomService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    private final MemberRepository memberRepository;

    public ChatController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 채팅 목록 페이지로 이동하는 엔드포인트
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @param authentication 인증 정보를 포함하는 객체 (OAuth2 사용자 정보 포함)
     * @return 채팅 목록 페이지 템플릿 (chat-list.html)의 이름
     */
    @GetMapping("/chats")
    public String getChatList(Model model, Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String githubId = String.valueOf(oAuth2User.getAttributes().get("id"));

        // GitHub ID로 사용자를 찾아서 등록 여부 확인
        Member member = memberRepository.findByGithubId(githubId);

        model.addAttribute("memberId", member.getId());
        model.addAttribute("nickname", member.getNickname());
        return "chat-list";
    }

    /**
     * 특정 채팅방 페이지로 이동하는 엔드포인트
     * @param chatId 조회하려는 채팅방의 ID
     * @param model  뷰에 데이터를 전달하기 위한 모델 객체
     * @param authentication 인증 정보를 포함하는 객체 (OAuth2 사용자 정보 포함)
     * @return 특정 채팅방 페이지 템플릿 (chat-room.html)의 이름
     */
    @GetMapping("/chats/{chatId}")
    public String getChatRoom(@PathVariable("chatId") Long chatId, Model model, Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String githubId = String.valueOf(oAuth2User.getAttributes().get("id"));

        // GitHub ID로 사용자를 찾아서 등록 여부 확인
        Member member = memberRepository.findByGithubId(githubId);

        model.addAttribute("memberId", member.getId());
        model.addAttribute("nickname", member.getNickname());
        model.addAttribute("chatId", chatId);
        return "chat-room";
    }
}
