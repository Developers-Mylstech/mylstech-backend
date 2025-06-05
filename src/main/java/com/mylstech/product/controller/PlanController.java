package com.mylstech.product.controller;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
@Tag(name = "Plan", description = "Subscription plan management APIs")
public class PlanController {
    private final PlanService planService;

    @PostMapping
    @Operation(
            summary = "Create a new plan", 
            description = "Creates a new subscription plan. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<PlanResponse> createPlan(
            @Parameter(description = "Plan details to create", required = true)
            @Valid @RequestBody PlanRequest request) {
        return new ResponseEntity<>(planService.addPlan (request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Get all plans", 
            description = "Returns a list of all subscription plans"
    )
    @ApiResponse(responseCode = "200", description = "List of plans retrieved successfully")
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        return ResponseEntity.ok(planService.getPlan ());
    }
    
    @GetMapping("/{planId}")
    @Operation(
            summary = "Get plan by ID", 
            description = "Returns a subscription plan by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<PlanResponse> getPlanById(
            @Parameter(description = "ID of the plan to retrieve", required = true)
            @PathVariable Long planId) {
        return ResponseEntity.ok(planService.getPlanById(planId).orElseThrow (()-> new ResourceNotFoundException ("Plan not found") ));
    }

    @PutMapping("/{planId}")
    @Operation(
            summary = "Update a plan", 
            description = "Updates a subscription plan. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan updated successfully"),
            @ApiResponse(responseCode = "404", description = "Plan not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<PlanResponse> updatePlan(
            @Parameter(description = "ID of the plan to update", required = true)
            @PathVariable Long planId,
            @Parameter(description = "Updated plan details", required = true)
            @Valid @RequestBody PlanRequest request) {
        return ResponseEntity.ok(planService.updatePlan(planId, request));
    }

    @DeleteMapping("/{planId}")
    @Operation(
            summary = "Delete a plan", 
            description = "Deletes a subscription plan. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plan deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Plan not found"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource"),
            @ApiResponse(responseCode = "400", description = "Plan cannot be deleted (e.g., has active subscriptions)")
    })
    public ResponseEntity<Void> deletePlan(
            @Parameter(description = "ID of the plan to delete", required = true)
            @PathVariable Long planId) {
        planService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }
}
