package com.mylstech.product.impl;

import com.mylstech.product.dto.request.BannerRequest;
import com.mylstech.product.dto.response.BannerResponse;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.mapper.BannerMapper;
import com.mylstech.product.model.Banner;
import com.mylstech.product.repository.BannerRepository;
import com.mylstech.product.repository.ImageRepository;
import com.mylstech.product.service.BannerService;
import com.mylstech.product.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public BannerResponse addBanner(BannerRequest request) {
        try {
            Banner banner = bannerMapper.toEntity ( request );

            if ( request.getImageId ( ) != null ) {
                banner.setImage ( imageRepository.findById ( request.getImageId ( ) )
                        .orElseThrow ( () -> new ResourceNotFoundException ( "Image", "id", request.getImageId ( ) ) ) );
            }

            Banner savedBanner = bannerRepository.save ( banner );
            return bannerMapper.toDto ( savedBanner );
        }
        catch ( DataIntegrityViolationException ex ) {
            log.error ( "Error adding banner", ex );
            throw ex;
        }
    }

    @Override
    public List<BannerResponse> getAllBanners() {
        List<Banner> banners = bannerRepository.findAll ( );
        return bannerMapper.toDtoList ( banners );
    }

    @Override
    public List<BannerResponse> getActiveBanners() {
        List<Banner> activeBanners = bannerRepository.findByActiveTrue ( );
        return bannerMapper.toDtoList ( activeBanners );
    }

    @Override
    public Optional<BannerResponse> getBannerById(Long bannerId) {
        return bannerRepository.findById ( bannerId )
                .map ( bannerMapper::toDto );
    }

    @Override
    public BannerResponse updateBanner(Long bannerId, BannerRequest request) {
        Banner banner = bannerRepository.findById ( bannerId )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Banner", "id", bannerId ) );

        Banner updatedBanner = bannerMapper.updateEntityFromDto ( banner, request );

        if ( request.getImageId ( ) != null ) {
            updatedBanner.setImage ( imageRepository.findById ( request.getImageId ( ) )
                    .orElseThrow ( () -> new ResourceNotFoundException ( "Image", "id", request.getImageId ( ) ) ) );
        }

        Banner savedBanner = bannerRepository.save ( updatedBanner );
        return bannerMapper.toDto ( savedBanner );
    }

    @Override
    public void deleteBanner(Long bannerId) {
        if ( ! bannerRepository.existsById ( bannerId ) ) {
            throw new ResourceNotFoundException ( "Banner", "id", bannerId );
        }
        deleteBannerImage ( bannerId );
        bannerRepository.deleteById ( bannerId );
    }

    @Override
    public BannerResponse deleteBannerImage(Long bannerId) {
        Long imageId = null;
        Banner banner = bannerRepository.findById ( bannerId )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Banner", "id", bannerId ) );
        if ( banner.getImage ( ) != null ) {
            imageId = banner.getImage ( ).getImageId ( );
        }
        banner.setImage ( null );
        Banner savedBanner = bannerRepository.save ( banner );
        if ( imageId != null ) {
            imageService.deleteImage ( imageId );
        }
        return bannerMapper.toDto ( savedBanner );
    }
}