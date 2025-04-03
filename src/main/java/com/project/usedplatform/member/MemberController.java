package com.project.usedplatform.member;

import com.project.usedplatform.chat.chatroom.ChatRoom;
import com.project.usedplatform.chat.chatroom.ChatRoomService;
import com.project.usedplatform.favorite.Favorite;
import com.project.usedplatform.favorite.FavoriteService;
import com.project.usedplatform.product.Product;
import com.project.usedplatform.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ChatRoomService chatRoomService;

    // 로그인 폼 (GET)
    @GetMapping("/login")
    public String loginForm() {
        return "member/memberLogin";  // 템플릿: src/main/resources/templates/member/memberLogin.html
    }

    // 회원가입 폼 (GET)
    @GetMapping("/signup")
    public String signupForm() {
        return "member/memberSignUp"; // 템플릿: src/main/resources/templates/member/memberSignUp.html
    }

    // 회원가입 처리 (POST)
    @PostMapping("/signup")
    public String signup(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("birthDate") String birthDate,
            @RequestParam("gender") String gender
    ) {
        System.out.println("username = " + username);
        System.out.println("email = " + email);
        System.out.println("password = " + password);
        System.out.println("name = " + name);
        System.out.println("birthDate = " + birthDate);
        System.out.println("gender = " + gender);

        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        member.setName(name);
        member.setBirthDate(LocalDate.parse(birthDate)); // ISO 형식 (YYYY-MM-DD)
        member.setGender(gender);

        memberService.registerMember(member);
        return "redirect:/member/login";
    }

    // 마이페이지 (GET) - 모든 정보를 통합
    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {
        Member member = memberService.findByUsername(principal.getName());
        model.addAttribute("member", member);

        // 내가 등록한 상품 조회
        List<Product> myProducts = productService.getProductsBySeller(member.getUsername());
        model.addAttribute("myProducts", myProducts);

        // 내가 찜한 상품 조회
        List<Favorite> favorites = favoriteService.getFavoritesByMember(member);
        List<Product> favoriteProducts = productService.getProductsByIds(
                favorites.stream().map(Favorite::getProductId).collect(Collectors.toList())
        );
        model.addAttribute("favoriteProducts", favoriteProducts);

        // 현재 사용자와 관련된 채팅방 목록 조회
        List<ChatRoom> chatRooms = chatRoomService.getChatRoomsForUser(member.getUsername());
        model.addAttribute("chatRooms", chatRooms);

        return "member/myPage"; // 템플릿: src/main/resources/templates/member/myPage.html
    }

    // 아이디 중복 체크 (GET)
    @GetMapping("/checkUsername")
    @ResponseBody
    public Map<String, Boolean> checkUsername(@RequestParam String username) {
        boolean available = (memberService.findByUsername(username) == null);
        return Collections.singletonMap("available", available);
    }

    // 이메일 중복 체크 (GET)
    @GetMapping("/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        boolean available = (memberService.findByEmail(email) == null);
        return Collections.singletonMap("available", available);
    }

    // 채팅방 나가기
    @PostMapping("/chatroom/leave")
    public String leaveChatRoom(@RequestParam("roomId") Long roomId, Principal principal) {
        // 추가 검증: 해당 채팅방이 현재 사용자와 관련 있는지 확인 (생략)
        chatRoomService.leaveChatRoom(roomId);
        return "redirect:/member/myPage";
    }
}