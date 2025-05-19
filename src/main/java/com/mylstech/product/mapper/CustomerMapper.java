package com.mylstech.product.mapper;

import com.mylstech.product.dto.request.CustomerRequest;
import com.mylstech.product.dto.response.CustomerResponse;
import com.mylstech.product.model.Customer;
import com.mylstech.product.model.User;

import java.util.List;

public interface CustomerMapper {
    CustomerResponse toDto(Customer customer);
    Customer toEntity(CustomerRequest customerRequest, User user);
    Customer updateEntityFromDto(Customer customer, CustomerRequest customerRequest);
    List<CustomerResponse> toDtoList(List<Customer> customers);
}