package com.mylstech.product.controller;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;
import com.mylstech.product.service.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServicesService serviceService;

    @PostMapping("/addService")
    public ResponseEntity<ServiceResponse> addService(@RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceService.addService(serviceRequest));

    }
}
