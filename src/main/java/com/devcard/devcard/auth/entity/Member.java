package com.devcard.devcard.auth.entity;

import com.devcard.devcard.auth.dto.MemberRequestDto;
import com.devcard.devcard.card.entity.Card;
import com.devcard.devcard.card.entity.Group;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String githubId;
    private String email;
    private String profileImg;
    private String username;
    private String nickname;
    private String role;

    @CreationTimestamp
    private Timestamp createDate;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Card card;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Group> groups = new ArrayList<>();

    public Member() {
    }

    public Member(String githubId, String email, String profileImg, String username, String nickname, String role, Timestamp createDate) {
        this.githubId = githubId;
        this.email = email;
        this.profileImg = profileImg;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
        this.createDate = createDate;
    }

    public void updateFromAttributes(Map<String, Object> attributes) {
        this.email = (String) attributes.get("email");
        this.profileImg = (String) attributes.get("avatar_url");
        this.username = (String) attributes.get("name");
        this.nickname = (String) attributes.get("login");
    }

    public void updateFromDto(MemberRequestDto dto) {
        if (dto.getEmail() != null) this.email = dto.getEmail();
        if (dto.getNickname() != null) this.nickname = dto.getNickname();
        if (dto.getProfileImg() != null) this.profileImg = dto.getProfileImg();
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

    public String getRole() {
        return role;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public List<Group> getGroups() {
        return groups;
    }
}

