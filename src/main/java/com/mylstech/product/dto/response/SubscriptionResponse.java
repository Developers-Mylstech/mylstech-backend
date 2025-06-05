package com.mylstech.product.dto.response;

import com.mylstech.product.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {
    private Long subscriptionId;
    private CustomerResponse customer;
    private PlanResponse plan;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isExpired;
    private Boolean isActive;
    private List<PaymentResponse> payments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public SubscriptionResponse(Subscription subscription) {
        this.subscriptionId = subscription.getSubscriptionId();
        this.customer = new CustomerResponse(subscription.getCustomer());
        this.plan = new PlanResponse(subscription.getPlan());
        this.startDate = subscription.getStartDate();
        this.endDate = subscription.getEndDate();
        this.isExpired = subscription.getIsExpired();
        this.isActive = subscription.isActive();
        this.payments = subscription.getPayments().stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
        this.createdAt = subscription.getCreatedAt();
        this.updatedAt = subscription.getUpdatedAt();
    }
}