package com.project.usedplatform.favorite;

import com.project.usedplatform.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    // 찜 상태를 토글: 이미 찜되어 있으면 삭제, 아니면 저장 후 true 반환
    public boolean toggleFavorite(Member member, Long productId) {
        Optional<Favorite> favOpt = favoriteRepository.findByMemberAndProductId(member, productId);
        if (favOpt.isPresent()) {
            favoriteRepository.delete(favOpt.get());
            return false;
        } else {
            favoriteRepository.save(new Favorite(member, productId));
            return true;
        }
    }

    // Member 객체를 기준으로 찜한 상품 목록 조회
    public List<Favorite> getFavoritesByMember(Member member) {
        return favoriteRepository.findByMember(member);
    }
}