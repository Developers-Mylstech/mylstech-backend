package com.mylstech.product.repository;

import com.mylstech.product.model.Payment;
import com.mylstech.product.util.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findBySubscriptionSubscriptionId(Long subscriptionId);
    
    List<Payment> findBySubscriptionCustomerCustomerId(Long customerId);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED' AND p.createdAt BETWEEN ?1 AND ?2")
    BigDecimal getTotalRevenueForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'FAILED' AND p.createdAt BETWEEN ?1 AND ?2")
    Long getFailedPaymentsCountForPeriod(LocalDateTime startDate, LocalDateTime endDate);
}