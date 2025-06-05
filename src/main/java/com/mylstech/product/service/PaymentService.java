package com.mylstech.product.service;

import com.mylstech.product.dto.request.PaymentRequest;
import com.mylstech.product.dto.response.PaymentResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    /**
     * Process a payment for a subscription
     */
    PaymentResponse processPayment(PaymentRequest request);

    /**
     * Get a payment by ID (admin access)
     */
    Optional<PaymentResponse> getPaymentById(Long paymentId);

    /**
     * Get a payment by ID for the current authenticated user
     */
    Optional<PaymentResponse> getCurrentUserPayment(Long paymentId);

    /**
     * Get all payments (admin access)
     */
    List<PaymentResponse> getAllPayments();

    /**
     * Get all payments for the current authenticated user
     */
    List<PaymentResponse> getCurrentUserPayments();

    /**
     * Get all payments for a specific subscription of the current authenticated user
     */
    List<PaymentResponse> getPaymentsForSubscription(Long subscriptionId);

    /**
     * Get all payments for a customer (admin access)
     */
    List<PaymentResponse> getPaymentsByCustomerId(Long customerId);

    /**
     * Get all payments for a subscription (admin access)
     */
    List<PaymentResponse> getPaymentsBySubscriptionId(Long subscriptionId);

    /**
     * Get all payments with a specific status (admin access)
     */
    List<PaymentResponse> getPaymentsByStatus(String status);

    /**
     * Get total revenue for a period (admin access)
     */
    Double getTotalRevenueForPeriod(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Cancel a payment (admin access)
     */
    PaymentResponse cancelPayment(Long paymentId);

    /**
     * Refund a payment (admin access)
     */
    PaymentResponse refundPayment(Long paymentId);

    /**
     * Process a cash payment for a subscription
     */
    PaymentResponse processCashPayment(PaymentRequest request);

    /**
     * Confirm receipt of a cash payment (admin only)
     */
    PaymentResponse confirmCashPayment(Long paymentId, String receiptDetails);
}
