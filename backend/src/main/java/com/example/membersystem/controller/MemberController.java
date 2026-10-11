package com.example.membersystem.controller;

import com.example.membersystem.dto.ApiResponse;
import com.example.membersystem.dto.MemberResponse;
import com.example.membersystem.dto.UpdateRequest;
import com.example.membersystem.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 會員管理控制器 (MemberController)
 * 負責處理會員相關的查詢、修改、刪除
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 取得「當前登入者本人」的資訊 (進入歡迎頁時呼叫)
    // 網址：GET http://localhost:8080/api/members/me
    @GetMapping("/me")
    public ApiResponse<MemberResponse> getCurrentMember(HttpServletRequest request) {
        // 從攔截器解析後的 Request 屬性中，取出當前登入者的 ID
        Long currentMemberId = (Long) request.getAttribute("currentMemberId");
        MemberResponse member = memberService.getMemberById(currentMemberId);
        return ApiResponse.ok(member);
    }

    // 3. 修改會員帳號資料 (條件 3)
    // 網址：PUT http://localhost:8080/api/members/{id}
    @PutMapping("/{id}")
    public ApiResponse<MemberResponse> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRequest req,
            HttpServletRequest request) {
        Long currentMemberId = (Long) request.getAttribute("currentMemberId");
        MemberResponse updated = memberService.updateMember(currentMemberId, id, req);
        return ApiResponse.ok("會員資料修改成功！", updated);
    }

    // 4. 查詢會員資料 (條件 4: 藉由姓名或帳號搜尋他人資訊)
    // 網址：GET http://localhost:8080/api/members/search?keyword=關鍵字
    @GetMapping("/search")
    public ApiResponse<List<MemberResponse>> searchMembers(
            @RequestParam(required = false, defaultValue = "") String keyword) {
        List<MemberResponse> results = memberService.searchMembers(keyword);
        return ApiResponse.ok(results);
    }

    // 5. 刪除會員帳號資訊 (條件 5)
    // 網址：DELETE http://localhost:8080/api/members/{id}
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMember(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long currentMemberId = (Long) request.getAttribute("currentMemberId");
        memberService.deleteMember(currentMemberId, id);
        return ApiResponse.ok("會員帳號已成功刪除！", null);
    }
}
