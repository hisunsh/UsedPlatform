package com.project.usedplatform.favorite;

import com.project.usedplatform.member.Member;
import jakarta.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne 관계로 Member와 연결 (member_id 컬럼)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // DB 스키마와의 호환을 위해 member_username 컬럼도 추가 (nullable=false)
    @Column(name = "member_username", nullable = false)
    private String memberUsername;

    // 찜한 상품의 id
    @Column(nullable = false)
    private Long productId;

    public Favorite() {}

    public Favorite(Member member, Long productId) {
        this.member = member;
        this.memberUsername = member.getUsername();
        this.productId = productId;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;
        if(member != null) {
            this.memberUsername = member.getUsername();
        }
    }
    public String getMemberUsername() {
        return memberUsername;
    }
    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
}