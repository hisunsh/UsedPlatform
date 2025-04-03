package com.project.usedplatform.favorite;

import com.project.usedplatform.member.Member;
import com.project.usedplatform.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private MemberService memberService;

    // AJAX 요청: 찜 토글
    @PostMapping("/toggle")
    public ToggleResponse toggleFavorite(@RequestParam("productId") Long productId, Principal principal) {
        // principal.getName()는 MemberDetailsService에서 username을 반환하도록 구성되어 있음
        Member member = memberService.findByUsername(principal.getName());
        boolean favorited = favoriteService.toggleFavorite(member, productId);
        return new ToggleResponse(favorited);
    }

    public static class ToggleResponse {
        private boolean favorited;
        public ToggleResponse(boolean favorited) {
            this.favorited = favorited;
        }
        public boolean isFavorited() {
            return favorited;
        }
        public void setFavorited(boolean favorited) {
            this.favorited = favorited;
        }
    }
}