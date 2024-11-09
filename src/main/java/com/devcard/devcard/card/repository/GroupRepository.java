package com.devcard.devcard.card.repository;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>{
    List<Group> findByMember(Member member);
    Optional<Group> findByIdAndMember(Long id, Member member);
}
