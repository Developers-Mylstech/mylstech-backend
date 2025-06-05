package com.mylstech.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private Boolean isExpired = false;
    
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Add a payment to this subscription
     * 
     * @param payment The payment to add
     */
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setSubscription(this);
    }
    
    /**
     * Remove a payment from this subscription
     * 
     * @param payment The payment to remove
     */
    public boolean removePayment(Payment payment) {
        return payments.remove(payment);
    }
    
    /**
     * Check if the subscription is currently active
     * 
     * @return true if the subscription is active, false otherwise
     */
    @Transient
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !isExpired && 
               startDate.compareTo(today) <= 0 && 
               endDate.compareTo(today) >= 0;
    }
}
