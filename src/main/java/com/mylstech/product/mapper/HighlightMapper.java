package com.mylstech.product.mapper;

import com.mylstech.product.dto.request.HighlightRequest;
import com.mylstech.product.model.Highlight;

/**
 * Mapper for the Highlight entity and its DTOs
 */
public interface HighlightMapper {

    /**
     * Convert a Highlight entity to a HighlightRequest DTO
     *
     * @param highlight the entity to convert
     * @return the DTO
     */
    HighlightRequest toDto(Highlight highlight);

    /**
     * Convert a HighlightRequest DTO to a Highlight entity
     *
     * @param highlightRequest the DTO to convert
     * @return the entity
     */
    Highlight toEntity(HighlightRequest highlightRequest);
}
