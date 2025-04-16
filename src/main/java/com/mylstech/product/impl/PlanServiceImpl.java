package com.mylstech.product.impl;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;
import com.mylstech.product.model.Plan;
import com.mylstech.product.repository.PlanRepository;
import com.mylstech.product.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    @Override
    public PlanResponse addPlan(PlanRequest request) {
        Plan save = planRepository.save ( request.toPlan (request ) );
        return new PlanResponse ( save );
    }

    @Override
    public List<PlanResponse> getPlan() {
        return planRepository.findAll ().stream ().map ( PlanResponse::new ).toList ();
    }

    @Override
    public PlanResponse updatePlan(Long planId, PlanRequest request) {
        Plan existingPlan = planRepository.findById ( planId ).orElseThrow ( () -> new RuntimeException ( "plan not found" ) );
        if(request.getTitle () != null) {
            existingPlan.setTitle ( request.getTitle () );
        }
        if (request.getDescription() != null) {
            existingPlan.setDescription(request.getDescription());
        }
        if(request.getImageUrl () != null) {
            existingPlan.setImageUrl ( request.getImageUrl () );
        }
        if (request.getPricing() != null) {
            existingPlan.setPricing( BigDecimal.valueOf(request.getPricing()));
        }
        if (request.getStatus() != null) {
            existingPlan.setStatus(request.getStatus());
        }
        if (request.getDuration() != null) {
            existingPlan.setDuration(request.getDuration());
        }
        if (request.getPlanType() != null) {
            existingPlan.setPlanType(request.getPlanType());
        }
        if (request.getHighlights() != null) {
            existingPlan.getHighlightsEmbedded().addAll(request.getHighlights());

        }

        Plan updatedPlan = planRepository.save(existingPlan);
        return new PlanResponse(updatedPlan);
    }


}
