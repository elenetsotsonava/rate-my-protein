package com.ratemyprotein.dto;

import com.ratemyprotein.entity.Product;

public class ProductRankingItem {

    private final Product product;
    private final long reviewCount;
    private final double averageRating;
    private final double bayesianScore;

    public ProductRankingItem(
            Product product,
            long reviewCount,
            double averageRating,
            double bayesianScore
    ) {
        this.product = product;
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
        this.bayesianScore = bayesianScore;
    }

    public Product getProduct() {
        return product;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public double getBayesianScore() {
        return bayesianScore;
    }
}