package com.ratemyprotein.controller;

import com.ratemyprotein.dto.ReviewRequest;
import com.ratemyprotein.entity.Product;
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

        return "reviews/form";
    }

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

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
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

            model.addAttribute("product", product);

            return "reviews/form";
        }

        return "redirect:/products/"
                + productId
                + "?reviewAdded";
    }
}