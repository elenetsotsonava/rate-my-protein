package com.ratemyprotein.controller;

import com.ratemyprotein.entity.ProteinType;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String showProducts(
            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            Long brandId,

            @RequestParam(required = false)
            Long flavorId,

            @RequestParam(required = false)
            ProteinType proteinType,

            Model model
    ) {
        model.addAttribute(
                "products",
                productService.searchProducts(
                        search,
                        brandId,
                        flavorId,
                        proteinType
                )
        );

        model.addAttribute(
                "brands",
                productService.getAllBrands()
        );

        model.addAttribute(
                "flavors",
                productService.getAllFlavors()
        );

        model.addAttribute(
                "proteinTypes",
                ProteinType.values()
        );

        model.addAttribute(
                "search",
                search == null ? "" : search
        );

        model.addAttribute(
                "selectedBrandId",
                brandId
        );

        model.addAttribute(
                "selectedFlavorId",
                flavorId
        );

        model.addAttribute(
                "selectedProteinType",
                proteinType
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