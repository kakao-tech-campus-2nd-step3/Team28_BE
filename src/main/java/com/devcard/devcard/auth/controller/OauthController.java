package com.devcard.devcard.auth.controller;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

@Controller
public class OauthController {
    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String githubId = String.valueOf(oAuth2User.getAttributes().get("id"));

        System.out.println("깃허브 아이디: " + githubId);

        // GitHub ID로 사용자를 찾아서 등록 여부 확인
        Member member = memberRepository.findByGithubId(githubId);
        boolean isRegistered = (member != null);

        Map<String, Object> response = new HashMap<>();
        response.put("is_registered", isRegistered);

        return ResponseEntity.ok(response);

    }
}
