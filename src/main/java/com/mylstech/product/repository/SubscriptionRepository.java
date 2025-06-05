package com.mylstech.product.repository;

import com.mylstech.product.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByCustomerCustomerId(Long customerId);
    
    List<Subscription> findByPlanPlanId(Long planId);
    
    @Query("SELECT s FROM Subscription s WHERE s.endDate < ?1 AND s.isExpired = false")
    List<Subscription> findExpiredButNotMarked(LocalDate currentDate);
    
    @Query("SELECT s FROM Subscription s WHERE s.customer.customerId = ?1 AND s.isExpired = false AND s.endDate >= ?2")
    List<Subscription> findActiveSubscriptionsByCustomerId(Long customerId, LocalDate currentDate);
    
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.createdAt >= ?1")
    Long countNewSubscriptionsAfterDate(LocalDate date);
}