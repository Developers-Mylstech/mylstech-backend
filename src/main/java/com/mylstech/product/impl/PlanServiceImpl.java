package com.mylstech.product.impl;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.mapper.PlanMapper;
import com.mylstech.product.model.Plan;
import com.mylstech.product.repository.ImageRepository;
import com.mylstech.product.repository.PlanRepository;
import com.mylstech.product.service.ImageService;
import com.mylstech.product.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final PlanMapper planMapper;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public PlanResponse addPlan(PlanRequest request) {
        Plan plan = planMapper.toEntity(request);
        
        if (request.getImageId() != null) {
            plan.setImage(imageRepository.findById(request.getImageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Image", "id", request.getImageId())));
        }
        
        Plan savedPlan = planRepository.save(plan);
        return planMapper.toDto(savedPlan);
    }

    @Override
    public List<PlanResponse> getPlan() {
        return planRepository.findAll().stream()
                .map(planMapper::toDto)
                .toList();
    }
    
    @Override
    public Optional<PlanResponse> getPlanById(Long planId) {
        return planRepository.findById(planId)
                .map(planMapper::toDto);
    }

    @Override
    public PlanResponse updatePlan(Long planId, PlanRequest request) {
        Plan existingPlan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", planId));

        // Update the entity using the mapper
        Plan updatedPlan = planMapper.updateEntityFromDto(existingPlan, request);
        
        if (request.getImageId() != null) {
            updatedPlan.setImage(imageRepository.findById(request.getImageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Image", "id", request.getImageId())));
        }

        // Save and return the updated plan
        Plan savedPlan = planRepository.save(updatedPlan);
        return planMapper.toDto(savedPlan);
    }

    @Override
    @Transactional
    public void deletePlan(Long planId) {
        if (!planRepository.existsById(planId)) {
            throw new ResourceNotFoundException("Plan", "id", planId);
        }
        
        // Delete the plan's image first
        deletePlanImage(planId);
        
        // Then delete the plan
        planRepository.deleteById(planId);
    }
    
    @Override
    @Transactional
    public PlanResponse updatePlanImage(Long planId, Long imageId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", planId));
        
        plan.setImage(imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId)));
        
        Plan savedPlan = planRepository.save(plan);
        return planMapper.toDto(savedPlan);
    }

    @Override
    @Transactional
    public PlanResponse deletePlanImage(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", planId));
        
        // Store the image ID before removing the association
        Long imageId = null;
        if (plan.getImage() != null) {
            imageId = plan.getImage().getImageId();
        }
        
        // Remove the image association
        plan.setImage(null);
        Plan savedPlan = planRepository.save(plan);
        
        // Delete the image if it exists
        if (imageId != null) {
            imageService.deleteImage(imageId);
        }
        
        return planMapper.toDto(savedPlan);
    }
}
