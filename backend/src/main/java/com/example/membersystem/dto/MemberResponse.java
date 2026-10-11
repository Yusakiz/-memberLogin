package com.example.membersystem.dto;

import com.example.membersystem.entity.Member;
import java.time.LocalDateTime;

public class MemberResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MemberResponse() {}

    public static MemberResponse fromEntity(Member member) {
        MemberResponse res = new MemberResponse();
        res.setId(member.getId());
        res.setUsername(member.getUsername());
        res.setFullName(member.getFullName());
        res.setEmail(member.getEmail());
        res.setPhone(member.getPhone());
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
