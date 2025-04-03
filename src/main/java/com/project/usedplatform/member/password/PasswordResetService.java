package com.project.usedplatform.member.password;

import com.project.usedplatform.member.Member;
import com.project.usedplatform.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 등록된 이메일에 대해 재설정 토큰 생성 및 "이메일 전송"(여기서는 콘솔 출력)
    public String createPasswordResetTokenForEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // 30분 유효
        PasswordResetToken prt = new PasswordResetToken(token, email, expiryDate);
        tokenRepository.save(prt);
        // 실제 환경에서는 JavaMailSender 등을 사용하여 이메일 발송
        System.out.println("비밀번호 재설정 링크: http://localhost:8080/member/resetPassword?token=" + token);
        return token;
    }

    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken prt = tokenRepository.findByToken(token).orElse(null);
        return prt != null && !prt.isExpired();
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = tokenRepository.findByToken(token).orElse(null);
        if (prt == null || prt.isExpired()) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 토큰입니다.");
        }
        Member member = memberRepository.findByEmail(prt.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        tokenRepository.delete(prt);
    }
}