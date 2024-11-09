package com.devcard.devcard.card.dto;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.entity.Card;

import java.time.LocalDateTime;

public class CardResponseDto {

    private final Long id;
    private final String name;
    private final String company;
    private final String position;
    private final String email;
    private final String phone;
    private final String githubId;
    private final String bio;
    private final String profilePicture;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CardResponseDto(Long id, String name, String company, String position, String email, String phone,
                           String githubId, String bio, String profilePicture,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.githubId = githubId;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getGithubId() { return githubId; }
    public String getBio() { return bio; }
    public String getProfilePicture() { return profilePicture; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static CardResponseDto fromEntity(Card card) {
        Member member = card.getMember();
        return new CardResponseDto(
                card.getId(),
                member.getUsername(),
                card.getCompany(),
                card.getPosition(),
                member.getEmail(),
                card.getPhone(),
                member.getGithubId(),
                card.getBio(),
                member.getProfileImg(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}
