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

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 1. 註冊功能
    @Transactional
    public MemberResponse register(RegisterRequest req) {
        if (memberRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("此會員帳號已被使用，請更換其他帳號");
        }

        Member member = new Member();
        member.setUsername(req.getUsername().trim());
        // 密碼加密儲存 (BCrypt)
        member.setPassword(passwordEncoder.encode(req.getPassword()));
        member.setFullName(req.getFullName().trim());
        member.setEmail(req.getEmail() != null ? req.getEmail().trim() : null);
        member.setPhone(req.getPhone() != null ? req.getPhone().trim() : null);

        Member saved = memberRepository.save(member);
        return MemberResponse.fromEntity(saved);
    }

    // 2. 登入功能
    public LoginResponse login(LoginRequest req) {
        Member member = memberRepository.findByUsername(req.getUsername().trim())
                .orElseThrow(() -> new IllegalArgumentException("帳號或密碼錯誤"));

        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("帳號或密碼錯誤");
        }

        String token = jwtUtil.generateToken(member.getId(), member.getUsername());
        return new LoginResponse(token, MemberResponse.fromEntity(member));
    }

    // 取得指定會員資料
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("找不到該會員資料"));
        return MemberResponse.fromEntity(member);
    }

    // 3. 修改個人資料 (需驗證只能改自己的)
    @Transactional
    public MemberResponse updateMember(Long currentMemberId, Long targetId, UpdateRequest req) {
        if (!currentMemberId.equals(targetId)) {
            throw new IllegalStateException("您無權限修改其他會員的帳號資料");
        }

        Member member = memberRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("會員不存在"));

        member.setFullName(req.getFullName().trim());
        member.setEmail(req.getEmail() != null ? req.getEmail().trim() : null);
        member.setPhone(req.getPhone() != null ? req.getPhone().trim() : null);

        // 若有填寫新密碼，則重新加密更新
        if (StringUtils.hasText(req.getPassword())) {
            member.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        Member updated = memberRepository.save(member);
        return MemberResponse.fromEntity(updated);
    }

    // 4. 查詢功能 (依姓名或帳號搜尋他人資料)
    public List<MemberResponse> searchMembers(String keyword) {
        List<Member> list;
        if (!StringUtils.hasText(keyword)) {
            list = memberRepository.findAll();
        } else {
            String trimmed = keyword.trim();
            list = memberRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(trimmed, trimmed);
        }

        return list.stream()
                .map(MemberResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 5. 刪除個人帳號 (需驗證只能刪自己的)
    @Transactional
    public void deleteMember(Long currentMemberId, Long targetId) {
        if (!currentMemberId.equals(targetId)) {
            throw new IllegalStateException("您無權限刪除其他會員的帳號");
        }

        if (!memberRepository.existsById(targetId)) {
            throw new IllegalArgumentException("會員不存在或已被刪除");
        }

        memberRepository.deleteById(targetId);
    }
}
