package org.ll.kakao_login.domain.member.repository;


import org.ll.kakao_login.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByApiKey(String apiKey);
}
