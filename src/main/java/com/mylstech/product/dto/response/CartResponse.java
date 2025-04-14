package com.mylstech.product.dto.response;

import com.mylstech.product.model.Cart;
import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private Long cartId;
    private Integer userId;
    private List<PlanResponse> plans;

    public CartResponse(Cart cart) {
        this.cartId = cart.getCartId ();
        this.userId = cart.getUserId ();
        this.plans = cart.getPlans ().stream ().map ( PlanResponse::new ).toList ();
    }
}
