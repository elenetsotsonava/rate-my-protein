package com.ratemyprotein.controller;

import com.ratemyprotein.dto.ProductSubmissionRequest;
import com.ratemyprotein.entity.Product;
import com.ratemyprotein.entity.ProteinType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long flavorId,
            @RequestParam(required = false) ProteinType proteinType,
            Model model
    ) {
        List<Product> products = productService.searchProducts(
                search,
                brandId,
                flavorId,
                proteinType
        );

        model.addAttribute("products", products);
        model.addAttribute("brands", productService.getAllBrands());
        model.addAttribute("flavors", productService.getAllFlavors());
        model.addAttribute("proteinTypes", ProteinType.values());

        model.addAttribute("search", search);
        model.addAttribute("selectedBrandId", brandId);
        model.addAttribute("selectedFlavorId", flavorId);
        model.addAttribute("selectedProteinType", proteinType);

        return "products/list";
    }

    @GetMapping("/products/submit")
    public String showProductSubmissionForm(Model model) {

        if (!model.containsAttribute("productSubmissionRequest")) {
            model.addAttribute(
                    "productSubmissionRequest",
                    new ProductSubmissionRequest()
            );
        }

        addSubmissionOptions(model);

        return "products/submit";
    }

    @PostMapping("/products/submit")
    public String submitProduct(
            @Valid
            @ModelAttribute("productSubmissionRequest")
            ProductSubmissionRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addSubmissionOptions(model);
            return "products/submit";
        }

        try {
            productService.submitProduct(
                    request,
                    principal.getName()
            );
        } catch (IllegalArgumentException exception) {
            bindingResult.reject(
                    "productSubmissionError",
                    exception.getMessage()
            );

            addSubmissionOptions(model);

            return "products/submit";
        }

        redirectAttributes.addFlashAttribute(
                "submissionSuccess",
                true
        );

        return "redirect:/products/submit";
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

    private void addSubmissionOptions(Model model) {
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
    }
}