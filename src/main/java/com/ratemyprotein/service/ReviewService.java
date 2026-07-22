package com.ratemyprotein.service;

import com.ratemyprotein.dto.ReviewStats;
import java.util.List;
import com.ratemyprotein.dto.ReviewRequest;
import com.ratemyprotein.entity.AppUser;
import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.Review;
import com.ratemyprotein.repository.ProductRepository;
import com.ratemyprotein.repository.ReviewRepository;
import com.ratemyprotein.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    @Transactional(readOnly = true)
    public Review getOwnedReview(
            Long reviewId,
            String email
    ) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Review not found"
                        )
                );

        if (!review.getUser().getEmail()
                .equalsIgnoreCase(email)) {
            throw new SecurityException(
                    "You cannot modify this review"
            );
        }

        return review;
    }

    @Transactional
    public Review updateReview(
            Long reviewId,
            String email,
            ReviewRequest request
    ) {
        Review review = getOwnedReview(reviewId, email);

        review.setOverallRating(request.getOverallRating());
        review.setTasteRating(request.getTasteRating());
        review.setTextureRating(request.getTextureRating());
        review.setMixabilityRating(request.getMixabilityRating());
        review.setSweetnessRating(request.getSweetnessRating());
        review.setAftertasteRating(request.getAftertasteRating());
        review.setReviewText(request.getReviewText().trim());
        review.setWouldBuyAgain(request.isWouldBuyAgain());

        return reviewRepository.save(review);
    }

    @Transactional
    public Long deleteReview(
            Long reviewId,
            String email
    ) {
        Review review = getOwnedReview(reviewId, email);

        Long productId = review.getProduct().getId();

        reviewRepository.delete(review);

        return productId;
    }
    @Transactional(readOnly = true)
    public boolean hasUserReviewedProduct(
            String email,
            Long productId
    ) {
        AppUser user = findUserByEmail(email);

        return reviewRepository.existsByUserIdAndProductId(
                user.getId(),
                productId
        );
    }
    @Transactional(readOnly = true)
    public ReviewRequest createRequestFromReview(
            Long reviewId,
            String email
    ) {
        Review review = getOwnedReview(reviewId, email);

        ReviewRequest request = new ReviewRequest();

        request.setOverallRating(review.getOverallRating());
        request.setTasteRating(review.getTasteRating());
        request.setTextureRating(review.getTextureRating());
        request.setMixabilityRating(review.getMixabilityRating());
        request.setSweetnessRating(review.getSweetnessRating());
        request.setAftertasteRating(review.getAftertasteRating());
        request.setReviewText(review.getReviewText());
        request.setWouldBuyAgain(review.isWouldBuyAgain());

        return request;
    }
    @Transactional
    public Review createReview(
            Long productId,
            String email,
            ReviewRequest request
    ) {
        AppUser user = findUserByEmail(email);

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found"
                        )
                );

        if (reviewRepository.existsByUserIdAndProductId(
                user.getId(),
                productId
        )) {
            throw new IllegalStateException(
                    "You have already reviewed this product"
            );
        }

        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setOverallRating(request.getOverallRating());
        review.setTasteRating(request.getTasteRating());
        review.setTextureRating(request.getTextureRating());
        review.setMixabilityRating(request.getMixabilityRating());
        review.setSweetnessRating(request.getSweetnessRating());
        review.setAftertasteRating(request.getAftertasteRating());
        review.setReviewText(request.getReviewText().trim());
        review.setWouldBuyAgain(request.isWouldBuyAgain());

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository
                .findByProductIdOrderByCreatedAtDesc(productId);
    }

    public ReviewStats calculateStats(List<Review> reviews) {

        if (reviews == null || reviews.isEmpty()) {
            return new ReviewStats(
                    0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.0
            );
        }

        double averageOverall = reviews.stream()
                .mapToInt(Review::getOverallRating)
                .average()
                .orElse(0.0);

        double averageTaste = reviews.stream()
                .mapToInt(Review::getTasteRating)
                .average()
                .orElse(0.0);

        double averageTexture = reviews.stream()
                .mapToInt(Review::getTextureRating)
                .average()
                .orElse(0.0);

        double averageMixability = reviews.stream()
                .mapToInt(Review::getMixabilityRating)
                .average()
                .orElse(0.0);

        double averageSweetness = reviews.stream()
                .mapToInt(Review::getSweetnessRating)
                .average()
                .orElse(0.0);

        double averageAftertaste = reviews.stream()
                .mapToInt(Review::getAftertasteRating)
                .average()
                .orElse(0.0);

        return new ReviewStats(
                reviews.size(),
                averageOverall,
                averageTaste,
                averageTexture,
                averageMixability,
                averageSweetness,
                averageAftertaste
        );
    }

    private AppUser findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Authenticated user was not found"
                        )
                );
    }
    @Transactional(readOnly = true)
    public List<Review> getAllReviewsForAdmin() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public void deleteReviewAsAdmin(Long reviewId) {

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Review was not found."
                        )
                );

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository
                .findByUserIdOrderByCreatedAtDesc(userId);
    }
}