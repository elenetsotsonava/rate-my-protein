package com.ratemyprotein.dto;

public class ReviewStats {

    private final long reviewCount;
    private final double averageOverall;
    private final double averageTaste;
    private final double averageTexture;
    private final double averageMixability;
    private final double averageSweetness;
    private final double averageAftertaste;

    public ReviewStats(
            long reviewCount,
            double averageOverall,
            double averageTaste,
            double averageTexture,
            double averageMixability,
            double averageSweetness,
            double averageAftertaste
    ) {
        this.reviewCount = reviewCount;
        this.averageOverall = averageOverall;
        this.averageTaste = averageTaste;
        this.averageTexture = averageTexture;
        this.averageMixability = averageMixability;
        this.averageSweetness = averageSweetness;
        this.averageAftertaste = averageAftertaste;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public double getAverageOverall() {
        return averageOverall;
    }

    public double getAverageTaste() {
        return averageTaste;
    }

    public double getAverageTexture() {
        return averageTexture;
    }

    public double getAverageMixability() {
        return averageMixability;
    }

    public double getAverageSweetness() {
        return averageSweetness;
    }

    public double getAverageAftertaste() {
        return averageAftertaste;
    }
}