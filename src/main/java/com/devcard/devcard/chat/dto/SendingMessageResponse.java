package com.devcard.devcard.chat.dto;

import java.time.LocalDateTime;

public class SendingMessageResponse {

    private String message_id;
    private LocalDateTime send_at;

    public SendingMessageResponse(Long message_id, LocalDateTime sendAt) {
        this.message_id = "msg_" + message_id;
        this.send_at = sendAt;
    }

    public String getMessage() {
        return message_id;
    }

    public LocalDateTime getSendAt() {
        return send_at;
    }

}
