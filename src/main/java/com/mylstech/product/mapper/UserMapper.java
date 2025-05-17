package com.mylstech.product.mapper;

import com.mylstech.product.dto.request.SignupRequest;
import com.mylstech.product.dto.response.UserResponse;
import com.mylstech.product.model.User;

import java.util.List;

/**
 * Mapper for the User entity and its DTOs
 */
public interface UserMapper {

    /**
     * Convert a User entity to a UserResponse DTO
     *
     * @param user the entity to convert
     * @return the DTO
     */
    UserResponse toDto(User user);

    /**
     * Convert a SignupRequest DTO to a User entity
     *
     * @param signupRequest the DTO to convert
     * @return the entity
     */
    User toEntity(SignupRequest signupRequest);

    /**
     * Convert a list of User entities to a list of UserResponse DTOs
     *
     * @param users the list of entities
     * @return the list of DTOs
     */
    List<UserResponse> toDtoList(List<User> users);
}