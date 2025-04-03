package com.project.usedplatform.config;

import com.project.usedplatform.security.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 정적 리소스와 회원 관련 URL (회원가입, 로그인, 중복 체크 API 등) 은 인증 없이 접근 가능
                        .requestMatchers("/uploads/**", "/images/**", "/css/**", "/error",
                                "/member/signup", "/member/login", "/member/checkUsername", "/member/checkEmail")
                        .permitAll()
                        // 메인 페이지도 인증 없이 접근
                        .requestMatchers("/", "/member/login", "/member/signup")
                        .permitAll()
                        // 그 외의 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/member/login")
                        .loginProcessingUrl("/member/login/process")
                        .defaultSuccessUrl("/")   // 로그인 성공 후 항상 "/"로 이동
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/member/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}