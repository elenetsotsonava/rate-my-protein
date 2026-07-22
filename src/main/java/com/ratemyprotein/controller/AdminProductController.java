package com.ratemyprotein.controller;

import com.ratemyprotein.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(
            ProductService productService
    ) {
        this.productService = productService;
    }

    @GetMapping("/pending")
    public String showPendingProducts(Model model) {

        model.addAttribute(
                "pendingProducts",
                productService.getPendingProductSubmissions()
        );

        return "admin/products/pending";
    }

    @PostMapping("/{id}/approve")
    public String approveProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            productService.approveProductSubmission(id);

            redirectAttributes.addFlashAttribute(
                    "approvalSuccess",
                    "The product was approved successfully."
            );
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "adminError",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/products/pending";
    }

    @PostMapping("/{id}/reject")
    public String rejectProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            productService.rejectProductSubmission(id);

            redirectAttributes.addFlashAttribute(
                    "rejectionSuccess",
                    "The product submission was rejected."
            );
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "adminError",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/products/pending";
    }
}