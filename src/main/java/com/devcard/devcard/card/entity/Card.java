package com.devcard.devcard.card.entity;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.dto.CardRequestDto;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToMany(mappedBy = "cards")
    private List<Group> groups = new ArrayList<>();

    private String company;
    private String position;
    private String phone;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 기본 생성자 (JPA 요구 사항)
    protected Card() {
    }

    // 빌더 패턴을 위한 생성자
    private Card(Builder builder) {
        this.member = builder.member;
        this.company = builder.company;
        this.position = builder.position;
        this.phone = builder.phone;
        this.bio = builder.bio;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static class Builder {
        private final Member member;
        private String company;
        private String position;
        private String phone;
        private String bio;

        public Builder(Member member) {
            this.member = member;
        }

        public Builder company(String company) {
            this.company = company;
            return this;
        }

        public Builder position(String position) {
            this.position = position;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
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

    // Getter
    public Long getId() { return id; }
    public Member getMember() { return member; }
    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public String getPhone() { return phone; }
    public String getBio() { return bio; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<Group> getGroups() { return groups; }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    // DTO 기반 업데이트 메서드
    public void updateFromDto(CardRequestDto dto) {
        if (dto.getCompany() != null) this.company = dto.getCompany();
        if (dto.getPosition() != null) this.position = dto.getPosition();
        if (dto.getPhone() != null) this.phone = dto.getPhone();
        if (dto.getBio() != null) this.bio = dto.getBio();
        this.updatedAt = LocalDateTime.now();
    }
}
