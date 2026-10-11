package com.example.membersystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 註冊請求接收物件 (Register Request)
 * 透過 Bean Validation 註解做第一道防呆檢核
 */
public class RegisterRequest {

    @NotBlank(message = "會員帳號不得為空")
    @Size(min = 3, max = 30, message = "帳號長度需介於 3 到 30 個字元")
    private String username;

    @NotBlank(message = "會員密碼不得為空")
    @Size(min = 4, max = 50, message = "密碼長度需至少 4 個字元")
    @Pattern(regexp = "^[A-Za-z0-9_!@#$%^&*()+\\-=\\[\\]{};':\",.<>/?]+$", message = "密碼僅能使用英文字母、數字與常用符號，不可輸入中文")
    private String password;

    @NotBlank(message = "會員姓名不得為空")
    @Size(max = 50, message = "姓名長度不可超過 50 個字元")
    private String fullName;

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
}
