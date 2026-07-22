package com.ratemyprotein.controller;

import com.ratemyprotein.entity.Product;
import com.ratemyprotein.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.ratemyprotein.dto.ProductEditRequest;
import com.ratemyprotein.entity.ProteinType;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(
            ProductService productService
    ) {
        this.productService = productService;
    }

    /*
     * Main catalogue-management page.
     */
    @GetMapping
    public String showManagedProducts(Model model) {

        List<Product> products =
                productService.getManagedProducts();

        long activeProductCount = products.stream()
                .filter(product ->
                        Boolean.TRUE.equals(product.getActive())
                )
                .count();

        long inactiveProductCount =
                products.size() - activeProductCount;

        model.addAttribute("products", products);
        model.addAttribute(
                "activeProductCount",
                activeProductCount
        );
        model.addAttribute(
                "inactiveProductCount",
                inactiveProductCount
        );

        model.addAttribute(
                "pendingProductCount",
                productService
                        .getPendingProductSubmissions()
                        .size()
        );

        return "admin/products/list";
    }

    /*
     * Pending user submissions.
     */
    @GetMapping("/pending")
    public String showPendingProducts(Model model) {

        model.addAttribute(
                "pendingProducts",
                productService.getPendingProductSubmissions()
        );

        return "admin/products/pending";
    }

    /*
     * Approve a pending user submission.
     */
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

    /*
     * Reject and delete a pending user submission.
     */
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

    /*
     * Make an approved product publicly visible.
     */
    @PostMapping("/{id}/activate")
    public String activateProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            productService.activateProduct(id);

            redirectAttributes.addFlashAttribute(
                    "catalogueSuccess",
                    "The product was activated successfully."
            );
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "catalogueError",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/products";
    }

    /*
     * Hide an approved product from the public catalogue.
     */
    @PostMapping("/{id}/deactivate")
    public String deactivateProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            productService.deactivateProduct(id);

            redirectAttributes.addFlashAttribute(
                    "catalogueSuccess",
                    "The product was deactivated successfully."
            );
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "catalogueError",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/products";
    }
    @GetMapping("/{id}/edit")
    public String showEditProductForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            model.addAttribute(
                    "productEditRequest",
                    productService.getProductEditRequest(id)
            );

            model.addAttribute("productId", id);
            addEditFormOptions(model);

            return "admin/products/edit";

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "catalogueError",
                    exception.getMessage()
            );

            return "redirect:/admin/products";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("productEditRequest")
            ProductEditRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            addEditFormOptions(model);

            return "admin/products/edit";
        }

        try {
            productService.updateProduct(id, request);

        } catch (IllegalArgumentException exception) {

            bindingResult.reject(
                    "productEditError",
                    exception.getMessage()
            );

            model.addAttribute("productId", id);
            addEditFormOptions(model);

            return "admin/products/edit";
        }

        redirectAttributes.addFlashAttribute(
                "catalogueSuccess",
                "The product was updated successfully."
        );

        return "redirect:/admin/products";
    }

    private void addEditFormOptions(Model model) {

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