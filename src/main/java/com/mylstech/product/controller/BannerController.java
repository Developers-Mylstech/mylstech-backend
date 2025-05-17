package com.mylstech.product.controller;

import com.mylstech.product.dto.request.BannerRequest;
import com.mylstech.product.dto.response.BannerResponse;
import com.mylstech.product.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
@Tag(name = "Banner", description = "Banner management APIs")
public class BannerController {
    private final BannerService bannerService;

    @PostMapping
    @Operation(summary = "Add a new banner", description = "Creates a new banner and returns the banner details")
    public ResponseEntity<BannerResponse> addBanner(@Valid @RequestBody BannerRequest request) {
        return new ResponseEntity<> ( bannerService.addBanner ( request ), HttpStatus.CREATED );
    }

    @GetMapping
    @Operation(summary = "Get all banners", description = "Returns a list of all banners")
    public ResponseEntity<List<BannerResponse>> getAllBanners() {
        return ResponseEntity.ok ( bannerService.getAllBanners ( ) );
    }

    @GetMapping("/active")
    @Operation(summary = "Get active banners", description = "Returns a list of all active banners")
    public ResponseEntity<List<BannerResponse>> getActiveBanners() {
        return ResponseEntity.ok ( bannerService.getActiveBanners ( ) );
    }

    @GetMapping("/{bannerId}")
    @Operation(summary = "Get banner by ID", description = "Returns a banner by its ID")
    public ResponseEntity<BannerResponse> getBannerById(@PathVariable Long bannerId) {
        return bannerService.getBannerById ( bannerId )
                .map ( ResponseEntity::ok )
                .orElse ( ResponseEntity.notFound ( ).build ( ) );
    }

    @PutMapping("/{bannerId}")
    @Operation(summary = "Update a banner", description = "Updates a banner and returns the updated banner details")
    public ResponseEntity<BannerResponse> updateBanner(
            @PathVariable Long bannerId,
            @Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok ( bannerService.updateBanner ( bannerId, request ) );
    }

    @DeleteMapping("/{bannerId}")
    @Operation(summary = "Delete a banner", description = "Deletes a banner by its ID")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long bannerId) {
        bannerService.deleteBanner ( bannerId );
        return ResponseEntity.noContent ( ).build ( );
    }

    @DeleteMapping("/{bannerId}/image")
    @Operation(summary = "Delete banner image", description = "Removes the image from a banner")
    public ResponseEntity<BannerResponse> deleteBannerImage(@PathVariable Long bannerId) {
        return ResponseEntity.ok ( bannerService.deleteBannerImage ( bannerId ) );
    }
}