package com.devcard.devcard.card.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CardRequestDto {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private final String name;

    @NotBlank(message = "회사는 필수 입력 항목입니다.")
    private final String company;

    private final String position;

    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private final String email;

    @Pattern(regexp = "^[+]?[0-9\\-\\s]+$", message = "유효한 전화번호 형식을 입력하세요.")
    private final String phone;

    public CardRequestDto(String name, String company, String position, String email, String phone) {
        this.name = name;
        this.company = company;
        this.position = position;
        this.email = email;
        this.phone = phone;
    }

    public String getName() { return name; }
    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

}
