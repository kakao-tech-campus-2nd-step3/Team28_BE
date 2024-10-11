package com.devcard.devcard.card.entity;

import com.devcard.devcard.card.dto.CardRequestDto;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String githubId;
    private String name;
    private String company;
    private String position;
    private String email;
    private String phone;
    private String profilePicture;
    private String bio;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 기본 생성자 (JPA 요구 사항)
    protected Card() {
    }

    // 빌더 클래스
    public static class Builder {
        private String githubId;
        private String name;
        private String company;
        private String position;
        private String email;
        private String phone;
        private String profilePicture;
        private String bio;

        public Builder() {
        }

        public Builder githubId(String githubId) {
            this.githubId = githubId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder company(String company) {
            this.company = company;
            return this;
        }

        public Builder position(String position) {
            this.position = position;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder profilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }

    // 빌더 사용 생성자
    private Card(Builder builder) {
        this.githubId = builder.githubId;
        this.name = builder.name;
        this.company = builder.company;
        this.position = builder.position;
        this.email = builder.email;
        this.phone = builder.phone;
        this.profilePicture = builder.profilePicture;
        this.bio = builder.bio;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getter
    public Long getId() { return id; }
    public String getGithubId() { return githubId; }
    public String getName() { return name; }
    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getProfilePicture() { return profilePicture; }
    public String getBio() { return bio; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // DTO 기반 업데이트 메서드
    public void updateFromDto(CardRequestDto dto) {
        if (dto.getName() != null) this.name = dto.getName();
        if (dto.getCompany() != null) this.company = dto.getCompany();
        if (dto.getPosition() != null) this.position = dto.getPosition();
        if (dto.getEmail() != null) this.email = dto.getEmail();
        if (dto.getPhone() != null) this.phone = dto.getPhone();
        this.updatedAt = LocalDateTime.now();
    }
}
