package com.example.test.controller;

import com.example.test.entity.Member;
import com.example.test.repository.MemberRepository;
import com.example.test.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 1. 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody Map<String, String> params) {
        Member member = Member.builder()
                .username(params.get("username"))
                .password(passwordEncoder.encode(params.get("password"))) // 암호화 필수!
                .nickname(params.get("nickname"))
                .build();
        memberRepository.save(member);
        return "회원가입 성공! 이제 로그인해라.";
    }

    // 2. 로그인 (성공하면 토큰 줌)
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> params) {
        Member member = memberRepository.findByUsername(params.get("username"))
                .orElseThrow(() -> new RuntimeException("없는 아이디다."));

        if (!passwordEncoder.matches(params.get("password"), member.getPassword())) {
            throw new RuntimeException("비밀번호 틀렸다.");
        }

        // 출입증 발급!
        return jwtUtil.generateToken(member.getUsername());
    }
}