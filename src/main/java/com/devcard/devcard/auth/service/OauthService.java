package com.devcard.devcard.auth.service;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

@Service
public class OauthService extends DefaultOAuth2UserService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);

//        System.out.println("userRequest:"+userRequest);
//        System.out.println("getClientRegistraion:"+userRequest.getClientRegistration());  //client에 대한 정보들이 받아짐
//        System.out.println("getAccessToken:"+userRequest.getAccessToken());
//        System.out.println("getAttributes:"+super.loadUser(userRequest).getAttributes()); //유저 정보를 받아옴

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Integer githubIdInt = (Integer) attributes.get("id");
        String githubId = String.valueOf(githubIdInt);

        // 데이터베이스에 이미 등록된 사용자인지 확인
        Member existingMember = memberRepository.findByGithubId(githubId);
        if (existingMember == null) {
            // 사용자가 없으면 새로 저장
            Member newMember = new Member(
                    githubId,
                    (String) attributes.get("email"),
                    (String) attributes.get("avatar_url"),
                    (String) attributes.get("name"),
                    (String) attributes.get("login"),
                    "ROLE_USER",  // 기본 역할 설정
                    new Timestamp(System.currentTimeMillis())
            );
            memberRepository.save(newMember);
        }

        return super.loadUser(userRequest);
    }
}