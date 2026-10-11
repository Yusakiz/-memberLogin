package com.example.membersystem.dto;

import com.example.membersystem.entity.Member;
import java.time.LocalDateTime;

/**
 * 回傳給前端展示的會員資訊
 * 注意：這裡【故意不包含 password 欄位】，確保密碼永遠不會回傳給前端！
 */
public class MemberResponse {
    private Long id;
    private String username;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MemberResponse() {}

    // 靜態工廠方法：將資料庫 Entity 轉換為安全的 Response DTO
    public static MemberResponse fromEntity(Member member) {
        MemberResponse res = new MemberResponse();
        res.setId(member.getId());
        res.setUsername(member.getUsername());
        res.setFullName(member.getFullName());
        res.setCreatedAt(member.getCreatedAt());
        res.setUpdatedAt(member.getUpdatedAt());
        return res;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
