package com.devcard.devcard.chat.dto;

public class SendingMessageRequest {

    private Long chatId;
    private String message;

    public Long getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
