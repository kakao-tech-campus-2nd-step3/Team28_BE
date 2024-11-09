package com.devcard.devcard.auth.dto;

import com.devcard.devcard.auth.entity.Member;

import java.sql.Timestamp;

public class MemberResponseDto {

    private Long id;
    private String githubId;
    private String email;
    private String profileImg;
    private String username;
    private String nickname;
    private Timestamp createDate;

    public MemberResponseDto() {
    }

    public MemberResponseDto(Long id, String githubId, String email, String profileImg, String username, String nickname, Timestamp createDate) {
        this.id = id;
        this.githubId = githubId;
        this.email = email;
        this.profileImg = profileImg;
        this.username = username;
        this.nickname = nickname;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public String getGithubId() {
        return githubId;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public static MemberResponseDto fromEntity(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getGithubId(),
                member.getEmail(),
                member.getProfileImg(),
                member.getUsername(),
                member.getNickname(),
                member.getCreateDate()
        );
    }
}
