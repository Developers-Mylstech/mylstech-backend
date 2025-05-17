package com.mylstech.product.service;

import com.mylstech.product.dto.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ImageService {
    ImageResponse uploadImage(MultipartFile file);

    List<ImageResponse> getAllImages();

    Optional<ImageResponse> getImageById(Long imageId);

    void deleteImage(Long imageId);
}
