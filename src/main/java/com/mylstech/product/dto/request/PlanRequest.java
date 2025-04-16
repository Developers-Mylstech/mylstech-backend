package com.mylstech.product.dto.request;

import com.mylstech.product.model.Plan;
import com.mylstech.product.util.PlanType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PlanRequest {
    private String imageUrl;
    private String title;
    private String description;
    private Double pricing;
    private Boolean status;
    private Integer duration;
    private PlanType planType;
    private List<String> highlights = new ArrayList<> ( );

    public Plan toPlan(PlanRequest request) {
        Plan plan = new Plan ( );
        plan.setImageUrl ( request.imageUrl != null ? request.imageUrl : "" );
        plan.setTitle ( request.title != null ? request.title : "title" );
        plan.setDescription ( request.description != null ? request.description : "" );
        plan.setPricing ( request.pricing != null ? BigDecimal.valueOf ( request.pricing ) : BigDecimal.ZERO );
        plan.setStatus ( request.status != null ? request.status : false );
        plan.setDuration ( request.duration != null ? request.duration : 0 );
        plan.setPlanType ( request.planType != null ? request.planType : PlanType.MONTHLY );
        if (this.highlights != null && !this.highlights.isEmpty()) {
            if (plan.getHighlightsEmbedded() == null) {
                plan.setHighlightsEmbedded(new ArrayList<>());
            }
            plan.getHighlightsEmbedded().addAll(this.highlights);
        }
        return plan;
    }


}
