package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.PaymentRequest;
import com.mylstech.product.dto.response.PaymentResponse;
import com.mylstech.product.mapper.PaymentMapper;
import com.mylstech.product.model.Payment;
import com.mylstech.product.model.Subscription;
import com.mylstech.product.util.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponse toDto(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        return PaymentResponse.builder ( )
                .paymentId ( payment.getPaymentId ( ) )
                .subscriptionId ( payment.getSubscription ( ).getSubscriptionId ( ) )
                .amount ( payment.getAmount ( ) )
                .status ( payment.getStatus ( ) )
                .paymentMethod ( payment.getPaymentMethod ( ) )
                .transactionId ( payment.getTransactionId ( ) )
                .paymentDetails ( payment.getPaymentDetails ( ) )
                .failureReason ( payment.getFailureReason ( ) )
                .createdAt ( payment.getCreatedAt ( ) )
                .updatedAt ( payment.getUpdatedAt ( ) )
                .build ( );
    }

    @Override
    public Payment toEntity(PaymentRequest paymentRequest, Subscription subscription) {
        if ( paymentRequest == null ) {
            return null;
        }

        Payment payment = new Payment ( );
        payment.setSubscription ( subscription );
        // Amount will be set by the service based on the subscription's plan
        payment.setStatus ( PaymentStatus.PENDING ); // Default status
        payment.setPaymentMethod ( paymentRequest.getPaymentMethod ( ) );
        payment.setTransactionId ( paymentRequest.getTransactionId ( ) );
        payment.setPaymentDetails ( paymentRequest.getPaymentDetails ( ) );

        return payment;
    }

    @Override
    public List<PaymentResponse> toDtoList(List<Payment> payments) {
        if ( payments == null ) {
            return List.of ( );
        }

        return payments.stream ( )
                .map ( this::toDto )
                .toList ( );
    }
}