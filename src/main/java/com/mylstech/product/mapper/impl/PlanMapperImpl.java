package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;
import com.mylstech.product.mapper.PlanMapper;
import com.mylstech.product.model.Plan;
import com.mylstech.product.util.PlanType;
import org.springframework.stereotype.Component;

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
        plan.setPricing ( planRequest.getPricing ( ) != null ? planRequest.getPricing ( ) : 0.0 );
        plan.setIsActive ( planRequest.getIsActive ( ) != null && planRequest.getIsActive ( ) );
        plan.setTrailDuration ( planRequest.getTrailDuration ( ) != null ? planRequest.getTrailDuration ( ) : 0 );
        plan.setPlanType ( planRequest.getPlanType ( ) != null ? planRequest.getPlanType ( ) : PlanType.MONTHLY );

        if ( planRequest.getHighlights ( ) != null && ! planRequest.getHighlights ( ).isEmpty ( ) ) {
            plan.setHighlights ( new ArrayList<>( planRequest.getHighlights ( ) ) );
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
            plan.setPricing ( planRequest.getPricing ( ) );
        }

        if ( planRequest.getIsActive ( ) != null ) {
            plan.setIsActive ( planRequest.getIsActive ( ) );
        }

        if ( planRequest.getTrailDuration ( ) != null ) {
            plan.setTrailDuration ( planRequest.getTrailDuration ( ) );
        }

        if ( planRequest.getPlanType ( ) != null ) {
            plan.setPlanType ( planRequest.getPlanType ( ) );
        }

        if ( planRequest.getHighlights ( ) != null ) {
            // Clear existing highlights and add new ones
            plan.getHighlights ( ).clear ( );
            
            if ( ! planRequest.getHighlights ( ).isEmpty ( ) ) {
                plan.getHighlights ( ).addAll ( planRequest.getHighlights ( ) );
            }
        }

        return plan;
    }
}
