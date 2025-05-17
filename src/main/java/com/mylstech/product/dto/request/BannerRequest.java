package com.mylstech.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerRequest {

    private String title;

    private String description;

    private Long imageId;

    private Boolean active;
}