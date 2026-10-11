package com.example.membersystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 會員資料庫實體 (Entity)
 * 透過 JPA 註解，讓 Spring 自動對應並產生資料庫的 members 資料表
 */
@Entity
@Table(name = "members")
public class Member {

    // 主鍵 ID，由資料庫自動遞增 (1, 2, 3...)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 會員帳號：不允許為空 (nullable = false)，且必須全系統唯一 (unique = true)
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // 會員密碼：存放 BCrypt 加密後的字串，不允許為空
    @Column(nullable = false, length = 100)
    private String password;

    // 會員姓名：不允許為空
    @Column(nullable = false, length = 50)
    private String fullName;

    // 建立時間 (新增後不可修改 updatable = false)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 最後更新時間
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // JPA 規定的無參數建構子 (No-Args Constructor)
    public Member() {
    }

    // 方便建立新會員的建構子
    public Member(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    // 在寫入資料庫之前，自動填入當前時間
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 在更新資料庫之前，自動更新為當前時間
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getter 與 Setter (Java 存取物件屬性的標準方法)
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
