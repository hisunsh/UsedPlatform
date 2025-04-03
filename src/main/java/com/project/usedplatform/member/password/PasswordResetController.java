package com.project.usedplatform.member.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    // 비밀번호 찾기 폼 (GET)
    @GetMapping("/forgotPassword")
    public String forgotPasswordForm() {
        return "member/forgotPassword";  // 템플릿 파일: src/main/resources/templates/member/forgotPassword.html
    }

    // 비밀번호 찾기 폼 제출 (POST) - 재설정 링크 전송
    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        try {
            passwordResetService.createPasswordResetTokenForEmail(email);
            model.addAttribute("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다. (로컬에서는 콘솔 로그를 확인하세요)");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "member/forgotPassword";
    }

    // 비밀번호 재설정 폼 (GET) - 토큰 포함 링크로 접근
    @GetMapping("/resetPassword")
    public String resetPasswordForm(@RequestParam("token") String token, Model model) {
        if (!passwordResetService.validatePasswordResetToken(token)) {
            model.addAttribute("error", "유효하지 않거나 만료된 토큰입니다.");
            return "member/resetPassword";
        }
        model.addAttribute("token", token);
        return "member/resetPassword"; // 템플릿 파일: src/main/resources/templates/member/resetPassword.html
    }

    // 비밀번호 재설정 폼 제출 (POST)
    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("token", token);
            return "member/resetPassword";
        }
        try {
            passwordResetService.resetPassword(token, newPassword);
            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다. 로그인 해주세요.");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "member/resetPassword";
    }
}