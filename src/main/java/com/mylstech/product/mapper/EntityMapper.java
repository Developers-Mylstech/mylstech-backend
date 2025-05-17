package com.mylstech.product.mapper;

import java.util.List;

/**
 * Generic interface for entity to DTO mapping and vice versa
 *
 * @param <D> - DTO type
 * @param <E> - Entity type
 */
public interface EntityMapper<D, E> {

    /**
     * Convert entity to DTO
     *
     * @param entity the entity to convert
     * @return the DTO
     */
    D toDto(E entity);

    /**
     * Convert DTO to entity
     *
     * @param dto the DTO to convert
     * @return the entity
     */
    E toEntity(D dto);

    /**
     * Convert a list of entities to a list of DTOs
     *
     * @param entities the list of entities
     * @return the list of DTOs
     */
    default List<D> toDtoList(List<E> entities) {
        return entities.stream ( )
                .map ( this::toDto )
                .toList ( );
    }

    /**
     * Convert a list of DTOs to a list of entities
     *
     * @param dtos the list of DTOs
     * @return the list of entities
     */
    default List<E> toEntityList(List<D> dtos) {
        return dtos.stream ( )
                .map ( this::toEntity )
                .toList ( );
    }
}
