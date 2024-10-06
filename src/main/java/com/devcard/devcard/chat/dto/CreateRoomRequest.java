package com.devcard.devcard.chat.dto;

import java.util.List;

public class CreateRoomRequest {

    private List<Long> participantsId;

    public List<Long> getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(List<Long> participantsId) {
        this.participantsId = participantsId;
    }
}
