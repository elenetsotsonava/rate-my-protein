package com.ratemyprotein.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "brand_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_brand")
    )
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "flavor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_flavor")
    )
    private Flavor flavor;

    /*
     * The user who submitted this product.
     *
     * This field is nullable because products created by the
     * system or an administrator may not have a submitting user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "submitted_by_user_id",
            foreignKey = @ForeignKey(name = "fk_product_submitted_by")
    )
    private AppUser submittedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "protein_type",
            nullable = false,
            length = 30
    )
    private ProteinType proteinType;

    @Column(length = 2000)
    private String description;

    @Column(name = "protein_per_serving")
    private Integer proteinPerServing;

    private Integer calories;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /*
     * active = true:
     * The product is approved and publicly visible.
     *
     * active = false:
     * The product is pending approval or deactivated.
     */
    @Column(nullable = false)
    private Boolean active = true;

    public Product() {
    }

    public Product(
            Brand brand,
            Flavor flavor,
            String name,
            ProteinType proteinType
    ) {
        this.brand = brand;
        this.flavor = flavor;
        this.name = name;
        this.proteinType = proteinType;
    }

    public Long getId() {
        return id;
    }

    public Brand getBrand() {
        return brand;
    }

    public Flavor getFlavor() {
        return flavor;
    }

    public AppUser getSubmittedBy() {
        return submittedBy;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getName() {
        return name;
    }

    public ProteinType getProteinType() {
        return proteinType;
    }

    public String getDescription() {
        return description;
    }

    public Integer getProteinPerServing() {
        return proteinPerServing;
    }

    public Integer getCalories() {
        return calories;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setFlavor(Flavor flavor) {
        this.flavor = flavor;
    }

    public void setSubmittedBy(AppUser submittedBy) {
        this.submittedBy = submittedBy;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProteinType(ProteinType proteinType) {
        this.proteinType = proteinType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProteinPerServing(Integer proteinPerServing) {
        this.proteinPerServing = proteinPerServing;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}