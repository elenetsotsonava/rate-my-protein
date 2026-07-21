package com.ratemyprotein.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewRequest {

    @NotNull(message = "Overall rating is required")
    @Min(value = 1, message = "Overall rating must be at least 1")
    @Max(value = 5, message = "Overall rating must not exceed 5")
    private Integer overallRating;

    @NotNull(message = "Taste rating is required")
    @Min(value = 1, message = "Taste rating must be at least 1")
    @Max(value = 5, message = "Taste rating must not exceed 5")
    private Integer tasteRating;

    @NotNull(message = "Texture rating is required")
    @Min(value = 1, message = "Texture rating must be at least 1")
    @Max(value = 5, message = "Texture rating must not exceed 5")
    private Integer textureRating;

    @NotNull(message = "Mixability rating is required")
    @Min(value = 1, message = "Mixability rating must be at least 1")
    @Max(value = 5, message = "Mixability rating must not exceed 5")
    private Integer mixabilityRating;

    @NotNull(message = "Sweetness rating is required")
    @Min(value = 1, message = "Sweetness rating must be at least 1")
    @Max(value = 5, message = "Sweetness rating must not exceed 5")
    private Integer sweetnessRating;

    @NotNull(message = "Aftertaste rating is required")
    @Min(value = 1, message = "Aftertaste rating must be at least 1")
    @Max(value = 5, message = "Aftertaste rating must not exceed 5")
    private Integer aftertasteRating;

    @NotBlank(message = "Review text is required")
    @Size(
            min = 10,
            max = 2000,
            message = "Review must contain between 10 and 2000 characters"
    )
    private String reviewText;

    private boolean wouldBuyAgain;

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public Integer getTasteRating() {
        return tasteRating;
    }

    public void setTasteRating(Integer tasteRating) {
        this.tasteRating = tasteRating;
    }

    public Integer getTextureRating() {
        return textureRating;
    }

    public void setTextureRating(Integer textureRating) {
        this.textureRating = textureRating;
    }

    public Integer getMixabilityRating() {
        return mixabilityRating;
    }

    public void setMixabilityRating(Integer mixabilityRating) {
        this.mixabilityRating = mixabilityRating;
    }

    public Integer getSweetnessRating() {
        return sweetnessRating;
    }

    public void setSweetnessRating(Integer sweetnessRating) {
        this.sweetnessRating = sweetnessRating;
    }

    public Integer getAftertasteRating() {
        return aftertasteRating;
    }

    public void setAftertasteRating(Integer aftertasteRating) {
        this.aftertasteRating = aftertasteRating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public boolean isWouldBuyAgain() {
        return wouldBuyAgain;
    }

    public void setWouldBuyAgain(boolean wouldBuyAgain) {
        this.wouldBuyAgain = wouldBuyAgain;
    }
}