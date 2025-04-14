package com.mylstech.product.dto.response;

import com.mylstech.product.model.Plan;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class PlanResponse {
    private Long planId;
    private String imageUrl;
    private String title;
    private String description;
    private Double pricing;
    private Boolean status;
    private Integer duration;
    private String planType;

    public PlanResponse(Plan plan) {
        this.planId = plan.getPlanId ();
        this.imageUrl = plan.getImageUrl ();
        this.title = plan.getTitle ();
        this.description = plan.getDescription ();
        this.pricing = plan.getPricing ();
        this.status = plan.getStatus ();
        this.duration = plan.getDuration ();
        this.planType = plan.getPlanType ();
    }
}
