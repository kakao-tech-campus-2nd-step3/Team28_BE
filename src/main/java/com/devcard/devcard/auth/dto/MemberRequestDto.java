package com.devcard.devcard.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberRequestDto {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    private String nickname;

    private String profileImg;

    public MemberRequestDto() {
    }

    public MemberRequestDto(String email, String nickname, String profileImg) {
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public String getProfileImg() { return profileImg; }
}
