package com.example.membersystem.config;

import com.example.membersystem.entity.Member;
import com.example.membersystem.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 資料初始化器 (DataInitializer)
 * 系統第一次啟動時，若資料庫是空的，自動注入 3 筆測試帳號
 * 方便面試官開箱即測，省去手動註冊的時間
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (memberRepository.count() == 0) {
            String defaultPwd = passwordEncoder.encode("123456");

            Member m1 = new Member("test01", defaultPwd, "測試會員小王");
            Member m2 = new Member("alice", defaultPwd, "愛麗絲");
            Member m3 = new Member("bob", defaultPwd, "鮑伯陳");

            memberRepository.save(m1);
            memberRepository.save(m2);
            memberRepository.save(m3);

            System.out.println(">>> [初始化完成] 預設測試帳號已注入: test01 / 123456, alice / 123456, bob / 123456");
        }
    }
}
