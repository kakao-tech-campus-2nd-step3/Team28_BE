package com.devcard.devcard.chat.dto;

public record SendingMessageRequest(
    Long chatId,
    String message,
    String sender
) {

}
