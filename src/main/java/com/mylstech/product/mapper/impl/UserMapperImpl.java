package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.SignupRequest;
import com.mylstech.product.dto.response.UserResponse;
import com.mylstech.product.mapper.UserMapper;
import com.mylstech.product.model.User;
import com.mylstech.product.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of the UserMapper interface
 */
@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse toDto(User user) {
        if ( user == null ) {
            return null;
        }

        return UserResponse.builder ( )
                .userId ( user.getUserId ( ) )
                .fullName ( user.getFullName ( ) )
                .email ( user.getEmail ( ) )
                .role ( user.getRole ( ) )
                .verified ( user.isVerified ( ) )
                .createdAt ( user.getCreatedAt ( ) )
                .updatedAt ( user.getUpdatedAt ( ) )
                .build ( );
    }

    @Override
    public User toEntity(SignupRequest signupRequest) {
        if ( signupRequest == null ) {
            return null;
        }

        User user = new User ( );
        user.setFullName ( signupRequest.getFullName ( ) );
        user.setEmail ( signupRequest.getEmail ( ) );
        user.setPassword ( passwordEncoder.encode ( signupRequest.getPassword ( ) ) );
        user.setRole ( Role.CUSTOMER ); // Default role
        // For simplicity, set as verified

        return user;
    }

    @Override
    public List<UserResponse> toDtoList(List<User> users) {
        if ( users == null ) {
            return List.of ( );
        }

        return users.stream ( )
                .map ( this::toDto )
                .toList ( );
    }
}