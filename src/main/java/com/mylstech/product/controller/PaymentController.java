package com.mylstech.product.controller;

import com.mylstech.product.dto.request.PaymentRequest;
import com.mylstech.product.dto.response.PaymentResponse;
import com.mylstech.product.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment management APIs for processing and tracking subscription payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @Operation(
            summary = "Make a payment",
            description = "Creates a new payment for a subscription. Supports various payment methods including credit card, debit card, PayPal, and others."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Subscription not found"),
            @ApiResponse(responseCode = "403", description = "Not authorized to make payment for this subscription")
    })
    public ResponseEntity<PaymentResponse> makePayment(
            @Parameter(description = "Payment details including subscription ID and payment method", required = true)
            @Valid @RequestBody PaymentRequest request) {
        return new ResponseEntity<>(paymentService.processPayment(request), HttpStatus.CREATED);
    }

    @GetMapping("/my-payments")
    @Operation(
            summary = "Get current user's payments",
            description = "Returns a list of all payments made by the currently authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully")
    public ResponseEntity<List<PaymentResponse>> getCurrentUserPayments() {
        return ResponseEntity.ok(paymentService.getCurrentUserPayments());
    }

    @GetMapping("/my-subscriptions/{subscriptionId}/payments")
    @Operation(
            summary = "Get payments for a subscription",
            description = "Returns a list of payments for a specific subscription owned by the current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found or not owned by current user")
    })
    public ResponseEntity<List<PaymentResponse>> getPaymentsForSubscription(
            @Parameter(description = "ID of the subscription to get payments for", required = true)
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(paymentService.getPaymentsForSubscription(subscriptionId));
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get payment by ID",
            description = "Returns a payment by its ID. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<PaymentResponse> getPaymentById(
            @Parameter(description = "ID of the payment to retrieve", required = true)
            @PathVariable Long paymentId) {
        return paymentService.getPaymentById(paymentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my-payments/{paymentId}")
    @Operation(
            summary = "Get current user's payment by ID",
            description = "Returns a specific payment by ID for the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found or not owned by current user")
    })
    public ResponseEntity<PaymentResponse> getCurrentUserPayment(
            @Parameter(description = "ID of the payment to retrieve", required = true)
            @PathVariable Long paymentId) {
        return paymentService.getCurrentUserPayment(paymentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all payments",
            description = "Returns a list of all payments in the system. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get payments by customer ID",
            description = "Returns a list of payments for a specific customer. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<List<PaymentResponse>> getPaymentsByCustomerId(
            @Parameter(description = "ID of the customer to get payments for", required = true)
            @PathVariable Long customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomerId(customerId));
    }

    @GetMapping("/subscription/{subscriptionId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get payments by subscription ID",
            description = "Returns a list of payments for a specific subscription. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<List<PaymentResponse>> getPaymentsBySubscriptionId(
            @Parameter(description = "ID of the subscription to get payments for", required = true)
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(paymentService.getPaymentsBySubscriptionId(subscriptionId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get payments by status",
            description = "Returns a list of payments with a specific status (e.g., PENDING, COMPLETED, FAILED). Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(
            @Parameter(description = "Payment status to filter by (e.g., PENDING, COMPLETED, FAILED)", required = true)
            @PathVariable String status) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get total revenue for a period",
            description = "Returns the total revenue for a specific time period. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revenue calculated successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource")
    })
    public ResponseEntity<Double> getTotalRevenueForPeriod(
            @Parameter(description = "Start date for the period (format: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for the period (format: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getTotalRevenueForPeriod(startDate, endDate));
    }

    @PutMapping("/{paymentId}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Cancel a payment",
            description = "Cancels a payment by its ID. This will update the payment status to CANCELLED. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource"),
            @ApiResponse(responseCode = "400", description = "Payment cannot be cancelled (e.g., already completed)")
    })
    public ResponseEntity<PaymentResponse> cancelPayment(
            @Parameter(description = "ID of the payment to cancel", required = true)
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.cancelPayment(paymentId));
    }

    @PutMapping("/{paymentId}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Refund a payment",
            description = "Refunds a payment by its ID. This will update the payment status to REFUNDED. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment refunded successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource"),
            @ApiResponse(responseCode = "400", description = "Payment cannot be refunded (e.g., not completed)")
    })
    public ResponseEntity<PaymentResponse> refundPayment(
            @Parameter(description = "ID of the payment to refund", required = true)
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.refundPayment(paymentId));
    }

    @PostMapping("/cash")
    @Operation(
            summary = "Make a cash payment",
            description = "Creates a new cash payment for a subscription. The payment will be in PENDING status until confirmed by an admin."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cash payment successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Subscription not found"),
            @ApiResponse(responseCode = "403", description = "Not authorized to make payment for this subscription")
    })
    public ResponseEntity<PaymentResponse> makeCashPayment(
            @Parameter(description = "Cash payment details including subscription ID", required = true)
            @Valid @RequestBody PaymentRequest request) {
        return new ResponseEntity<>(paymentService.processCashPayment(request), HttpStatus.CREATED);
    }

    @PutMapping("/{paymentId}/confirm-cash")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Confirm cash payment",
            description = "Confirms receipt of a cash payment by updating its status to COMPLETED. Restricted to admin users only."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cash payment successfully confirmed"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource"),
            @ApiResponse(responseCode = "400", description = "Payment is not a cash payment or cannot be confirmed")
    })
    public ResponseEntity<PaymentResponse> confirmCashPayment(
            @Parameter(description = "ID of the cash payment to confirm", required = true)
            @PathVariable Long paymentId,
            @Parameter(description = "Receipt details or notes about the cash payment", required = true)
            @RequestParam String receiptDetails) {
        return ResponseEntity.ok(paymentService.confirmCashPayment(paymentId, receiptDetails));
    }
}