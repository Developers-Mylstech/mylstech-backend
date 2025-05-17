package com.mylstech.product.mapper;

import com.mylstech.product.dto.request.ServiceRequest;
import com.mylstech.product.dto.response.ServiceResponse;
import com.mylstech.product.model.Service;

/**
 * Mapper for the Service entity and its DTOs
 */
public interface ServiceMapper {

    /**
     * Convert a Service entity to a ServiceResponse DTO
     *
     * @param service the entity to convert
     * @return the DTO
     */
    ServiceResponse toDto(Service service);

    /**
     * Convert a ServiceRequest DTO to a Service entity
     *
     * @param serviceRequest the DTO to convert
     * @return the entity
     */
    Service toEntity(ServiceRequest serviceRequest);

    /**
     * Update a Service entity with data from a ServiceRequest DTO
     *
     * @param service        the entity to update
     * @param serviceRequest the DTO with updated data
     * @return the updated entity
     */
    Service updateEntityFromDto(Service service, ServiceRequest serviceRequest);
}
