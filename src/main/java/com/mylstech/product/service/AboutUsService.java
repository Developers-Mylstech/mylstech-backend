package com.mylstech.product.service;

import com.mylstech.product.dto.request.AboutUsRequest;
import com.mylstech.product.dto.response.AboutUsResponse;

public interface AboutUsService {
    AboutUsResponse getAboutUsInfo();
    AboutUsResponse updateAboutUsInfo(AboutUsRequest request);
}