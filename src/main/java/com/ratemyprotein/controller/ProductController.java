package com.ratemyprotein.controller;

import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.Review;
import com.ratemyprotein.service.ProductService;
import com.ratemyprotein.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(
            ProductService productService,
            ReviewService reviewService
    ) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("/products")
    public String showProducts(Model model) {

        model.addAttribute(
                "products",
                productService.getActiveProducts()
        );

        return "products/list";
    }

    @GetMapping("/products/{id}")
    public String showProductDetails(
            @PathVariable Long id,
            Model model
    ) {
        Product product = productService.getProductById(id);

        List<Review> reviews =
                reviewService.getReviewsForProduct(id);

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute(
                "reviewStats",
                reviewService.calculateStats(reviews)
        );

        return "products/detail";
    }
}