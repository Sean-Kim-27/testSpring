package com.example.test.repository;

import com.example.test.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 로그인 할 때 아이디로 회원을 찾기 위해 필요함
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
}
