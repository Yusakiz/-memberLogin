package com.example.membersystem.controller;

import com.example.membersystem.dto.*;
import com.example.membersystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 認證控制器 (AuthController)
 * 負責「註冊」與「登入」兩大公開 API
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;

    // 透過建構子注入 MemberService
    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 1. 會員註冊 API
    // 網址：POST http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ApiResponse<MemberResponse> register(@Valid @RequestBody RegisterRequest req) {
        // 呼叫 Service 執行註冊邏輯
        MemberResponse response = memberService.register(req);
        // 回傳標準 JSON 包裝盒
        return ApiResponse.ok("註冊成功！請使用新帳號登入", response);
    }

    // 2. 會員登入 API
    // 網址：POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        // 呼叫 Service 執行登入邏輯 (比對密碼、簽發 Token)
        LoginResponse response = memberService.login(req);
        // 回傳標準 JSON 包裝盒 (內含 Token 與會員資料)
        return ApiResponse.ok("登入成功", response);
    }
}
