package com.mylstech.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {
    // Customer ID is removed as it will be retrieved from authentication context
    
    @NotNull(message = "Plan ID is required")
    private Long planId;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
}
