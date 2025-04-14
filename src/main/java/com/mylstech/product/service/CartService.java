package com.mylstech.product.service;

import com.mylstech.product.dto.request.CartRequest;
import com.mylstech.product.dto.response.CartResponse;

public interface CartService {

    CartResponse addCart(CartRequest request);

    CartResponse addPlan(Long cartId, CartRequest request);

    CartResponse getCart(Long cartId);
}
