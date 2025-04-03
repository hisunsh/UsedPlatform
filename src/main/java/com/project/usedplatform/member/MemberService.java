package com.project.usedplatform.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member registerMember(Member member) {
        Optional<Member> existingByEmail = memberRepository.findByEmail(member.getEmail());
        if(existingByEmail.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        Optional<Member> existingByUsername = memberRepository.findByUsername(member.getUsername());
        if(existingByUsername.isPresent()){
            throw new IllegalArgumentException("이미 등록된 아이디입니다.");
        }
        // (비밀번호 암호화 등 추가 처리 가능)
        return memberRepository.save(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }
}