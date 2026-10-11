package com.example.membersystem.controller;

import com.example.membersystem.dto.ApiResponse;
import com.example.membersystem.dto.MemberResponse;
import com.example.membersystem.dto.UpdateRequest;
import com.example.membersystem.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 取得當前登入者資訊 (進入歡迎頁時呼叫)
    @GetMapping("/me")
    public ApiResponse<MemberResponse> getCurrentMember(HttpServletRequest request) {
        Long currentMemberId = (Long) request.getAttribute("currentMemberId");
        MemberResponse member = memberService.getMemberById(currentMemberId);
        return ApiResponse.ok(member);
    }

    // 3. 修改會員帳號資料 (需登入且只能改自己)
    @PutMapping("/{id}")
    public ApiResponse<MemberResponse> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRequest req,
            HttpServletRequest request) {
        Long currentMemberId = (Long) request.getAttribute("currentMemberId");
        MemberResponse updated = memberService.updateMember(currentMemberId, id, req);
        return ApiResponse.ok("會員資料修改成功！", updated);
    }

    // 4. 查詢功能 (藉由會員姓名或會員帳號搜尋他人會員資訊)
    @GetMapping("/search")
    public ApiResponse<List<MemberResponse>> searchMembers(
            @RequestParam(required = false, defaultValue = "") String keyword) {
        List<MemberResponse> results = memberService.searchMembers(keyword);
        return ApiResponse.ok(results);
    }

    // 5. 刪除會員帳號資訊 (需登入且只能刪自己)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMember(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long currentMemberId = (Long) request.getAttribute("currentMemberId");
        memberService.deleteMember(currentMemberId, id);
        return ApiResponse.ok("會員帳號已成功刪除！", null);
    }
}
