package com.project.usedplatform.member;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 첫 번째: email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // 두 번째: password
    @Column(name = "password", nullable = false)
    private String password;

    // 세 번째: name (회원의 실제 이름)
    @Column(name = "name", nullable = false)
    private String name;

    // 네 번째: birthDate
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    // 다섯 번째: gender
    @Column(name = "gender", nullable = false)
    private String gender;

    // 여섯 번째: username (회원가입 시 입력받은 아이디)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    public Member() {}

    public Member(String email, String password, String name, LocalDate birthDate, String gender, String username) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.username = username;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}