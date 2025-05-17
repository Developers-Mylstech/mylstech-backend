package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.BannerRequest;
import com.mylstech.product.dto.response.BannerResponse;
import com.mylstech.product.mapper.BannerMapper;
import com.mylstech.product.model.Banner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BannerMapperImpl implements BannerMapper {

    @Override
    public BannerResponse toDto(Banner banner) {
        if ( banner == null ) {
            return null;
        }

        return new BannerResponse ( banner );
    }

    @Override
    public Banner toEntity(BannerRequest bannerRequest) {
        if ( bannerRequest == null ) {
            return null;
        }
        Banner banner = new Banner ( );
        banner.setTitle ( bannerRequest.getTitle ( ) );
        banner.setDescription ( bannerRequest.getDescription ( ) );
        banner.setActive ( bannerRequest.getActive ( ) );
        return banner;
    }

    @Override
    public Banner updateEntityFromDto(Banner banner, BannerRequest bannerRequest) {
        if ( bannerRequest == null ) {
            return banner;
        }

        if ( bannerRequest.getTitle ( ) != null ) {
            banner.setTitle ( bannerRequest.getTitle ( ) );
        }

        if ( bannerRequest.getDescription ( ) != null ) {
            banner.setDescription ( bannerRequest.getDescription ( ) );
        }

        if ( bannerRequest.getActive ( ) != null ) {
            banner.setActive ( bannerRequest.getActive ( ) );
        }

        return banner;
    }

    @Override
    public List<BannerResponse> toDtoList(List<Banner> banners) {
        if ( banners == null ) {
            return List.of ( );
        }

        return banners.stream ( )
                .map ( this::toDto )
                .toList ( );
    }
}