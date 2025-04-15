package com.mylstech.product.service;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;

public interface ServicesService {
    ServiceResponse addService(ServiceRequest serviceRequest);
}
