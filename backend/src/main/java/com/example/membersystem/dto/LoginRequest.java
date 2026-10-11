package com.example.membersystem.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "請輸入會員帳號")
    private String username;

    @NotBlank(message = "請輸入會員密碼")
    private String password;

    public LoginRequest() {}

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
}
