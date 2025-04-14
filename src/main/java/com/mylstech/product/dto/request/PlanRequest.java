package com.mylstech.product.dto.request;

import com.mylstech.product.model.Plan;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlanRequest {
    private String imageUrl;
    private String title;
    private String description;
    private Double pricing;
    private Boolean status;
    private Integer duration;
    private String planType;

    public Plan toPlan(PlanRequest request) {
        Plan plan = new Plan ( );
        plan.setImageUrl ( request.imageUrl );
        plan.setTitle ( request.title );
        plan.setDescription ( request.description );
        plan.setPricing ( BigDecimal.valueOf ( request.pricing ) );
        plan.setStatus ( request.status );
        plan.setDuration ( request.duration );
        plan.setPlanType ( request.planType );

        return plan;
    }


}
