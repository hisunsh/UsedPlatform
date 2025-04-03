package com.project.usedplatform.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 상품 등록
    public Product registerProduct(Product product) {
        return productRepository.save(product);
    }

    // 전체 상품 목록 조회 (페이지네이션 적용)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // 특정 상품 조회 (상세보기)
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // 상품 수정
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    // 상품 삭제
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // 키워드 검색 (페이지네이션 적용)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByTitleContaining(keyword, pageable);
    }

    // 판매자 기준 상품 조회 (판매자 username 기준)
    public List<Product> getProductsBySeller(String sellerUsername) {
        return productRepository.findAll().stream()
                .filter(prod -> prod.getSeller() != null && prod.getSeller().equals(sellerUsername))
                .toList();
    }

    // id 리스트로 상품 조회 (예: 찜한 상품 목록)
    public List<Product> getProductsByIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }
}