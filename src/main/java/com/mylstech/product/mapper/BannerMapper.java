package com.mylstech.product.mapper;

import com.mylstech.product.dto.request.BannerRequest;
import com.mylstech.product.dto.response.BannerResponse;
import com.mylstech.product.model.Banner;

import java.util.List;

public interface BannerMapper {
    BannerResponse toDto(Banner banner);

    Banner toEntity(BannerRequest bannerRequest);

    Banner updateEntityFromDto(Banner banner, BannerRequest bannerRequest);

    List<BannerResponse> toDtoList(List<Banner> banners);
}