package com.mylstech.product.controller;

import com.mylstech.product.dto.request.AboutUsRequest;
import com.mylstech.product.dto.response.AboutUsResponse;
import com.mylstech.product.service.AboutUsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/about-us")
@RequiredArgsConstructor
@Tag(name = "About Us", description = "Company about us information API")
public class AboutUsController {
    private final AboutUsService aboutUsService;
    
    @GetMapping
    @Operation(
            summary = "Get about us information", 
            description = "Returns the company about us information"
    )
    @ApiResponse(responseCode = "200", description = "About us information retrieved successfully")
    public ResponseEntity<AboutUsResponse> getAboutUsInfo() {
        return ResponseEntity.ok(aboutUsService.getAboutUsInfo());
    }
    
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update about us information", 
            description = "Updates the company about us information. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "About us information updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<AboutUsResponse> updateAboutUsInfo(
            @Parameter(description = "Updated about us information", required = true)
            @Valid @RequestBody AboutUsRequest request) {
        return ResponseEntity.ok(aboutUsService.updateAboutUsInfo(request));
    }
}