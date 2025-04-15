package com.mylstech.product.controller;

import com.mylstech.product.dto.request.CartRequest;
import com.mylstech.product.dto.response.CartResponse;
import com.mylstech.product.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin("**")
public class CartController {
    private final CartService cartService;

    @PostMapping("/getCart")
    public ResponseEntity<CartResponse> addCart(@RequestBody CartRequest request) {
        return new ResponseEntity<> ( cartService.addCart(request), HttpStatus.OK);
    }

    @PutMapping("/addPlan/{cartId}")
    public  ResponseEntity<CartResponse> addPlan(@PathVariable Long cartId, @RequestBody CartRequest request) {
        return new ResponseEntity<> ( cartService.addPlan(cartId, request), HttpStatus.OK);
    }
    @GetMapping("/getCart/{cartId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long cartId) {
        return new ResponseEntity<> ( cartService.getCart(cartId), HttpStatus.OK);
    }

}
