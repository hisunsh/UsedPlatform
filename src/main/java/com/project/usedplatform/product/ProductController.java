package com.project.usedplatform.product;

import com.project.usedplatform.member.Member;
import com.project.usedplatform.member.MemberService;
import com.project.usedplatform.favorite.FavoriteService;
import com.project.usedplatform.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FileStorageService fileStorageService;

    // 상품 등록 폼
    @GetMapping("/register")
    public String productRegisterForm() {
        return "product/productRegistration";
    }

    // 상품 등록 처리
    @PostMapping("/register")
    public String productRegister(Product product,
                                  @RequestParam("file") MultipartFile imageFile,
                                  Principal principal) {
        if (!imageFile.isEmpty()) {
            try {
                String newFileName = fileStorageService.storeFile(imageFile);
                product.setImage(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Member member = memberService.findByUsername(principal.getName());
        product.setSeller(member.getUsername());
        productService.registerProduct(product);
        return "redirect:/product/list";
    }

    // 상품 목록 조회 (페이지네이션 적용, 한 페이지당 6개)
    @GetMapping("/list")
    public String productList(Model model,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "0") int page,
                              Principal principal) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<Product> productPage;
        if (keyword != null && !keyword.isEmpty()) {
            productPage = productService.searchProducts(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            productPage = productService.getAllProducts(pageable);
        }
        model.addAttribute("productPage", productPage);

        if (principal != null) {
            Member member = memberService.findByUsername(principal.getName());
            List<Long> favoriteProductIds = favoriteService.getFavoritesByMember(member)
                    .stream()
                    .map(fav -> fav.getProductId())
                    .collect(Collectors.toList());
            model.addAttribute("favoriteProductIds", favoriteProductIds);
        }
        return "product/productList";
    }

    // 상품 상세 조회
    @GetMapping("/detail/{id}")
    public String productDetail(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        String currentUsername = principal != null ? principal.getName() : "";
        model.addAttribute("currentUsername", currentUsername);
        return "product/productDetail";
    }

    // 상품 수정 폼
    @GetMapping("/edit/{id}")
    public String productEditForm(@PathVariable Long id, Model model, Principal principal) {
        Product existingProduct = productService.getProductById(id);
        Member member = memberService.findByUsername(principal.getName());
        if (existingProduct == null || !existingProduct.getSeller().equals(member.getUsername())) {
            model.addAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/product/detail/" + id;
        }
        model.addAttribute("product", existingProduct);
        return "product/productEdit";
    }

    // 상품 수정 처리 (파일 업로드 처리 포함)
    @PostMapping("/edit/{id}")
    public String productEdit(@PathVariable Long id,
                              Product updatedProduct,
                              @RequestParam("file") MultipartFile imageFile,
                              Principal principal,
                              Model model) {
        Product existingProduct = productService.getProductById(id);
        Member member = memberService.findByUsername(principal.getName());
        if (existingProduct == null || !existingProduct.getSeller().equals(member.getUsername())) {
            model.addAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/product/detail/" + id;
        }
        try {
            if (!imageFile.isEmpty()) {
                String newFileName = fileStorageService.storeFile(imageFile);
                updatedProduct.setImage(newFileName);
            } else {
                updatedProduct.setImage(existingProduct.getImage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "파일 업로드 실패: " + e.getMessage());
            return "redirect:/product/edit/" + id;
        }
        updatedProduct.setId(id);
        updatedProduct.setSeller(existingProduct.getSeller());
        productService.updateProduct(updatedProduct);
        return "redirect:/product/detail/" + id;
    }

    // 상품 삭제 처리
    @PostMapping("/delete/{id}")
    public String productDelete(@PathVariable Long id, Principal principal, Model model) {
        Product existingProduct = productService.getProductById(id);
        Member member = memberService.findByUsername(principal.getName());
        if (existingProduct == null || !existingProduct.getSeller().equals(member.getUsername())) {
            model.addAttribute("error", "삭제 권한이 없습니다.");
            return "redirect:/product/detail/" + id;
        }
        productService.deleteProduct(id);
        return "redirect:/product/list";
    }

    // 찜 토글 (AJAX)
    @PostMapping("/favorite/toggle")
    @ResponseBody
    public FavoriteToggleResponse toggleFavorite(@RequestParam("productId") Long productId, Principal principal) {
        Member member = memberService.findByUsername(principal.getName());
        boolean favorited = favoriteService.toggleFavorite(member, productId);
        return new FavoriteToggleResponse(favorited);
    }

    public static class FavoriteToggleResponse {
        private boolean favorited;
        public FavoriteToggleResponse(boolean favorited) {
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