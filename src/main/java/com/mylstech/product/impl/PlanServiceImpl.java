package com.mylstech.product.impl;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.mapper.PlanMapper;
import com.mylstech.product.model.Plan;
import com.mylstech.product.repository.ImageRepository;
import com.mylstech.product.repository.PlanRepository;
import com.mylstech.product.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final PlanMapper planMapper;
    private final ImageRepository imageRepository;

    @Override
    public PlanResponse addPlan(PlanRequest request) {
        Plan plan = planMapper.toEntity ( request );
        plan.setImage ( imageRepository.findById ( request.getImageId ( ) ).orElseThrow ( () -> new ResourceNotFoundException ( "Image not found" ) ) );
        Plan savedPlan = planRepository.save ( plan );
        return planMapper.toDto ( savedPlan );
    }

    @Override
    public List<PlanResponse> getPlan() {
        return planRepository.findAll ( ).stream ( ).map ( planMapper::toDto ).toList ( );
    }

    @Override
    public PlanResponse updatePlan(Long planId, PlanRequest request) {
        Plan existingPlan = planRepository.findById ( planId ).orElseThrow ( () -> new RuntimeException ( "plan not found" ) );

        // Update the entity using the mapper
        Plan updatedPlan = planMapper.updateEntityFromDto ( existingPlan, request );

        // Save and return the updated plan
        Plan savedPlan = planRepository.save ( updatedPlan );
        return planMapper.toDto ( savedPlan );
    }

    @Override
    public void deletePlan(Long planId) {
        if ( planRepository.existsById ( planId ) ) {
            planRepository.deleteById ( planId );
        }
    }


}
