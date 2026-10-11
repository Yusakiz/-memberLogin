package com.example.membersystem.repository;

import com.example.membersystem.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    // 依會員姓名或帳號模糊搜尋 (忽略大小寫)
    List<Member> findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(String username, String fullName);
}
