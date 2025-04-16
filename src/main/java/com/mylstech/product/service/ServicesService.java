package com.mylstech.product.service;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;
import com.mylstech.product.util.ServiceType;

import java.util.List;

public interface ServicesService {
    ServiceResponse addService(ServiceRequest serviceRequest);

    List<ServiceResponse> getAllServices();
    List<ServiceResponse> getByServiceType(ServiceType serviceType);
    ServiceResponse updateService(Long serviceId, ServiceRequest serviceRequest);

    void deleteService(Long serviceId);
}
