package com.devcard.devcard.chat.repository;

import com.devcard.devcard.chat.model.ChatUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    List<ChatUser> findByIdIn(List<Long> participantsId);
}
