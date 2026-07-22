package com.ratemyprotein.dto;

import com.ratemyprotein.entity.ProteinType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductEditRequest {

    @NotBlank(message = "Product name is required.")
    @Size(
            max = 150,
            message = "Product name cannot exceed 150 characters."
    )
    private String name;

    @NotNull(message = "Please select a brand.")
    private Long brandId;

    @NotNull(message = "Please select a flavor.")
    private Long flavorId;

    @NotNull(message = "Please select a protein type.")
    private ProteinType proteinType;

    @Size(
            max = 2000,
            message = "Description cannot exceed 2000 characters."
    )
    private String description;

    @NotNull(message = "Protein per serving is required.")
    @Min(
            value = 0,
            message = "Protein per serving cannot be negative."
    )
    @Max(
            value = 200,
            message = "Protein per serving cannot exceed 200 grams."
    )
    private Integer proteinPerServing;

    @NotNull(message = "Calories are required.")
    @Min(
            value = 0,
            message = "Calories cannot be negative."
    )
    @Max(
            value = 5000,
            message = "Calories cannot exceed 5000."
    )
    private Integer calories;

    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Price cannot be negative."
    )
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required.")
    @Min(
            value = 0,
            message = "Stock quantity cannot be negative."
    )
    private Integer stockQuantity;

    @Size(
            max = 500,
            message = "Image URL cannot exceed 500 characters."
    )
    private String imageUrl;

    public ProductEditRequest() {
    }

    public String getName() {
        return name;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getFlavorId() {
        return flavorId;
    }

    public ProteinType getProteinType() {
        return proteinType;
    }

    public String getDescription() {
        return description;
    }

    public Integer getProteinPerServing() {
        return proteinPerServing;
    }

    public Integer getCalories() {
        return calories;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public void setFlavorId(Long flavorId) {
        this.flavorId = flavorId;
    }

    public void setProteinType(ProteinType proteinType) {
        this.proteinType = proteinType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProteinPerServing(
            Integer proteinPerServing
    ) {
        this.proteinPerServing = proteinPerServing;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}