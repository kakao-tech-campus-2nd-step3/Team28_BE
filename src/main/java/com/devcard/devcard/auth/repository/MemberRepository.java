package com.devcard.devcard.auth.repository;

import com.devcard.devcard.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByGithubId(String githubId);
}
