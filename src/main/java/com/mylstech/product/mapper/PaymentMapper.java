package com.mylstech.product.mapper;

import com.mylstech.product.dto.request.PaymentRequest;
import com.mylstech.product.dto.response.PaymentResponse;
import com.mylstech.product.model.Payment;
import com.mylstech.product.model.Subscription;

import java.util.List;

public interface PaymentMapper {
    PaymentResponse toDto(Payment payment);
    
    Payment toEntity(PaymentRequest paymentRequest, Subscription subscription);
    
    List<PaymentResponse> toDtoList(List<Payment> payments);
}