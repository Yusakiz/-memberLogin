package com.example.membersystem.controller;

import com.example.membersystem.dto.*;
import com.example.membersystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 1. 會員註冊
    @PostMapping("/register")
    public ApiResponse<MemberResponse> register(@Valid @RequestBody RegisterRequest req) {
        MemberResponse response = memberService.register(req);
        return ApiResponse.ok("註冊成功！請使用新帳號登入", response);
    }

    // 2. 會員登入
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        LoginResponse response = memberService.login(req);
        return ApiResponse.ok("登入成功", response);
    }
}
