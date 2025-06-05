package com.mylstech.product.service;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;

import java.util.List;
import java.util.Optional;

public interface PlanService {
    PlanResponse addPlan(PlanRequest request);
    List<PlanResponse> getPlan();
    Optional<PlanResponse> getPlanById(Long planId);
    PlanResponse updatePlan(Long planId, PlanRequest request);
    void deletePlan(Long planId);
    
    // New image-related methods
    PlanResponse updatePlanImage(Long planId, Long imageId);
    PlanResponse deletePlanImage(Long planId);
}
