package com.mylstech.product.impl;

import com.mylstech.product.dto.request.CartRequest;
import com.mylstech.product.dto.response.CartResponse;
import com.mylstech.product.model.Cart;
import com.mylstech.product.model.Plan;
import com.mylstech.product.repository.CartRepository;
import com.mylstech.product.repository.PlanRepository;
import com.mylstech.product.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final PlanRepository planRepository;

    @Override
    public CartResponse addCart(CartRequest request) {
        Cart cart = new Cart ( );
        cart.setUserId ( request.getUserId () );
        Cart save = cartRepository.save ( cart );
        return new CartResponse (save);
    }

    @Override
    public CartResponse addPlan(Long cartId, CartRequest request) {
        Cart cart = cartRepository.findById ( cartId ).orElseThrow ( () -> new RuntimeException ( "cart not found" ) );
        if (request.getPlanId () != null ) {
            Plan plan = planRepository.findById ( request.getPlanId ( ) ).orElseThrow ( () -> new RuntimeException ( "plan not found" ) );
            cart.getPlans ().add ( plan );
        }
        Cart save = cartRepository.save ( cart );
        return new CartResponse ( save );
    }

    @Override
    public CartResponse getCart(Long cartId) {
        return new CartResponse ( cartRepository.findById ( cartId ).orElseThrow (()->new RuntimeException ("cart not found")) );
    }

}
