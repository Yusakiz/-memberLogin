package com.example.membersystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "會員帳號不得為空")
    @Size(min = 3, max = 30, message = "帳號長度需介於 3 到 30 個字元")
    private String username;

    @NotBlank(message = "會員密碼不得為空")
    @Size(min = 4, max = 50, message = "密碼長度需至少 4 個字元")
    private String password;

    @NotBlank(message = "會員姓名不得為空")
    @Size(max = 50, message = "姓名長度不可超過 50 個字元")
    private String fullName;

    @Email(message = "電子信箱格式不正確")
    private String email;

    private String phone;

    public RegisterRequest() {}

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
}
