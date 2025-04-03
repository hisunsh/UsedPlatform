package com.project.usedplatform.product;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private double price;

    // 판매자 정보를 저장 (회원의 username)
    private String seller;

    private String image; // 이미지 파일명

    public Product() {}

    public Product(String title, String description, double price, String seller, String image) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.seller = seller;
        this.image = image;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSeller() { return seller; }
    public void setSeller(String seller) { this.seller = seller; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}