package com.mylstech.product.dto.response;

import com.mylstech.product.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long customerId;
    private UserResponse user;
    private String address;
    private String contact;
    
    public CustomerResponse(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.address = customer.getAddress();
        this.contact = customer.getContact();
        
        if (customer.getUser() != null) {
            this.user = UserResponse.builder()
                    .userId(customer.getUser().getUserId())
                    .fullName(customer.getUser().getFullName())
                    .email(customer.getUser().getEmail())
                    .role(customer.getUser().getRole())
                    .verified(customer.getUser().isVerified())
                    .createdAt(customer.getUser().getCreatedAt())
                    .updatedAt(customer.getUser().getUpdatedAt())
                    .build();
        }
    }
}