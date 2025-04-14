package com.mylstech.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    private Integer userId;
    private Long planId;
}
