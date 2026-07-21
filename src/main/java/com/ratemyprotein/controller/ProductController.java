package com.ratemyprotein.controller;

import com.ratemyprotein.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
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
        model.addAttribute(
                "product",
                productService.getProductById(id)
        );

        return "products/detail";
    }
}