package com.example.kukuafya;

import com.google.gson.annotations.SerializedName;

public class AnalysisResponse {
    @SerializedName("isFeces")
    private boolean isFeces;

    @SerializedName("healthStatus")
    private String healthStatus;

    @SerializedName("confidence")
    private String confidence;

    @SerializedName("description")
    private String description;

    @SerializedName("recommendation")
    private String recommendation;

    @SerializedName("error")
    private String error;

    public boolean isFeces() {
        return isFeces;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public String getConfidence() {
        return confidence;
    }

    public String getDescription() {
        return description;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getError() {
        return error;
    }

    public boolean hasError() {
        return error != null && !error.isEmpty();
    }
}

