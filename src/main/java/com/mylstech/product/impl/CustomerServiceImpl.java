package com.mylstech.product.impl;

import com.mylstech.product.dto.request.CustomerRequest;
import com.mylstech.product.dto.response.CustomerResponse;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.mapper.CustomerMapper;
import com.mylstech.product.model.Customer;
import com.mylstech.product.model.User;
import com.mylstech.product.repository.CustomerRepository;
import com.mylstech.product.repository.UserRepository;
import com.mylstech.product.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse createCustomer(User user) {
        // Check if customer already exists for this user
        Optional<Customer> existingCustomer = customerRepository.findByUser(user);
        if (existingCustomer.isPresent()) {
            return customerMapper.toDto(existingCustomer.get());
        }
        
        // Create a new customer with default values
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setAddress(""); // Default empty address
        customer.setContact(""); // Default empty contact
        
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long customerId, CustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        Customer updatedCustomer = customerMapper.updateEntityFromDto(customer, request);
        Customer savedCustomer = customerRepository.save(updatedCustomer);
        
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomerByUserId(Long userId, CustomerRequest request) {
        Customer customer = customerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "userId", userId));
        
        Customer updatedCustomer = customerMapper.updateEntityFromDto(customer, request);
        Customer savedCustomer = customerRepository.save(updatedCustomer);
        
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public Optional<CustomerResponse> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::toDto);
    }

    @Override
    public Optional<CustomerResponse> getCustomerByUserId(Long userId) {
        return customerRepository.findByUserUserId(userId)
                .map(customerMapper::toDto);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDtoList(customers);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }
        
        customerRepository.deleteById(customerId);
    }

    @Override
    public Optional<CustomerResponse> getCustomerByEmail(String email) {
        return userRepository.findByEmail(email)
                .flatMap(user -> customerRepository.findByUserUserId(user.getUserId()))
                .map(customerMapper::toDto);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomerByEmail(String email, CustomerRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        return updateCustomerByUserId(user.getUserId(), request);
    }
}
