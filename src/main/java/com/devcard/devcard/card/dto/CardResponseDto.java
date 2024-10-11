package com.devcard.devcard.card.dto;

import com.devcard.devcard.card.entity.Card;

import java.time.LocalDateTime;

public class CardResponseDto {

    private final Long id;
    private final String name;
    private final String company;
    private final String position;
    private final String email;
    private final String phone;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CardResponseDto(Long id, String name, String company, String position, String email, String phone,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static CardResponseDto fromEntity (Card card) {
        return new CardResponseDto(
                card.getId(),
                card.getName(),
                card.getCompany(),
                card.getPosition(),
                card.getEmail(),
                card.getPhone(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}
