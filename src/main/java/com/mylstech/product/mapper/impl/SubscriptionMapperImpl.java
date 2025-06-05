package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.SubscriptionRequest;
import com.mylstech.product.dto.response.SubscriptionResponse;
import com.mylstech.product.mapper.SubscriptionMapper;
import com.mylstech.product.model.Customer;
import com.mylstech.product.model.Plan;
import com.mylstech.product.model.Subscription;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionMapperImpl implements SubscriptionMapper {
    
    @Override
    public SubscriptionResponse toDto(Subscription subscription) {
        if (subscription == null) {
            return null;
        }
        
        return new SubscriptionResponse(subscription);
    }
    
    @Override
    public Subscription toEntity(SubscriptionRequest request, Customer customer, Plan plan) {
        if (request == null) {
            return null;
        }
        
        Subscription subscription = new Subscription();
        subscription.setCustomer(customer);
        subscription.setPlan(plan);
        subscription.setStartDate(request.getStartDate());
        // End date will be set by the service based on plan type
        subscription.setIsExpired(false); // Default value
        
        return subscription;
    }
    
    @Override
    public Subscription updateEntityFromDto(Subscription subscription, SubscriptionRequest request, Customer customer, Plan plan) {
        if (request == null) {
            return subscription;
        }
        
        if (customer != null) {
            subscription.setCustomer(customer);
        }
        
        if (plan != null) {
            subscription.setPlan(plan);
        }
        
        if (request.getStartDate() != null) {
            subscription.setStartDate(request.getStartDate());
        }
        
        // End date will be recalculated by the service if needed
        
        return subscription;
    }
    
    @Override
    public List<SubscriptionResponse> toDtoList(List<Subscription> subscriptions) {
        if (subscriptions == null) {
            return List.of();
        }
        
        return subscriptions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}