package com.mylstech.product.controller;

import com.mylstech.product.dto.request.ContactRequest;
import com.mylstech.product.dto.response.ContactResponse;
import com.mylstech.product.service.ContactService;
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
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "Company contact information API")
public class ContactController {
    private final ContactService contactService;
    
    @GetMapping
    @Operation(
            summary = "Get contact information", 
            description = "Returns the company contact information"
    )
    @ApiResponse(responseCode = "200", description = "Contact information retrieved successfully")
    public ResponseEntity<ContactResponse> getContactInfo() {
        return ResponseEntity.ok(contactService.getContactInfo());
    }
    
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update contact information", 
            description = "Updates the company contact information. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact information updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<ContactResponse> updateContactInfo(
            @Parameter(description = "Updated contact information", required = true)
            @Valid @RequestBody ContactRequest request) {
        return ResponseEntity.ok(contactService.updateContactInfo(request));
    }
}