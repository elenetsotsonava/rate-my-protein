package com.ratemyprotein.controller;

import com.ratemyprotein.entity.Review;
import com.ratemyprotein.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final ReviewService reviewService;

    public AdminReviewController(
            ReviewService reviewService
    ) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String showReviews(Model model) {

        List<Review> reviews =
                reviewService.getAllReviewsForAdmin();

        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewCount", reviews.size());

        return "admin/reviews/list";
    }

    @PostMapping("/{id}/delete")
    public String deleteReview(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            reviewService.deleteReviewAsAdmin(id);

            redirectAttributes.addFlashAttribute(
                    "moderationSuccess",
                    "The review was deleted successfully."
            );

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "moderationError",
                    exception.getMessage()
            );
        }

        return "redirect:/admin/reviews";
    }
}