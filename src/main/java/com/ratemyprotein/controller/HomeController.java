package com.ratemyprotein.controller;

import com.ratemyprotein.entity.Product;
import com.ratemyprotein.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {

        List<Product> featuredProducts = productService
                .getActiveProducts()
                .stream()
                .limit(3)
                .toList();

        model.addAttribute(
                "featuredProducts",
                featuredProducts
        );

        return "index";
    }
}