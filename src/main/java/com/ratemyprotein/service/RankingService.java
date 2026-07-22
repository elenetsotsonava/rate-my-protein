package com.ratemyprotein.service;

import com.ratemyprotein.dto.ProductRankingItem;
import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.Review;
import com.ratemyprotein.repository.ProductRepository;
import com.ratemyprotein.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RankingService {

    /*
     * Prior review weight.
     *
     * A value of 5 means a product with only one or two reviews
     * is pulled more strongly toward the global average.
     */
    private static final double PRIOR_REVIEW_WEIGHT = 5.0;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public RankingService(
            ProductRepository productRepository,
            ReviewRepository reviewRepository
    ) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductRankingItem> getTopRatedProducts() {

        List<Product> products =
                productRepository.findByActiveTrue();

        List<Review> allReviews =
                reviewRepository.findAll();

        if (allReviews.isEmpty()) {
            return List.of();
        }

        double globalAverage = allReviews.stream()
                .mapToInt(Review::getOverallRating)
                .average()
                .orElse(0.0);

        Map<Long, List<Review>> reviewsByProduct =
                allReviews.stream()
                        .collect(Collectors.groupingBy(
                                review -> review
                                        .getProduct()
                                        .getId()
                        ));

        return products.stream()
                .map(product -> createRankingItem(
                        product,
                        reviewsByProduct.getOrDefault(
                                product.getId(),
                                List.of()
                        ),
                        globalAverage
                ))
                .filter(Objects::nonNull)
                .sorted(
                        Comparator
                                .comparingDouble(
                                        ProductRankingItem::getBayesianScore
                                )
                                .reversed()
                                .thenComparing(
                                        Comparator
                                                .comparingDouble(
                                                        ProductRankingItem
                                                                ::getAverageRating
                                                )
                                                .reversed()
                                )
                                .thenComparing(
                                        Comparator
                                                .comparingLong(
                                                        ProductRankingItem
                                                                ::getReviewCount
                                                )
                                                .reversed()
                                )
                )
                .toList();
    }

    private ProductRankingItem createRankingItem(
            Product product,
            List<Review> reviews,
            double globalAverage
    ) {
        long reviewCount = reviews.size();

        /*
         * Products without reviews are not displayed in the
         * Top Rated list.
         */
        if (reviewCount == 0) {
            return null;
        }

        double productAverage = reviews.stream()
                .mapToInt(Review::getOverallRating)
                .average()
                .orElse(0.0);

        double bayesianScore =
                (
                        reviewCount
                                / (reviewCount + PRIOR_REVIEW_WEIGHT)
                ) * productAverage
                        +
                        (
                                PRIOR_REVIEW_WEIGHT
                                        / (reviewCount + PRIOR_REVIEW_WEIGHT)
                        ) * globalAverage;

        return new ProductRankingItem(
                product,
                reviewCount,
                productAverage,
                bayesianScore
        );
    }
}