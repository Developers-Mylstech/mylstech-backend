package com.mylstech.product.dto.response;

import com.mylstech.product.model.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponse {
    private Long bannerId;
    private String title;
    private String description;
    private ImageResponse image;
    private Boolean active;
    private LocalDateTime createdAt;

    public BannerResponse(Banner banner) {
        this.bannerId = banner.getBannerId ( );
        this.title = banner.getTitle ( );
        this.description = banner.getDescription ( );
        this.active = banner.getActive ( );
        this.createdAt = banner.getCreatedAt ( );

        if ( banner.getImage ( ) != null ) {
            this.image = ImageResponse.builder ( )
                    .imageId ( banner.getImage ( ).getImageId ( ) )
                    .imageUrl ( banner.getImage ( ).getImageUrl ( ) )
                    .createdAt ( banner.getImage ( ).getCreatedAt ( ) )
                    .build ( );
        }
    }
}