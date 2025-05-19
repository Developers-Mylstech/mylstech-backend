package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.CustomerRequest;
import com.mylstech.product.dto.response.CustomerResponse;
import com.mylstech.product.mapper.CustomerMapper;
import com.mylstech.product.model.Customer;
import com.mylstech.product.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerResponse toDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        return new CustomerResponse ( customer );
    }

    @Override
    public Customer toEntity(CustomerRequest customerRequest, User user) {
        if ( customerRequest == null ) {
            return null;
        }

        Customer customer = new Customer ( );
        customer.setUser ( user );
        customer.setAddress ( customerRequest.getAddress ( ) );
        customer.setContact ( customerRequest.getContact ( ) );

        return customer;
    }

    @Override
    public Customer updateEntityFromDto(Customer customer, CustomerRequest customerRequest) {
        if ( customerRequest == null ) {
            return customer;
        }

        if ( customerRequest.getAddress ( ) != null ) {
            customer.setAddress ( customerRequest.getAddress ( ) );
        }

        if ( customerRequest.getContact ( ) != null ) {
            customer.setContact ( customerRequest.getContact ( ) );
        }

        return customer;
    }

    @Override
    public List<CustomerResponse> toDtoList(List<Customer> customers) {
        if ( customers == null ) {
            return List.of ( );
        }

        return customers.stream ( )
                .map ( this::toDto )
                .toList ( );
    }
}