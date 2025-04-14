package com.mylstech.product.service;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;

import java.util.List;

public interface PlanService {
    PlanResponse addPlan(PlanRequest request);

    List<PlanResponse> getPlan();
}
