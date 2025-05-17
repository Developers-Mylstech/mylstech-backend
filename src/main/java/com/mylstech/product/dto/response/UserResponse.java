package com.mylstech.product.dto.response;

import com.mylstech.product.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private Role role;
    private boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}