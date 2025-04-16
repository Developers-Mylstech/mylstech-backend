package com.mylstech.product.impl;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;
import com.mylstech.product.model.Description;
import com.mylstech.product.model.Highlight;
import com.mylstech.product.repository.ServiceRepository;
import com.mylstech.product.service.ServicesService;
import com.mylstech.product.util.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {
    private final ServiceRepository serviceRepository;


    @Override
    public ServiceResponse addService(ServiceRequest serviceRequest) {
        com.mylstech.product.model.Service save = serviceRepository.save ( serviceRequest.toService ( ) );
        return new ServiceResponse ( save );
    }

    @Override
    public List<ServiceResponse> getAllServices() {
        List<com.mylstech.product.model.Service> all = serviceRepository.findAll ( );
        return all.stream ( ).map ( ServiceResponse::new ).toList ( );
    }

    @Override
    public List<ServiceResponse> getByServiceType(ServiceType serviceType) {
        List<com.mylstech.product.model.Service> all = serviceRepository.findByServiceType ( serviceType );
        return all.stream ( ).map ( ServiceResponse::new ).toList ( );

    }

    @Override
    public ServiceResponse updateService(Long serviceId, ServiceRequest serviceRequest) {
        com.mylstech.product.model.Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

        // Update basic fields
        if (serviceRequest.getServiceType() != null) {
            existingService.setServiceType(serviceRequest.getServiceType());
        }
        if (serviceRequest.getTitle() != null) {
            existingService.setTitle(serviceRequest.getTitle());
        }
        if (serviceRequest.getImageUrl() != null) {
            existingService.setImageUrl(serviceRequest.getImageUrl());
        }

        // Update description
        updateDescription(existingService, serviceRequest);

        // Update highlights
        updateHighlights(existingService, serviceRequest);

        com.mylstech.product.model.Service updatedService = serviceRepository.save(existingService);
        return new ServiceResponse(updatedService);
    }

    @Override
    public void deleteService(Long serviceId) {
        if (!serviceRepository.existsById(serviceId)) {
            throw new RuntimeException("Service not found with id: " + serviceId);
        }
        serviceRepository.deleteById(serviceId);
    }

    private void updateDescription(com.mylstech.product.model.Service service, ServiceRequest request) {
        if (request.getShortDescription() != null || 
            request.getLongDescription1() != null || 
            request.getLongDescription2() != null) {
            
            if (service.getDescription() == null) {
                service.setDescription(new Description());
            }
            
            Description desc = service.getDescription();
            if (request.getShortDescription() != null) {
                desc.setShortDescription(request.getShortDescription());
            }
            if (request.getLongDescription1() != null) {
                desc.setLongDescription1(request.getLongDescription1());
            }
            if (request.getLongDescription2() != null) {
                desc.setLongDescription2(request.getLongDescription2());
            }
        }
    }

    private void updateHighlights(com.mylstech.product.model.Service service, ServiceRequest request) {
        if (request.getHighlights() != null) {
            if (request.getHighlights().isEmpty()) {
                service.clearHighlights();
            } else {
                List<Highlight> newHighlights = request.getHighlights().stream()
                    .map(hr -> {
                        Highlight h = new Highlight();
                        h.setTitle(hr.getTitle());
                        h.setDescription(hr.getDescription());
                        return h;
                    })
                    .toList();
                service.setHighlights(newHighlights);
            }
        }
    }
}
