package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;
import com.mylstech.product.mapper.ServiceMapper;
import com.mylstech.product.model.Service;
import org.springframework.stereotype.Component;

/**
 * Implementation of the ServiceMapper interface
 */
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public ServiceResponse toDto(Service service) {
        if ( service == null ) {
            return null;
        }

        return new ServiceResponse ( service );
    }

    @Override
    public Service toEntity(ServiceRequest serviceRequest) {
        if ( serviceRequest == null ) {
            return null;
        }

        Service service = new Service ( );
        service.setServiceType ( serviceRequest.getServiceType ( ) );
        service.setTitle ( serviceRequest.getTitle ( ) );
        service.setDescription ( serviceRequest.getDescription ( ) );
        service.setLongDescription1 ( serviceRequest.getLongDescription1 ( ) );
        service.setLongDescription2 ( serviceRequest.getLongDescription2 ( ) );

        return service;
    }

    @Override
    public Service updateEntityFromDto(Service service, ServiceRequest serviceRequest) {
        if ( serviceRequest == null ) {
            return service;
        }

        if ( serviceRequest.getServiceType ( ) != null ) {
            service.setServiceType ( serviceRequest.getServiceType ( ) );
        }

        if ( serviceRequest.getTitle ( ) != null ) {
            service.setTitle ( serviceRequest.getTitle ( ) );
        }

        if ( serviceRequest.getDescription ( ) != null ) {
            service.setDescription ( serviceRequest.getDescription ( ) );
        }

        if ( serviceRequest.getLongDescription1 ( ) != null ) {
            service.setLongDescription1 ( serviceRequest.getLongDescription1 ( ) );
        }

        if ( serviceRequest.getLongDescription2 ( ) != null ) {
            service.setLongDescription2 ( serviceRequest.getLongDescription2 ( ) );
        }

        return service;
    }
}
