package com.mylstech.product.controller;

import com.mylstech.product.dto.request.BannerRequest;
import com.mylstech.product.dto.response.BannerResponse;
import com.mylstech.product.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
@Tag(name = "Banner", description = "Banner management APIs")
public class BannerController {
    private final BannerService bannerService;

    @PostMapping
    @Operation(
            summary = "Add a new banner", 
            description = "Creates a new banner and returns the banner details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Banner successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<BannerResponse> addBanner(
            @Parameter(description = "Banner details to create", required = true)
            @Valid @RequestBody BannerRequest request) {
        return new ResponseEntity<>(bannerService.addBanner(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Get all banners", 
            description = "Returns a list of all banners"
    )
    @ApiResponse(responseCode = "200", description = "List of banners retrieved successfully")
    public ResponseEntity<List<BannerResponse>> getAllBanners() {
        return ResponseEntity.ok(bannerService.getAllBanners());
    }

    @GetMapping("/active")
    @Operation(summary = "Get active banners", description = "Returns a list of all active banners")
    public ResponseEntity<List<BannerResponse>> getActiveBanners() {
        return ResponseEntity.ok ( bannerService.getActiveBanners ( ) );
    }

    @GetMapping("/{bannerId}")
    @Operation(
            summary = "Get banner by ID", 
            description = "Returns a banner by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Banner retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Banner not found")
    })
    public ResponseEntity<BannerResponse> getBannerById(
            @Parameter(description = "ID of the banner to retrieve", required = true)
            @PathVariable Long bannerId) {
        return bannerService.getBannerById(bannerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{bannerId}")
    @Operation(
            summary = "Update a banner", 
            description = "Updates a banner and returns the updated banner details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Banner updated successfully"),
            @ApiResponse(responseCode = "404", description = "Banner not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<BannerResponse> updateBanner(
            @Parameter(description = "ID of the banner to update", required = true)
            @PathVariable Long bannerId,
            @Parameter(description = "Updated banner details", required = true)
            @Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok(bannerService.updateBanner(bannerId, request));
    }

    @DeleteMapping("/{bannerId}")
    @Operation(
            summary = "Delete a banner", 
            description = "Deletes a banner by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Banner deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Banner not found")
    })
    public ResponseEntity<Void> deleteBanner(
            @Parameter(description = "ID of the banner to delete", required = true)
            @PathVariable Long bannerId) {
        bannerService.deleteBanner(bannerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bannerId}/image")
    @Operation(summary = "Delete banner image", description = "Removes the image from a banner")
    public ResponseEntity<BannerResponse> deleteBannerImage(@PathVariable Long bannerId) {
        return ResponseEntity.ok ( bannerService.deleteBannerImage ( bannerId ) );
    }
}