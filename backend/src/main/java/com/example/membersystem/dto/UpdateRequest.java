package com.example.membersystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateRequest {

    @NotBlank(message = "會員姓名不得為空")
    @Size(max = 50, message = "姓名長度不可超過 50 個字元")
    private String fullName;

    // 可選填：若有填寫才更新密碼
    private String password;

    public UpdateRequest() {}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
