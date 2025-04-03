package com.project.usedplatform.favorite;

import com.project.usedplatform.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByMemberAndProductId(Member member, Long productId);
    List<Favorite> findByMember(Member member);
}