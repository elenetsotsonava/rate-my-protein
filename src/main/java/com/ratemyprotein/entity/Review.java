package com.ratemyprotein.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reviews_user_product",
                        columnNames = {"user_id", "product_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_reviews_product_id",
                        columnList = "product_id"
                ),
                @Index(
                        name = "idx_reviews_user_id",
                        columnList = "user_id"
                )
        }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private AppUser user;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;

    @Min(value = 1, message = "Overall rating must be at least 1")
    @Max(value = 5, message = "Overall rating must not exceed 5")
    @Column(
            name = "overall_rating",
            nullable = false
    )
    private Integer overallRating;

    @Min(value = 1, message = "Taste rating must be at least 1")
    @Max(value = 5, message = "Taste rating must not exceed 5")
    @Column(
            name = "taste_rating",
            nullable = false
    )
    private Integer tasteRating;

    @Min(value = 1, message = "Texture rating must be at least 1")
    @Max(value = 5, message = "Texture rating must not exceed 5")
    @Column(
            name = "texture_rating",
            nullable = false
    )
    private Integer textureRating;

    @Min(value = 1, message = "Mixability rating must be at least 1")
    @Max(value = 5, message = "Mixability rating must not exceed 5")
    @Column(
            name = "mixability_rating",
            nullable = false
    )
    private Integer mixabilityRating;

    @Min(value = 1, message = "Sweetness rating must be at least 1")
    @Max(value = 5, message = "Sweetness rating must not exceed 5")
    @Column(
            name = "sweetness_rating",
            nullable = false
    )
    private Integer sweetnessRating;

    @Min(value = 1, message = "Aftertaste rating must be at least 1")
    @Max(value = 5, message = "Aftertaste rating must not exceed 5")
    @Column(
            name = "aftertaste_rating",
            nullable = false
    )
    private Integer aftertasteRating;

    @Column(
            name = "review_text",
            nullable = false,
            length = 2000
    )
    private String reviewText;

    @Column(
            name = "would_buy_again",
            nullable = false
    )
    private boolean wouldBuyAgain;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    public Review() {
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public Integer getTasteRating() {
        return tasteRating;
    }

    public void setTasteRating(Integer tasteRating) {
        this.tasteRating = tasteRating;
    }

    public Integer getTextureRating() {
        return textureRating;
    }

    public void setTextureRating(Integer textureRating) {
        this.textureRating = textureRating;
    }

    public Integer getMixabilityRating() {
        return mixabilityRating;
    }

    public void setMixabilityRating(Integer mixabilityRating) {
        this.mixabilityRating = mixabilityRating;
    }

    public Integer getSweetnessRating() {
        return sweetnessRating;
    }

    public void setSweetnessRating(Integer sweetnessRating) {
        this.sweetnessRating = sweetnessRating;
    }

    public Integer getAftertasteRating() {
        return aftertasteRating;
    }

    public void setAftertasteRating(Integer aftertasteRating) {
        this.aftertasteRating = aftertasteRating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public boolean isWouldBuyAgain() {
        return wouldBuyAgain;
    }

    public void setWouldBuyAgain(boolean wouldBuyAgain) {
        this.wouldBuyAgain = wouldBuyAgain;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}