package com.mylstech.product.mapper;

import com.mylstech.product.dto.request.SubscriptionRequest;
import com.mylstech.product.dto.response.SubscriptionResponse;
import com.mylstech.product.model.Customer;
import com.mylstech.product.model.Plan;
import com.mylstech.product.model.Subscription;

import java.util.List;

public interface SubscriptionMapper {
    /**
     * Convert a Subscription entity to a SubscriptionResponse DTO
     */
    SubscriptionResponse toDto(Subscription subscription);
    
    /**
     * Convert a SubscriptionRequest DTO to a Subscription entity
     * @param request The SubscriptionRequest DTO
     * @param customer The Customer entity (from authentication context)
     * @param plan The Plan entity
     */
    Subscription toEntity(SubscriptionRequest request, Customer customer, Plan plan);
    
    /**
     * Update a Subscription entity from a SubscriptionRequest DTO
     * @param subscription The Subscription entity to update
     * @param request The SubscriptionRequest DTO with updated values
     * @param customer The Customer entity (optional, can be null)
     * @param plan The Plan entity (optional, can be null)
     */
    Subscription updateEntityFromDto(Subscription subscription, SubscriptionRequest request, Customer customer, Plan plan);
    
    /**
     * Convert a list of Subscription entities to a list of SubscriptionResponse DTOs
     */
    List<SubscriptionResponse> toDtoList(List<Subscription> subscriptions);
}