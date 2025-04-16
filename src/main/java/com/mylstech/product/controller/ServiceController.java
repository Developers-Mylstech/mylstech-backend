package com.mylstech.product.controller;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;
import com.mylstech.product.service.ServicesService;
import com.mylstech.product.util.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServicesService serviceService;

    @PostMapping("/addService")
    public ResponseEntity<ServiceResponse> addService(@RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceService.addService(serviceRequest));

    }

    @GetMapping("/getServices")
    public ResponseEntity<List<ServiceResponse>> getServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }


    @GetMapping("/getFeaturedServices")
    public ResponseEntity<List<ServiceResponse>> getFeaturedServices() {
        return ResponseEntity.ok(serviceService.getByServiceType ( ServiceType.FEATURED));
    }
    @GetMapping("/getSoftwareServices")
    public ResponseEntity<List<ServiceResponse>> getSoftwareServices() {
        return ResponseEntity.ok(serviceService.getByServiceType ( ServiceType.SOFTWARE));
    }

    @GetMapping("/getOtherServices")
    public ResponseEntity<List<ServiceResponse>> getOtherServices() {
        return ResponseEntity.ok(serviceService.getByServiceType ( ServiceType.OTHER));
    }
    //update
    @PutMapping("/updateService/{serviceId}")
    public ResponseEntity<ServiceResponse> updateService(@PathVariable Long serviceId, @RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceService.updateService(serviceId, serviceRequest));
    }
    //delete
    @DeleteMapping("/deleteService/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
        serviceService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }
}
