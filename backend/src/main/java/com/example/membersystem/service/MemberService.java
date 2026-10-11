package com.example.membersystem.service;

import com.example.membersystem.dto.*;
import com.example.membersystem.entity.Member;
import com.example.membersystem.repository.MemberRepository;
import com.example.membersystem.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 會員商業邏輯層 (Service)
 * 負責處理各項業務規則：密碼加密、重複檢查、身分權限比對
 */
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 建構子依賴注入 (Constructor Injection)
    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 1. 會員註冊功能 (條件 1)
    @Transactional
    public MemberResponse register(RegisterRequest req) {
        // 業務規則 1：檢查帳號是否已存在
        if (memberRepository.existsByUsername(req.getUsername().trim())) {
            throw new IllegalArgumentException("此會員帳號已被使用，請更換其他帳號");
        }

        // 業務規則 2：密碼不可明文入庫，使用 BCrypt 雜湊加密
        String encodedPassword = passwordEncoder.encode(req.getPassword());

        Member member = new Member(
                req.getUsername().trim(),
                encodedPassword,
                req.getFullName().trim()
        );

        Member saved = memberRepository.save(member);
        return MemberResponse.fromEntity(saved);
    }

    // 2. 會員登入功能 (條件 2)
    public LoginResponse login(LoginRequest req) {
        // 依帳號尋找會員
        Member member = memberRepository.findByUsername(req.getUsername().trim())
                .orElseThrow(() -> new IllegalArgumentException("帳號或密碼錯誤"));

        // 比對密碼 (比對使用者輸入的明文與資料庫的 BCrypt 雜湊值)
        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("帳號或密碼錯誤");
        }

        // 登入成功，簽發 JWT Token
        String token = jwtUtil.generateToken(member.getId(), member.getUsername());
        return new LoginResponse(token, MemberResponse.fromEntity(member));
    }

    // 取得指定會員資料
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("找不到該會員資料"));
        return MemberResponse.fromEntity(member);
    }

    // 3. 修改會員資料 (條件 3 & 條件 6: 需登入且只能改自己)
    @Transactional
    public MemberResponse updateMember(Long currentMemberId, Long targetId, UpdateRequest req) {
        // 安全檢查：防範水平越權 (IDOR)
        if (!currentMemberId.equals(targetId)) {
            throw new IllegalStateException("您無權限修改其他會員的帳號資料");
        }

        Member member = memberRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("會員不存在"));

        member.setFullName(req.getFullName().trim());

        // 若使用者有輸入新密碼，則重新加密後更新
        if (StringUtils.hasText(req.getPassword())) {
            member.setPassword(passwordEncoder.encode(req.getPassword().trim()));
        }

        Member updated = memberRepository.save(member);
        return MemberResponse.fromEntity(updated);
    }

    // 4. 查詢會員功能 (條件 4: 依姓名或帳號搜尋他人資料)
    public List<MemberResponse> searchMembers(String keyword) {
        List<Member> list;
        if (!StringUtils.hasText(keyword)) {
            // 若沒有輸入關鍵字，回傳全部會員
            list = memberRepository.findAll();
        } else {
            // 依姓名或帳號進行模糊搜尋
            String trimmed = keyword.trim();
            list = memberRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(trimmed, trimmed);
        }

        return list.stream()
                .map(MemberResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 5. 刪除會員功能 (條件 5 & 條件 6: 需登入且只能刪自己)
    @Transactional
    public void deleteMember(Long currentMemberId, Long targetId) {
        // 安全檢查：防範水平越權
        if (!currentMemberId.equals(targetId)) {
            throw new IllegalStateException("您無權限刪除其他會員的帳號");
        }

        if (!memberRepository.existsById(targetId)) {
            throw new IllegalArgumentException("會員不存在或已被刪除");
        }

        memberRepository.deleteById(targetId);
    }
}
