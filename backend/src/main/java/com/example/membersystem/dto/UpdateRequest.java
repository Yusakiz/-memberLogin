package com.example.membersystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateRequest {

    @NotBlank(message = "會員姓名不得為空")
    @Size(max = 50, message = "姓名長度不可超過 50 個字元")
    private String fullName;

    @Email(message = "電子信箱格式不正確")
    private String email;

    private String phone;

    // Optional: if provided, update password
    private String password;

    public UpdateRequest() {}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
