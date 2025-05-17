package com.mylstech.product.mapper;

import com.mylstech.product.dto.response.ImageResponse;
import com.mylstech.product.model.ImageEntity;

import java.util.List;

public interface ImageMapper {
    ImageResponse toDto(ImageEntity imageEntity);

    List<ImageResponse> toDtoList(List<ImageEntity> imageEntities);
}
