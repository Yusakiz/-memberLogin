package com.example.membersystem.config;

import com.example.membersystem.entity.Member;
import com.example.membersystem.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
        // 如果資料庫內尚無資料，自動建立預設測試帳號，方便面試官開箱即測
        if (memberRepository.count() == 0) {
            Member member1 = new Member();
            member1.setUsername("test01");
            member1.setPassword(passwordEncoder.encode("123456"));
            member1.setFullName("測試會員小王");
            member1.setEmail("wang@example.com");
            member1.setPhone("0912-345-678");

            Member member2 = new Member();
            member2.setUsername("alice");
            member2.setPassword(passwordEncoder.encode("123456"));
            member2.setFullName("愛麗絲");
            member2.setEmail("alice@example.com");
            member2.setPhone("0922-111-222");

            Member member3 = new Member();
            member3.setUsername("bob");
            member3.setPassword(passwordEncoder.encode("123456"));
            member3.setFullName("鮑伯陳");
            member3.setEmail("bob@example.com");
            member3.setPhone("0933-444-555");

            memberRepository.save(member1);
            memberRepository.save(member2);
            memberRepository.save(member3);

            System.out.println(">>> [初始化完成] 預設測試帳號已建立: test01 / 123456, alice / 123456, bob / 123456");
        }
    }
}
