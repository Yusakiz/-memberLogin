package com.example.membersystem.repository;

import com.example.membersystem.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 會員資料庫存取介面 (Repository)
 * 繼承 JpaRepository<Member, Long>：
 * 第一個泛型是操作的實體類別 (Member)
 * 第二個泛型是主鍵型態 (Long)
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 1. 檢查帳號是否已被註冊 (註冊防呆用)
    // Spring Data JPA 會自動轉成 SQL: SELECT count(*) > 0 FROM members WHERE username = ?
    boolean existsByUsername(String username);

    // 2. 依帳號尋找會員 (登入比對密碼用)
    // 回傳 Optional<Member> 可以優雅地避免 NullPointerException
    // SQL: SELECT * FROM members WHERE username = ?
    Optional<Member> findByUsername(String username);

    // 3. 依姓名或帳號進行模糊搜尋，且忽略大小寫 (條件 4: 查詢功能)
    // Spring Data JPA 會自動轉成 SQL:
    // SELECT * FROM members WHERE LOWER(username) LIKE %?% OR LOWER(full_name) LIKE %?%
    List<Member> findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(String username, String fullName);
}
