package com.mylstech.product.service;

import com.mylstech.product.dto.request.SubscriptionRequest;
import com.mylstech.product.dto.response.SubscriptionResponse;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    /**
     * Create a subscription for the current authenticated user
     */
    SubscriptionResponse createSubscription(SubscriptionRequest request);
    
    /**
     * Get a subscription by ID (admin access)
     */
    Optional<SubscriptionResponse> getSubscriptionById(Long subscriptionId);
    
    /**
     * Get a subscription by ID for the current authenticated user
     */
    Optional<SubscriptionResponse> getCurrentUserSubscription(Long subscriptionId);
    
    /**
     * Get all subscriptions (admin access)
     */
    List<SubscriptionResponse> getAllSubscriptions();
    
    /**
     * Get all subscriptions for the current authenticated user
     */
    List<SubscriptionResponse> getCurrentUserSubscriptions();
    
    /**
     * Get all subscriptions for a customer (admin access)
     */
    List<SubscriptionResponse> getSubscriptionsByCustomerId(Long customerId);
    
    /**
     * Get all subscriptions for a plan (admin access)
     */
    List<SubscriptionResponse> getSubscriptionsByPlanId(Long planId);
    
    /**
     * Get active subscriptions for a customer (admin access)
     */
    List<SubscriptionResponse> getActiveSubscriptionsByCustomerId(Long customerId);
    
    /**
     * Get active subscriptions for the current authenticated user
     */
    List<SubscriptionResponse> getCurrentUserActiveSubscriptions();
    
    /**
     * Update a subscription for the current authenticated user
     */
    SubscriptionResponse updateSubscription(Long subscriptionId, SubscriptionRequest request);
    
    /**
     * Renew a subscription for the current authenticated user
     */
    SubscriptionResponse renewSubscription(Long subscriptionId, SubscriptionRequest request);
    
    /**
     * Cancel a subscription for the current authenticated user
     */
    SubscriptionResponse cancelSubscription(Long subscriptionId);
    
    /**
     * Delete a subscription (admin access)
     */
    void deleteSubscription(Long subscriptionId);
    
    /**
     * Mark expired subscriptions (scheduled task)
     */
    void markExpiredSubscriptions();
}