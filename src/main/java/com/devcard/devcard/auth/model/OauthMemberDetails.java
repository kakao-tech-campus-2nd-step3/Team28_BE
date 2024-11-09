package com.devcard.devcard.auth.model;

import com.devcard.devcard.auth.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class OauthMemberDetails implements OAuth2User {
    private Member member;
    private Map<String, Object> attributes;

    //    OAuth 로그인
    public OauthMemberDetails(Member member, Map<String, Object> attributes){
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collet = new ArrayList<GrantedAuthority>();
        collet.add(()-> member.getRole());
        return collet;
    }

    @Override
    public String getName() {
        return member.getId()+"";
    }

    public Member getMember() { return member; }
}
