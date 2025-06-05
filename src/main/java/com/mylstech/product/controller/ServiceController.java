package com.mylstech.product.controller;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;
import com.mylstech.product.service.ServicesService;
import com.mylstech.product.util.ServiceType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@Tag(name = "Service", description = "Service management APIs")
public class ServiceController {
    private final ServicesService serviceService;

    @PostMapping("/addService")
    @Operation(
            summary = "Add a new service", 
            description = "Creates a new service and returns the service details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ServiceResponse> addService(
            @Parameter(description = "Service details to create", required = true)
            @RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceService.addService(serviceRequest));
    }

    @GetMapping("/getServices")
    @Operation(
            summary = "Get all services", 
            description = "Returns a list of all services"
    )
    @ApiResponse(responseCode = "200", description = "List of services retrieved successfully")
    public ResponseEntity<List<ServiceResponse>> getServices() {
        return ResponseEntity.ok ( serviceService.getAllServices ( ) );
    }


    @GetMapping("/getFeaturedServices")
    @Operation(
            summary = "Get featured services", 
            description = "Returns a list of services with FEATURED type"
    )
    @ApiResponse(responseCode = "200", description = "List of featured services retrieved successfully")
    public ResponseEntity<List<ServiceResponse>> getFeaturedServices() {
        return ResponseEntity.ok(serviceService.getByServiceType(ServiceType.FEATURED));
    }

    @GetMapping("/getSoftwareServices")
    @Operation(
            summary = "Get software services", 
            description = "Returns a list of services with SOFTWARE type"
    )
    @ApiResponse(responseCode = "200", description = "List of software services retrieved successfully")
    public ResponseEntity<List<ServiceResponse>> getSoftwareServices() {
        return ResponseEntity.ok(serviceService.getByServiceType(ServiceType.SOFTWARE));
    }

    @GetMapping("/getOtherServices")
    @Operation(
            summary = "Get other services", 
            description = "Returns a list of services with OTHER type"
    )
    @ApiResponse(responseCode = "200", description = "List of other services retrieved successfully")
    public ResponseEntity<List<ServiceResponse>> getOtherServices() {
        return ResponseEntity.ok(serviceService.getByServiceType(ServiceType.OTHER));
    }

    @PutMapping("/updateService/{serviceId}")
    @Operation(
            summary = "Update a service", 
            description = "Updates a service and returns the updated service details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service updated successfully"),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ServiceResponse> updateService(
            @Parameter(description = "ID of the service to update", required = true)
            @PathVariable Long serviceId,
            @Parameter(description = "Updated service details", required = true)
            @RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceService.updateService(serviceId, serviceRequest));
    }

    @DeleteMapping("/deleteService/{serviceId}")
    @Operation(
            summary = "Delete a service", 
            description = "Deletes a service by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    public ResponseEntity<Void> deleteService(
            @Parameter(description = "ID of the service to delete", required = true)
            @PathVariable Long serviceId) {
        serviceService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteServiceImage/{serviceId}")
    @Operation(
            summary = "Delete service image", 
            description = "Removes the image associated with a service"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service image deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    public ResponseEntity<ServiceResponse> deleteServiceImage(
            @Parameter(description = "ID of the service to remove image from", required = true)
            @PathVariable Long serviceId) {
        return ResponseEntity.ok(serviceService.deleteServiceImage(serviceId));
    }
}
