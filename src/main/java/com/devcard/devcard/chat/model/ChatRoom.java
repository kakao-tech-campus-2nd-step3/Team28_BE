package com.devcard.devcard.chat.model;

import com.devcard.devcard.auth.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(name = "chat_room_participants")
    private List<Member> participants;
    private LocalDateTime createdAt;
    private String lastMessage = "메세지를 보내보세요."; // 기본값 설정
    private LocalDateTime lastMessageTime;

    public ChatRoom(List<Member> participants, LocalDateTime createdAt) {
        this.participants = participants;
        this.createdAt = createdAt;
    }

    protected ChatRoom() {
    }

    public List<Member> getParticipants() {
        return participants;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    // participants로부터 name을 가져와 리스트화
    public List<String> getParticipantsName() {
        return this.participants.stream().map(Member::getNickname).toList();
    }

    // participants로부터 id를 가져와 리스트화
    public List<Long> getParticipantsId() {
        return this.participants.stream().map(Member::getId).toList();
    }

    // lastMessage와 LastMessageTime 업데이트
    public void updateLastMessageAndLastMessageTime(String newLastMessage, LocalDateTime newLastMessageTime) {
        this.lastMessage = newLastMessage;
        this.lastMessageTime = newLastMessageTime;
    }
}
