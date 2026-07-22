package com.ratemyprotein.controller;

import com.ratemyprotein.service.RankingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RankingController {

    private final RankingService rankingService;

    public RankingController(
            RankingService rankingService
    ) {
        this.rankingService = rankingService;
    }

    @GetMapping("/products/top-rated")
    public String showTopRatedProducts(Model model) {

        model.addAttribute(
                "rankedProducts",
                rankingService.getTopRatedProducts()
        );

        return "products/top-rated";
    }
}