package com.mylstech.product.service;

import com.mylstech.product.dto.request.BannerRequest;
import com.mylstech.product.dto.response.BannerResponse;

import java.util.List;
import java.util.Optional;

public interface BannerService {
    BannerResponse addBanner(BannerRequest request);

    List<BannerResponse> getAllBanners();

    List<BannerResponse> getActiveBanners();

    Optional<BannerResponse> getBannerById(Long bannerId);

    BannerResponse updateBanner(Long bannerId, BannerRequest request);

    void deleteBanner(Long bannerId);

    BannerResponse deleteBannerImage(Long bannerId);
}