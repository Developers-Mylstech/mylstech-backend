package com.mylstech.product.impl;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;
import com.mylstech.product.exception.DuplicateEntryException;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.mapper.ServiceMapper;
import com.mylstech.product.repository.ImageRepository;
import com.mylstech.product.repository.ServiceRepository;
import com.mylstech.product.service.ImageService;
import com.mylstech.product.service.ServicesService;
import com.mylstech.product.util.ServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public ServiceResponse addService(ServiceRequest serviceRequest) {
        try {
            com.mylstech.product.model.Service service = serviceMapper.toEntity ( serviceRequest );
            if ( serviceRequest.getImageId ( ) != null ) {
                service.setImage ( imageRepository.findById ( serviceRequest.getImageId ( ) )
                        .orElseThrow ( () -> new ResourceNotFoundException ( "image", "imageId", serviceRequest.getImageId ( ) ) ) );
            }
            com.mylstech.product.model.Service savedService = serviceRepository.save ( service );
            return serviceMapper.toDto ( savedService );
        }
        catch ( DataIntegrityViolationException ex ) {
            if ( ex.getMessage ( ).contains ( "Duplicate entry" ) ) {
                throw new DuplicateEntryException ( "Service", "unique constraint for image" );
            }
            throw ex;
        }
    }

    @Override
    public List<ServiceResponse> getAllServices() {
        List<com.mylstech.product.model.Service> all = serviceRepository.findAll ( );
        return all.stream ( ).map ( serviceMapper::toDto ).toList ( );
    }

    @Override
    public List<ServiceResponse> getByServiceType(ServiceType serviceType) {
        List<com.mylstech.product.model.Service> all = serviceRepository.findByServiceType ( serviceType );
        return all.stream ( ).map ( serviceMapper::toDto ).toList ( );
    }

    @Override
    public ServiceResponse updateService(Long serviceId, ServiceRequest serviceRequest) {
        com.mylstech.product.model.Service existingService = serviceRepository.findById ( serviceId )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Service not found with id: " + serviceId ) );

        // Update the entity using the mapper
        com.mylstech.product.model.Service updatedService = serviceMapper.updateEntityFromDto ( existingService, serviceRequest );
        if ( serviceRequest.getImageId ( ) != null ) {
            updatedService.setImage ( imageRepository.findById ( serviceRequest.getImageId ( ) )
                    .orElseThrow ( () -> new ResourceNotFoundException ( "service", "image", serviceRequest.getImageId ( ) ) ) );
        }

        // Save and return the updated service
        com.mylstech.product.model.Service savedService = serviceRepository.save ( updatedService );
        return serviceMapper.toDto ( savedService );
    }

    @Override
    public void deleteService(Long serviceId) {
        if ( ! serviceRepository.existsById ( serviceId ) ) {
            throw new ResourceNotFoundException ( "Service not found with id: " + serviceId );
        }

        // Delete the service's image first
        deleteServiceImage ( serviceId );

        // Then delete the service
        serviceRepository.deleteById ( serviceId );
    }

    @Override
    public ServiceResponse deleteServiceImage(Long serviceId) {
        com.mylstech.product.model.Service service = serviceRepository.findById ( serviceId )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Service not found with id: " + serviceId ) );

        // Store the image ID before removing the association
        Long imageId = null;
        if ( service.getImage ( ) != null ) {
            imageId = service.getImage ( ).getImageId ( );
        }

        // Remove the image association
        service.setImage ( null );

        // Save the updated service
        com.mylstech.product.model.Service savedService = serviceRepository.save ( service );

        // Delete the image if it exists
        if ( imageId != null ) {
            imageService.deleteImage ( imageId );
        }

        return serviceMapper.toDto ( savedService );
    }
}
