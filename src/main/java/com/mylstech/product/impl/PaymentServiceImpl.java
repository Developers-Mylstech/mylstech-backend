package com.mylstech.product.impl;

import com.mylstech.product.dto.request.PaymentRequest;
import com.mylstech.product.dto.response.PaymentResponse;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.mapper.PaymentMapper;
import com.mylstech.product.model.Customer;
import com.mylstech.product.model.Payment;
import com.mylstech.product.model.Subscription;
import com.mylstech.product.model.User;
import com.mylstech.product.repository.CustomerRepository;
import com.mylstech.product.repository.PaymentRepository;
import com.mylstech.product.repository.SubscriptionRepository;
import com.mylstech.product.repository.UserRepository;
import com.mylstech.product.service.PaymentService;
import com.mylstech.product.util.PaymentMethod;
import com.mylstech.product.util.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        // Get current authenticated user
        Customer customer = getCurrentCustomer ( );

        // Verify subscription belongs to the current user
        Subscription subscription = subscriptionRepository.findById ( request.getSubscriptionId ( ) )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Subscription", "id", request.getSubscriptionId ( ) ) );

        if ( ! subscription.getCustomer ( ).getCustomerId ( ).equals ( customer.getCustomerId ( ) ) ) {
            throw new ResourceNotFoundException ( "Subscription", "id", request.getSubscriptionId ( ) );
        }

        // Process payment based on payment method
        switch (request.getPaymentMethod ( )) {
            case CASH:
                return processCashPayment ( request );
            case CREDIT_CARD:
            case DEBIT_CARD:
//            case PAYPAL:
//            case STRIPE:
//            case RAZORPAY:
//                // Calculate amount from subscription's plan
//                Double amount = subscription.getPlan().getPricing();
//
//                // Create payment
//                Payment payment = paymentMapper.toEntity(request, subscription);
//                payment.setAmount(amount);
//
//                // Here you would integrate with the respective payment gateway
//                // For now, we'll just set the status to COMPLETED
//                payment.setStatus(PaymentStatus.COMPLETED);
//
//                Payment savedPayment = paymentRepository.save(payment);
//                log.info("Payment processed with ID: {} and method: {}",
//                         savedPayment.getPaymentId(), savedPayment.getPaymentMethod());
//
//                return paymentMapper.toDto(savedPayment);
            case BANK_TRANSFER:
                // Create payment with pending status
                Payment bankPayment = paymentMapper.toEntity ( request, subscription );
                bankPayment.setAmount ( subscription.getPlan ( ).getPricing ( ) );
                bankPayment.setStatus ( PaymentStatus.PENDING );
                bankPayment.setPaymentDetails ( "Bank transfer pending verification" );

                Payment savedBankPayment = paymentRepository.save ( bankPayment );
                log.info ( "Bank transfer payment created with ID: {}", savedBankPayment.getPaymentId ( ) );

                return paymentMapper.toDto ( savedBankPayment );
            default:
                // Handle other payment methods
                Payment otherPayment = paymentMapper.toEntity ( request, subscription );
                otherPayment.setAmount ( subscription.getPlan ( ).getPricing ( ) );
                otherPayment.setStatus ( PaymentStatus.PENDING );
                otherPayment.setPaymentDetails ( "Payment pending processing" );

                Payment savedOtherPayment = paymentRepository.save ( otherPayment );
                log.info ( "Other payment method processed with ID: {}", savedOtherPayment.getPaymentId ( ) );

                return paymentMapper.toDto ( savedOtherPayment );
        }
    }

    @Override
    public Optional<PaymentResponse> getPaymentById(Long paymentId) {
        return paymentRepository.findById ( paymentId )
                .map ( paymentMapper::toDto );
    }

    @Override
    public Optional<PaymentResponse> getCurrentUserPayment(Long paymentId) {
        Customer customer = getCurrentCustomer ( );

        return paymentRepository.findById ( paymentId )
                .filter ( payment -> payment.getSubscription ( ).getCustomer ( ).getCustomerId ( ).equals ( customer.getCustomerId ( ) ) )
                .map ( paymentMapper::toDto );
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll ( );
        return paymentMapper.toDtoList ( payments );
    }

    @Override
    public List<PaymentResponse> getCurrentUserPayments() {
        Customer customer = getCurrentCustomer ( );
        List<Payment> payments = paymentRepository.findBySubscriptionCustomerCustomerId ( customer.getCustomerId ( ) );
        return paymentMapper.toDtoList ( payments );
    }

    @Override
    public List<PaymentResponse> getPaymentsForSubscription(Long subscriptionId) {
        Customer customer = getCurrentCustomer ( );

        // Verify subscription belongs to the current user
        Subscription subscription = subscriptionRepository.findById ( subscriptionId )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Subscription", "id", subscriptionId ) );

        if ( ! subscription.getCustomer ( ).getCustomerId ( ).equals ( customer.getCustomerId ( ) ) ) {
            throw new ResourceNotFoundException ( "Subscription", "id", subscriptionId );
        }

        List<Payment> payments = paymentRepository.findBySubscriptionSubscriptionId ( subscriptionId );
        return paymentMapper.toDtoList ( payments );
    }

    @Override
    public List<PaymentResponse> getPaymentsByCustomerId(Long customerId) {
        if ( ! customerRepository.existsById ( customerId ) ) {
            throw new ResourceNotFoundException ( "Customer", "id", customerId );
        }

        List<Payment> payments = paymentRepository.findBySubscriptionCustomerCustomerId ( customerId );
        return paymentMapper.toDtoList ( payments );
    }

    @Override
    public List<PaymentResponse> getPaymentsBySubscriptionId(Long subscriptionId) {
//       log.info ( "Getting payments for subscription ID: {}", subscriptionId );
//       log.info ( "Subscription repository exists: {}", subscriptionRepository.existsById ( subscriptionId ) );
        if ( ! subscriptionRepository.existsById ( subscriptionId ) ) {
            throw new ResourceNotFoundException ( "Subscription", "id", subscriptionId );
        }

        List<Payment> payments = paymentRepository.findBySubscriptionSubscriptionId ( subscriptionId );
        return paymentMapper.toDtoList ( payments );
    }

    @Override
    public List<PaymentResponse> getPaymentsByStatus(String status) {
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf ( status.toUpperCase ( ) );
        }
        catch ( IllegalArgumentException e ) {
            throw new IllegalArgumentException ( "Invalid payment status: " + status );
        }

        List<Payment> payments = paymentRepository.findByStatus ( paymentStatus );
        return paymentMapper.toDtoList ( payments );
    }

    @Override
    public Double getTotalRevenueForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = paymentRepository.getTotalRevenueForPeriod ( startDate, endDate );
        return revenue != null ? revenue.doubleValue ( ) : 0.0;
    }

    @Override
    @Transactional
    public PaymentResponse cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findById ( paymentId )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Payment", "id", paymentId ) );

        payment.setStatus ( PaymentStatus.CANCELLED );
        Payment savedPayment = paymentRepository.save ( payment );

        return paymentMapper.toDto ( savedPayment );
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById ( paymentId )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Payment", "id", paymentId ) );

        // Only completed payments can be refunded
        if ( payment.getStatus ( ) != PaymentStatus.COMPLETED ) {
            throw new IllegalStateException ( "Only completed payments can be refunded" );
        }

        // Process refund logic would go here (e.g., call to payment gateway)

        payment.setStatus ( PaymentStatus.REFUNDED );
        Payment savedPayment = paymentRepository.save ( payment );

        return paymentMapper.toDto ( savedPayment );
    }

    @Override
    @Transactional
    public PaymentResponse processCashPayment(PaymentRequest request) {
        // Get current authenticated user
        Customer customer = getCurrentCustomer ( );

        // Verify subscription belongs to the current user
        Subscription subscription = subscriptionRepository.findById ( request.getSubscriptionId ( ) )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Subscription", "id", request.getSubscriptionId ( ) ) );

        if ( ! subscription.getCustomer ( ).getCustomerId ( ).equals ( customer.getCustomerId ( ) ) ) {
            throw new ResourceNotFoundException ( "Subscription", "id", request.getSubscriptionId ( ) );
        }

        // Calculate amount from subscription's plan
        Double amount = subscription.getPlan ( ).getPricing ( );

        // Create payment
        Payment payment = paymentMapper.toEntity ( request, subscription );
        payment.setAmount ( amount );
        payment.setPaymentMethod ( PaymentMethod.CASH );

        // For cash payments, we initially set status to PENDING
        // It will be updated to COMPLETED when cash is received
        payment.setStatus ( PaymentStatus.PENDING );
        payment.setPaymentDetails ( "Cash payment pending verification" );

        Payment savedPayment = paymentRepository.save ( payment );

        log.info ( "Cash payment created with ID: {}", savedPayment.getPaymentId ( ) );

        return paymentMapper.toDto ( savedPayment );
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponse confirmCashPayment(Long paymentId, String receiptDetails) {
        Payment payment = paymentRepository.findById ( paymentId )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Payment", "id", paymentId ) );

        // Verify this is a cash payment
        if ( payment.getPaymentMethod ( ) != PaymentMethod.CASH ) {
            throw new IllegalStateException ( "Only cash payments can be confirmed with this method" );
        }

        // Update payment status and details
        payment.setStatus ( PaymentStatus.COMPLETED );
        payment.setPaymentDetails ( "Cash payment received. " + receiptDetails );
        payment.setTransactionId ( "CASH-" + System.currentTimeMillis ( ) );

        Payment savedPayment = paymentRepository.save ( payment );
        log.info ( "Cash payment confirmed with ID: {}", savedPayment.getPaymentId ( ) );

        return paymentMapper.toDto ( savedPayment );
    }

    /**
     * Get the current authenticated user's customer profile
     *
     * @return The customer entity for the current user
     * @throws ResourceNotFoundException if the customer profile doesn't exist
     */
    private Customer getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext ( ).getAuthentication ( );
        String email = ((UserDetails) authentication.getPrincipal ( )).getUsername ( );

        User user = userRepository.findByEmail ( email )
                .orElseThrow ( () -> new ResourceNotFoundException ( "User", "email", email ) );

        return customerRepository.findByUserUserId ( user.getUserId ( ) )
                .orElseThrow ( () -> new ResourceNotFoundException ( "Customer", "userId", user.getUserId ( ) ) );
    }
}
