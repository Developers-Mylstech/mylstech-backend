package com.mylstech.product.controller;

import com.mylstech.product.dto.request.SubscriptionRequest;
import com.mylstech.product.dto.response.SubscriptionResponse;
import com.mylstech.product.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscription", description = "Subscription management APIs")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    
    @PostMapping
    @Operation(
            summary = "Create a new subscription", 
            description = "Creates a new subscription for the current user and returns the subscription details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Parameter(description = "Subscription details to create", required = true)
            @Valid @RequestBody SubscriptionRequest request) {
        return new ResponseEntity<>(subscriptionService.createSubscription(request), HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all subscriptions", 
            description = "Returns a list of all subscriptions in the system. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of subscriptions retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }
    
    @GetMapping("/my-subscriptions")
    @Operation(
            summary = "Get current user's subscriptions", 
            description = "Returns a list of all subscriptions for the currently authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "List of subscriptions retrieved successfully")
    public ResponseEntity<List<SubscriptionResponse>> getCurrentUserSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getCurrentUserSubscriptions());
    }
    
    @GetMapping("/my-subscriptions/active")
    @Operation(summary = "Get current user's active subscriptions", description = "Returns a list of active subscriptions for the current user")
    public ResponseEntity<List<SubscriptionResponse>> getCurrentUserActiveSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getCurrentUserActiveSubscriptions());
    }
    
    @GetMapping("/{subscriptionId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get subscription by ID", 
            description = "Returns a subscription by its ID. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(
            @Parameter(description = "ID of the subscription to retrieve", required = true)
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(subscriptionId).orElseThrow (()-> new UsernameNotFoundException ("no subscription found")));
    }
    
    @GetMapping("/my-subscriptions/{subscriptionId}")
    @Operation(
            summary = "Get current user's subscription by ID", 
            description = "Returns a specific subscription by ID for the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found or not owned by current user")
    })
    public ResponseEntity<SubscriptionResponse> getCurrentUserSubscription(
            @Parameter(description = "ID of the subscription to retrieve", required = true)
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.getCurrentUserSubscription(subscriptionId).orElseThrow (() -> new UsernameNotFoundException ("no subscription found")));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get subscriptions by customer ID", 
            description = "Returns a list of subscriptions for a specific customer. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of subscriptions retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptionsByCustomerId(
            @Parameter(description = "ID of the customer to get subscriptions for", required = true)
            @PathVariable Long customerId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByCustomerId(customerId));
    }
    
    @GetMapping("/customer/{customerId}/active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get active subscriptions by customer ID", description = "Returns a list of active subscriptions for a customer (admin only)")
    public ResponseEntity<List<SubscriptionResponse>> getActiveSubscriptionsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscriptionsByCustomerId(customerId));
    }
    
    @GetMapping("/plan/{planId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get subscriptions by plan ID", description = "Returns a list of subscriptions for a plan (admin only)")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptionsByPlanId(@PathVariable Long planId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByPlanId(planId));
    }
    
    @PutMapping("/my-subscriptions/{subscriptionId}")
    @Operation(summary = "Update a subscription", description = "Updates a subscription for the current user")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(subscriptionId, request));
    }
    
    @PutMapping("/my-subscriptions/{subscriptionId}/renew")
    @Operation(
            summary = "Renew a subscription", 
            description = "Renews a subscription for the current user and returns the updated subscription details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription renewed successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found or not owned by current user"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<SubscriptionResponse> renewSubscription(
            @Parameter(description = "ID of the subscription to renew", required = true)
            @PathVariable Long subscriptionId,
            @Parameter(description = "Renewal details", required = true)
            @Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.renewSubscription(subscriptionId, request));
    }
    
    @PutMapping("/my-subscriptions/{subscriptionId}/cancel")
    @Operation(
            summary = "Cancel a subscription", 
            description = "Cancels a subscription for the current user and returns the updated subscription details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found or not owned by current user")
    })
    public ResponseEntity<SubscriptionResponse> cancelSubscription(
            @Parameter(description = "ID of the subscription to cancel", required = true)
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(subscriptionId));
    }
    
    @DeleteMapping("/{subscriptionId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a subscription", description = "Deletes a subscription by its ID (admin only)")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.deleteSubscription(subscriptionId);
        return ResponseEntity.noContent().build();
    }
}