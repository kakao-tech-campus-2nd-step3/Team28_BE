package com.devcard.devcard.chat.repository;

import com.devcard.devcard.auth.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<Member, Long> {

    List<Member> findByIdIn(List<Long> participantsId);
}
