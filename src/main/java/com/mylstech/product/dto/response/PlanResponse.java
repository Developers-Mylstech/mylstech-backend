package com.mylstech.product.dto.response;

import com.mylstech.product.model.ImageEntity;
import com.mylstech.product.model.Plan;
import com.mylstech.product.util.PlanType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlanResponse {
    private Long planId;
    private String title;
    private String description;
    private Double pricing;
    private Boolean isActive;
    private Integer trailDuration;
    private PlanType planType;
    private ImageResponse image;
    private List<String> highlights = new ArrayList<>();

    public PlanResponse() {
        // Default constructor for mapper
    }

    public PlanResponse(Plan plan) {
        this.planId = plan.getPlanId();
        this.title = plan.getTitle();
        this.description = plan.getDescription();
        this.pricing = plan.getPricing();
        this.isActive = plan.getIsActive();
        this.trailDuration = plan.getTrailDuration();
        this.planType = plan.getPlanType();
        
        if (plan.getImage() != null) {
            this.image = new ImageResponse(plan.getImage());
        }
        
        if (plan.getHighlights() != null && !plan.getHighlights().isEmpty()) {
            this.highlights = new ArrayList<>(plan.getHighlights());
        }
    }
    
    @Getter
    @Setter
    public static class ImageResponse {
        private Long imageId;
        private String imageUrl;
        
        public ImageResponse(ImageEntity image) {
            this.imageId = image.getImageId();
            this.imageUrl = image.getImageUrl();
        }
    }
}