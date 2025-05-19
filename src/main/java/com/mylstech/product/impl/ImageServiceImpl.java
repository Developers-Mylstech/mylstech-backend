package com.mylstech.product.impl;

import com.mylstech.product.config.FileStorageProperties;
import com.mylstech.product.dto.response.ImageResponse;
import com.mylstech.product.exception.ResourceInUseException;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.mapper.ImageMapper;
import com.mylstech.product.model.ImageEntity;
import com.mylstech.product.repository.*;
import com.mylstech.product.service.FileStorageService;
import com.mylstech.product.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final FileStorageService fileStorageService;
    private final ImageMapper imageMapper;
    private final FileStorageProperties fileStorageProperties;
    private final ServiceRepository serviceRepository;
    private final PlanRepository planRepository;
    private final BannerRepository bannerRepository;
    private final ClientRepository clientRepository;

    @Override
    public ImageResponse uploadImage(MultipartFile file) {
        // Store the file
        String fileName = fileStorageService.storeFile ( file );

        // Generate the download URL
        String fileDownloadUri = generateFileUrl ( fileName );

        // Create and save the image entity
        ImageEntity imageEntity = ImageEntity.builder ( )
                .imageUrl ( fileDownloadUri )
                .originalFilename ( file.getOriginalFilename ( ) )
                .contentType ( file.getContentType ( ) )
                .fileSize ( file.getSize ( ) )
                .build ( );

        ImageEntity savedImage = imageRepository.save ( imageEntity );

        return imageMapper.toDto ( savedImage );
    }

    @Override
    public List<ImageResponse> getAllImages() {
        List<ImageEntity> images = imageRepository.findAll ( );
        return imageMapper.toDtoList ( images );
    }

    @Override
    public Optional<ImageResponse> getImageById(Long imageId) {
        return imageRepository.findById ( imageId )
                .map ( imageMapper::toDto );
    }

    @Override
    public void deleteImage(Long imageId) {
        // First, check if the image exists
        ImageEntity imageEntity = imageRepository.findById ( imageId )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Image", "id", imageId ) );

        // Check if the image is associated with any Service entities
        boolean isUsedByService = checkImageUsedByService ( imageEntity.getImageUrl ( ) );

        // Check if the image is associated with any Plan entities
        boolean isUsedByPlan = checkImageUsedByPlan ( imageEntity.getImageUrl ( ) );

        // Check if the image is associated with any Banner entities
        boolean isUsedByBanner = checkImageUsedByBanner ( imageEntity.getImageUrl ( ) );

        // Check if the image is associated with any Client entities
        boolean isUsedByClient = checkImageUsedByClient ( imageEntity.getImageUrl ( ) );

        if ( isUsedByService || isUsedByPlan || isUsedByBanner || isUsedByClient ) {
            String usedBy = (isUsedByService ? "Service" : "") +
                    (isUsedByService && (isUsedByPlan || isUsedByBanner || isUsedByClient) ? ", " : "") +
                    (isUsedByPlan ? "Plan" : "") +
                    ((isUsedByService || isUsedByPlan) && (isUsedByBanner || isUsedByClient) ? ", " : "") +
                    (isUsedByBanner ? "Banner" : "") +
                    ((isUsedByService || isUsedByPlan || isUsedByBanner) && isUsedByClient ? ", " : "") +
                    (isUsedByClient ? "Client" : "");
            throw new ResourceInUseException ( "Image", usedBy );
        }

        // Delete the image file from storage
        try {
            String fileName = extractFileNameFromUrl ( imageEntity.getImageUrl ( ) );
            deletePhysicalFile ( fileName );
        }
        catch ( Exception e ) {
            log.error ( "Error deleting image file: {}", e.getMessage ( ) );
            // Continue with deletion from database even if file deletion fails
        }

        // Delete the image from the database
        imageRepository.delete ( imageEntity );
    }

    private String generateFileUrl(String fileName) {
        // If baseUrl is configured, use it; otherwise, build from the current request
        if ( fileStorageProperties.getBaseUrl ( ) != null && ! fileStorageProperties.getBaseUrl ( ).isEmpty ( ) ) {
            // Use direct access to the uploaded file via the static resource handler
            return fileStorageProperties.getBaseUrl ( ) + "/uploads/images/" + fileName;
        } else {
            return ServletUriComponentsBuilder.fromCurrentContextPath ( )
                    .path ( "/uploads/images/" )
                    .path ( fileName )
                    .toUriString ( );
        }
    }

    private String extractFileNameFromUrl(String imageUrl) {
        return imageUrl.substring ( imageUrl.lastIndexOf ( "/" ) + 1 );
    }

    private void deletePhysicalFile(String fileName) {
        try {
            Path filePath = fileStorageService.getFilePath ( fileName );
            Files.deleteIfExists ( filePath );
        }
        catch ( IOException e ) {
            throw new RuntimeException ( "Failed to delete image file: " + fileName, e );
        }
    }

    private boolean checkImageUsedByService(String imageUrl) {
        // This would require injecting ServiceRepository
        // Return true if any service uses this image URL
        return serviceRepository.existsByImageImageUrl ( imageUrl );
    }

    private boolean checkImageUsedByPlan(String imageUrl) {
        // This would require injecting PlanRepository
        // Return true if any plan uses this image URL
        return planRepository.existsByImageImageUrl ( imageUrl );
    }

    private boolean checkImageUsedByBanner(String imageUrl) {
        return bannerRepository.existsByImageImageUrl ( imageUrl );
    }

    private boolean checkImageUsedByClient(String imageUrl) {
        return clientRepository.existsByImageImageUrl ( imageUrl );
    }
}
