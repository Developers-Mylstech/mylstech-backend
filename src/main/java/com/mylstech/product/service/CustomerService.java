package com.mylstech.product.service;

import com.mylstech.product.dto.request.CustomerRequest;
import com.mylstech.product.dto.response.CustomerResponse;
import com.mylstech.product.model.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    CustomerResponse createCustomer(User user);
    CustomerResponse updateCustomer(Long customerId, CustomerRequest request);
    CustomerResponse updateCustomerByUserId(Long userId, CustomerRequest request);
    Optional<CustomerResponse> getCustomerById(Long customerId);
    Optional<CustomerResponse> getCustomerByUserId(Long userId);
    List<CustomerResponse> getAllCustomers();
    void deleteCustomer(Long customerId);
    /**
     * Gets the customer profile for the current authenticated user by email
     * 
     * @param email The email of the current user
     * @return The customer profile or empty if not found
     */
    Optional<CustomerResponse> getCustomerByEmail(String email);
    /**
     * Updates the customer profile for the current authenticated user by email
     * 
     * @param email The email of the current user
     * @param request The customer data to update
     * @return The updated customer profile
     */
    CustomerResponse updateCustomerByEmail(String email, CustomerRequest request);
}
