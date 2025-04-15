package com.mylstech.product.impl;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;
import com.mylstech.product.repository.ServiceRepository;
import com.mylstech.product.service.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {
    private final ServiceRepository serviceRepository;


    @Override
    public ServiceResponse addService(ServiceRequest serviceRequest) {
        com.mylstech.product.model.Service save = serviceRepository.save ( serviceRequest.toService ( ) );
        return new ServiceResponse ( save );
    }
}
