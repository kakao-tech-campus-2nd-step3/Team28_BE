package com.devcard.devcard.chat.model;

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
    private List<ChatUser> participants;
    private LocalDateTime createdAt;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public ChatRoom(List<ChatUser> participants, LocalDateTime createdAt) {
        this.participants = participants;
        this.createdAt = createdAt;
    }

    protected ChatRoom() {

    }

    public List<ChatUser> getParticipants() {
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
        return this.participants.stream().map(ChatUser::getName).toList();
    }
}
