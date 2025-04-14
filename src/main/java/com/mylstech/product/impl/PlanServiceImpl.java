package com.mylstech.product.impl;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;
import com.mylstech.product.model.Plan;
import com.mylstech.product.repository.PlanRepository;
import com.mylstech.product.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}
