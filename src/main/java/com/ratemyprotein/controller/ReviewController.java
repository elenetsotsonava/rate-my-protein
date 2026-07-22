package com.ratemyprotein.controller;

import com.ratemyprotein.dto.ReviewRequest;
import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.Review;
import com.ratemyprotein.service.ProductService;
import com.ratemyprotein.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;

    public ReviewController(
            ReviewService reviewService,
            ProductService productService
    ) {
        this.reviewService = reviewService;
        this.productService = productService;
    }

    /*
     * Display the new-review form.
     */
    @GetMapping("/reviews/products/{productId}/new")
    public String showReviewForm(
            @PathVariable Long productId,
            Principal principal,
            Model model
    ) {
        Product product = productService.getProductById(productId);

        if (reviewService.hasUserReviewedProduct(
                principal.getName(),
                productId
        )) {
            return "redirect:/products/"
                    + productId
                    + "?alreadyReviewed";
        }

        model.addAttribute("product", product);
        model.addAttribute(
                "reviewRequest",
                new ReviewRequest()
        );
        model.addAttribute("editMode", false);
        model.addAttribute("reviewId", null);

        return "reviews/form";
    }

    /*
     * Save a new review.
     */
    @PostMapping("/reviews/products/{productId}")
    public String submitReview(
            @PathVariable Long productId,
            @Valid
            @ModelAttribute("reviewRequest")
            ReviewRequest reviewRequest,
            BindingResult bindingResult,
            Principal principal,
            Model model
    ) {
        Product product = productService.getProductById(productId);

        /*
         * When validation fails, restore all model attributes
         * required by reviews/form.html.
         */
        if (bindingResult.hasErrors()) {
            addNewReviewFormAttributes(
                    model,
                    product
            );

            return "reviews/form";
        }

        try {
            reviewService.createReview(
                    productId,
                    principal.getName(),
                    reviewRequest
            );

        } catch (
                IllegalArgumentException |
                IllegalStateException exception
        ) {
            bindingResult.reject(
                    "review.failed",
                    exception.getMessage()
            );

            addNewReviewFormAttributes(
                    model,
                    product
            );

            return "reviews/form";
        }

        return "redirect:/products/"
                + productId
                + "?reviewAdded";
    }

    /*
     * Display the edit-review form.
     */
    @GetMapping("/reviews/{reviewId}/edit")
    public String showEditForm(
            @PathVariable Long reviewId,
            Principal principal,
            Model model
    ) {
        try {
            Review review = reviewService.getOwnedReview(
                    reviewId,
                    principal.getName()
            );

            Product product = productService.getProductById(
                    review.getProduct().getId()
            );

            model.addAttribute("product", product);

            model.addAttribute(
                    "reviewRequest",
                    reviewService.createRequestFromReview(
                            reviewId,
                            principal.getName()
                    )
            );

            model.addAttribute("reviewId", reviewId);
            model.addAttribute("editMode", true);

            return "reviews/form";

        } catch (SecurityException exception) {
            return "redirect:/products?forbidden";

        } catch (IllegalArgumentException exception) {
            return "redirect:/products?reviewNotFound";
        }
    }

    /*
     * Update an existing review.
     */
    @PostMapping("/reviews/{reviewId}/edit")
    public String updateReview(
            @PathVariable Long reviewId,
            @Valid
            @ModelAttribute("reviewRequest")
            ReviewRequest reviewRequest,
            BindingResult bindingResult,
            Principal principal,
            Model model
    ) {
        Review review;

        try {
            review = reviewService.getOwnedReview(
                    reviewId,
                    principal.getName()
            );

        } catch (SecurityException exception) {
            return "redirect:/products?forbidden";

        } catch (IllegalArgumentException exception) {
            return "redirect:/products?reviewNotFound";
        }

        Product product = productService.getProductById(
                review.getProduct().getId()
        );

        if (bindingResult.hasErrors()) {
            addEditReviewFormAttributes(
                    model,
                    product,
                    reviewId
            );

            return "reviews/form";
        }

        try {
            reviewService.updateReview(
                    reviewId,
                    principal.getName(),
                    reviewRequest
            );

        } catch (
                IllegalArgumentException |
                IllegalStateException exception
        ) {
            bindingResult.reject(
                    "review.failed",
                    exception.getMessage()
            );

            addEditReviewFormAttributes(
                    model,
                    product,
                    reviewId
            );

            return "reviews/form";
        }

        return "redirect:/products/"
                + product.getId()
                + "?reviewUpdated";
    }

    /*
     * Delete a review belonging to the logged-in user.
     */
    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(
            @PathVariable Long reviewId,
            Principal principal
    ) {
        try {
            Long productId = reviewService.deleteReview(
                    reviewId,
                    principal.getName()
            );

            return "redirect:/products/"
                    + productId
                    + "?reviewDeleted";

        } catch (SecurityException exception) {
            return "redirect:/products?forbidden";

        } catch (IllegalArgumentException exception) {
            return "redirect:/products?reviewNotFound";
        }
    }

    private void addNewReviewFormAttributes(
            Model model,
            Product product
    ) {
        model.addAttribute("product", product);
        model.addAttribute("editMode", false);
        model.addAttribute("reviewId", null);
    }

    private void addEditReviewFormAttributes(
            Model model,
            Product product,
            Long reviewId
    ) {
        model.addAttribute("product", product);
        model.addAttribute("editMode", true);
        model.addAttribute("reviewId", reviewId);
    }
}