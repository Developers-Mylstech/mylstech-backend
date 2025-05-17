package com.mylstech.product.mapper;

import com.mylstech.product.dto.request.PlanRequest;
import com.mylstech.product.dto.response.PlanResponse;
import com.mylstech.product.model.Plan;

/**
 * Mapper for the Plan entity and its DTOs
 */
public interface PlanMapper {

    /**
     * Convert a Plan entity to a PlanResponse DTO
     *
     * @param plan the entity to convert
     * @return the DTO
     */
    PlanResponse toDto(Plan plan);

    /**
     * Convert a PlanRequest DTO to a Plan entity
     *
     * @param planRequest the DTO to convert
     * @return the entity
     */
    Plan toEntity(PlanRequest planRequest);

    /**
     * Update a Plan entity with data from a PlanRequest DTO
     *
     * @param plan        the entity to update
     * @param planRequest the DTO with updated data
     * @return the updated entity
     */
    Plan updateEntityFromDto(Plan plan, PlanRequest planRequest);
}
