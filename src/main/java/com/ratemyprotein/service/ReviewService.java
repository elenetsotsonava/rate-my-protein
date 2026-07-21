package com.ratemyprotein.service;

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

    private AppUser findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Authenticated user was not found"
                        )
                );
    }
}