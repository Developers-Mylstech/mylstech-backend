package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.response.ImageResponse;
import com.mylstech.product.mapper.ImageMapper;
import com.mylstech.product.model.ImageEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageMapperImpl implements ImageMapper {

    @Override
    public ImageResponse toDto(ImageEntity imageEntity) {
        if ( imageEntity == null ) {
            return null;
        }

        return ImageResponse.builder ( )
                .imageId ( imageEntity.getImageId ( ) )
                .imageUrl ( imageEntity.getImageUrl ( ) )
                .createdAt ( imageEntity.getCreatedAt ( ) )
                .build ( );
    }

    @Override
    public List<ImageResponse> toDtoList(List<ImageEntity> imageEntities) {
        if ( imageEntities == null ) {
            return List.of ( );
        }

        return imageEntities.stream ( )
                .map ( this::toDto )
                .toList ( );
    }
}
