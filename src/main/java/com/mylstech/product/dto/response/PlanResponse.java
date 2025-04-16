package com.mylstech.product.dto.response;

import com.mylstech.product.model.Plan;
import lombok.Getter;
import lombok.Setter;


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
    private PlanType planType;
    private List<String> highlights = new ArrayList<> ( );

    public PlanResponse(Plan plan) {
        this.planId = plan.getPlanId ();
        this.imageUrl = plan.getImageUrl ();
        this.title = plan.getTitle ();
        this.description = plan.getDescription ();
        this.pricing = plan.getPricing ( ).doubleValue ( );
        this.status = plan.getStatus ();
        this.duration = plan.getDuration ();
        this.planType = plan.getPlanType ();
        if ( plan.getHighlightsEmbedded ( ) != null && ! plan.getHighlightsEmbedded ( ).isEmpty ( ) ) {
            // Direct list copy
            this.highlights.addAll ( plan.getHighlightsEmbedded ( ) );
        }
    }
}
