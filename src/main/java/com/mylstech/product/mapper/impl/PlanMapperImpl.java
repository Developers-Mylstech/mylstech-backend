package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;
import com.mylstech.product.mapper.PlanMapper;
import com.mylstech.product.model.Plan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Implementation of the PlanMapper interface
 */
@Component
public class PlanMapperImpl implements PlanMapper {

    @Override
    public PlanResponse toDto(Plan plan) {
        if ( plan == null ) {
            return null;
        }

        return new PlanResponse ( plan );
    }

    @Override
    public Plan toEntity(PlanRequest planRequest) {
        if ( planRequest == null ) {
            return null;
        }

        Plan plan = new Plan ( );

        plan.setTitle ( planRequest.getTitle ( ) != null ? planRequest.getTitle ( ) : "title" );
        plan.setDescription ( planRequest.getDescription ( ) != null ? planRequest.getDescription ( ) : "" );
        plan.setPricing ( planRequest.getPricing ( ) != null ? BigDecimal.valueOf ( planRequest.getPricing ( ) ) : BigDecimal.ZERO );
        plan.setStatus ( planRequest.getStatus ( ) != null && planRequest.getStatus ( ) );
        plan.setDuration ( planRequest.getDuration ( ) != null ? planRequest.getDuration ( ) : 0 );
        plan.setPlanType ( planRequest.getPlanType ( ) != null ? planRequest.getPlanType ( ) : com.mylstech.product.util.PlanType.MONTHLY );

        if ( planRequest.getHighlights ( ) != null && ! planRequest.getHighlights ( ).isEmpty ( ) ) {
            if ( plan.getHighlightsEmbedded ( ) == null ) {
                plan.setHighlightsEmbedded ( new ArrayList<> ( ) );
            }
            plan.getHighlightsEmbedded ( ).addAll ( planRequest.getHighlights ( ) );
        }

        return plan;
    }

    @Override
    public Plan updateEntityFromDto(Plan plan, PlanRequest planRequest) {
        if ( planRequest == null ) {
            return plan;
        }

        if ( planRequest.getTitle ( ) != null ) {
            plan.setTitle ( planRequest.getTitle ( ) );
        }

        if ( planRequest.getDescription ( ) != null ) {
            plan.setDescription ( planRequest.getDescription ( ) );
        }


        if ( planRequest.getPricing ( ) != null ) {
            plan.setPricing ( BigDecimal.valueOf ( planRequest.getPricing ( ) ) );
        }

        if ( planRequest.getStatus ( ) != null ) {
            plan.setStatus ( planRequest.getStatus ( ) );
        }

        if ( planRequest.getDuration ( ) != null ) {
            plan.setDuration ( planRequest.getDuration ( ) );
        }

        if ( planRequest.getPlanType ( ) != null ) {
            plan.setPlanType ( planRequest.getPlanType ( ) );
        }

        if ( planRequest.getHighlights ( ) != null && ! planRequest.getHighlights ( ).isEmpty ( ) ) {
            if ( plan.getHighlightsEmbedded ( ) == null ) {
                plan.setHighlightsEmbedded ( new ArrayList<> ( ) );
            }
            plan.getHighlightsEmbedded ( ).addAll ( planRequest.getHighlights ( ) );
        }

        return plan;
    }
}
