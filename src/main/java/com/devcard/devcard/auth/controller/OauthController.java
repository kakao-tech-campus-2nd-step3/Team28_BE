package com.devcard.devcard.auth.controller;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OauthController {
    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> requestData) {
        String githubId = requestData.get("github_id");

        // GitHub ID로 사용자를 찾아서 등록 여부 확인
        Member member = memberRepository.findByGithubId(githubId);
        boolean isRegistered = (member != null);

        Map<String, Object> response = new HashMap<>();
        response.put("is_registered", isRegistered);

        return ResponseEntity.ok(response);
    }
}
