package com.mylstech.product.controller;

import com.mylstech.product.dto.request.CustomerRequest;
import com.mylstech.product.dto.response.CustomerResponse;
import com.mylstech.product.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {
    private final CustomerService customerService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all customers", description = "Returns a list of all customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok ( customerService.getAllCustomers ( ) );
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get customer by ID", description = "Returns a customer by its ID")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId) {
        return customerService.getCustomerById ( customerId )
                .map ( ResponseEntity::ok )
                .orElse ( ResponseEntity.notFound ( ).build ( ) );
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#userId)")
    @Operation(summary = "Get customer by user ID", description = "Returns a customer by its user ID")
    public ResponseEntity<CustomerResponse> getCustomerByUserId(@PathVariable Long userId) {
        return customerService.getCustomerByUserId ( userId )
                .map ( ResponseEntity::ok )
                .orElse ( ResponseEntity.notFound ( ).build ( ) );
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current customer profile", description = "Returns the customer profile of the current user")
    public ResponseEntity<CustomerResponse> getCurrentCustomerProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername ( );
        return customerService.getCustomerByEmail ( email )
                .map ( ResponseEntity::ok )
                .orElse ( ResponseEntity.notFound ( ).build ( ) );
    }

    @PutMapping("/profile")
    @Operation(summary = "Update current customer profile", description = "Updates the customer profile of the current authenticated user")
    public ResponseEntity<CustomerResponse> updateCurrentCustomerProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CustomerRequest request) {
        String email = userDetails.getUsername ( );
        return ResponseEntity.ok ( customerService.updateCustomerByEmail ( email, request ) );
    }

    @PutMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a customer", description = "Updates a customer and returns the updated customer details")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok ( customerService.updateCustomer ( customerId, request ) );
    }

//    @PutMapping("/user/{userId}")
//    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#userId)")
//    @Operation(summary = "Update a customer by user ID", description = "Updates a customer by its user ID and returns the updated customer details")
//    public ResponseEntity<CustomerResponse> updateCustomerByUserId(
//            @PathVariable Long userId,
//            @Valid @RequestBody CustomerRequest request) {
//        return ResponseEntity.ok ( customerService.updateCustomerByUserId ( userId, request ) );
//    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a customer", description = "Deletes a customer by its ID")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer ( customerId );
        return ResponseEntity.noContent ( ).build ( );
    }
}