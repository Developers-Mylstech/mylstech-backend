package com.mylstech.product.controller;

import com.mylstech.product.dto.response.ImageResponse;
import com.mylstech.product.service.FileStorageService;
import com.mylstech.product.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload an image file", description = "Uploads an image file and returns the image details")
    public ResponseEntity<ImageResponse> uploadImage(
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {

        ImageResponse response = imageService.uploadImage ( file );
        return new ResponseEntity<> ( response, HttpStatus.CREATED );
    }

    @GetMapping
    public ResponseEntity<List<ImageResponse>> getAllImages() {
        List<ImageResponse> images = imageService.getAllImages ( );
        return ResponseEntity.ok ( images );
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ImageResponse> getImageById(@PathVariable Long imageId) {
        return imageService.getImageById ( imageId )
                .map ( ResponseEntity::ok )
                .orElse ( ResponseEntity.notFound ( ).build ( ) );
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage ( imageId );
        return ResponseEntity.noContent ( ).build ( );
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.getFilePath ( fileName );
            Resource resource = new UrlResource ( filePath.toUri ( ) );

            if ( resource.exists ( ) ) {
                String contentType = determineContentType ( fileName );

                return ResponseEntity.ok ( )
                        .contentType ( MediaType.parseMediaType ( contentType ) )
                        .header ( HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename ( ) + "\"" )
                        .body ( resource );
            } else {
                return ResponseEntity.notFound ( ).build ( );
            }
        }
        catch ( IOException ex ) {
            return ResponseEntity.internalServerError ( ).build ( );
        }
    }

    private String determineContentType(String fileName) {
        if ( fileName.endsWith ( ".jpg" ) || fileName.endsWith ( ".jpeg" ) ) {
            return "image/jpeg";
        } else if ( fileName.endsWith ( ".png" ) ) {
            return "image/png";
        } else if ( fileName.endsWith ( ".gif" ) ) {
            return "image/gif";
        } else if ( fileName.endsWith ( ".pdf" ) ) {
            return "application/pdf";
        } else {
            return "application/octet-stream";
        }
    }
}
