package com.ratemyprotein.controller;

import com.ratemyprotein.entity.AppUser;
import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.Review;
import com.ratemyprotein.service.ProductService;
import com.ratemyprotein.service.ReviewService;
import com.ratemyprotein.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final ProductService productService;

    public ProfileController(
            UserService userService,
            ReviewService reviewService,
            ProductService productService
    ) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @GetMapping("/profile")
    public String showProfile(
            Principal principal,
            Model model
    ) {
        AppUser user = userService.getUserByEmail(
                principal.getName()
        );

        List<Review> reviews =
                reviewService.getReviewsByUser(user.getId());

        List<Product> submittedProducts =
                productService.getSubmittedProductsByUser(
                        user.getId()
                );

        model.addAttribute("user", user);
        model.addAttribute("reviews", reviews);
        model.addAttribute(
                "submittedProducts",
                submittedProducts
        );

        model.addAttribute(
                "reviewCount",
                reviews.size()
        );

        model.addAttribute(
                "submissionCount",
                submittedProducts.size()
        );

        return "profile/index";
    }
}