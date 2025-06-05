package com.mylstech.product.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest {
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    private String postalCode;
    private String address;
}