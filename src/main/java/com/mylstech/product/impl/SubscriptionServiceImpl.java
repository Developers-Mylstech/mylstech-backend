package com.mylstech.product.impl;

import com.mylstech.product.dto.request.SubscriptionRequest;
import com.mylstech.product.dto.response.SubscriptionResponse;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.mapper.SubscriptionMapper;
import com.mylstech.product.model.Customer;
import com.mylstech.product.model.Plan;
import com.mylstech.product.model.Subscription;
import com.mylstech.product.model.User;
import com.mylstech.product.repository.CustomerRepository;
import com.mylstech.product.repository.PlanRepository;
import com.mylstech.product.repository.SubscriptionRepository;
import com.mylstech.product.repository.UserRepository;
import com.mylstech.product.service.SubscriptionService;
import com.mylstech.product.util.PlanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionMapper subscriptionMapper;
    
    @Override
    @Transactional
    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        // Get current authenticated user
        Customer customer = getCurrentCustomer();
        
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", request.getPlanId()));
        
        // Calculate end date based on plan type
        LocalDate endDate = calculateEndDate(request.getStartDate(), plan.getPlanType());
        
        Subscription subscription = subscriptionMapper.toEntity(request, customer, plan);
        subscription.setEndDate(endDate);
        
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        
        return subscriptionMapper.toDto(savedSubscription);
    }
    
    @Override
    public Optional<SubscriptionResponse> getSubscriptionById(Long subscriptionId) {
        // For admin access or when checking if a subscription exists
        return subscriptionRepository.findById(subscriptionId)
                .map(subscriptionMapper::toDto);
    }
    
    @Override
    public Optional<SubscriptionResponse> getCurrentUserSubscription(Long subscriptionId) {
        Customer customer = getCurrentCustomer();
        return subscriptionRepository.findById(subscriptionId)
                .filter(subscription -> subscription.getCustomer().getCustomerId().equals(customer.getCustomerId()))
                .map(subscriptionMapper::toDto);
    }
    
    @Override
    public List<SubscriptionResponse> getAllSubscriptions() {
        // Admin only method
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptionMapper.toDtoList(subscriptions);
    }
    
    @Override
    public List<SubscriptionResponse> getCurrentUserSubscriptions() {
        Customer customer = getCurrentCustomer();
        List<Subscription> subscriptions = subscriptionRepository.findByCustomerCustomerId(customer.getCustomerId());
        return subscriptionMapper.toDtoList(subscriptions);
    }
    
    @Override
    public List<SubscriptionResponse> getSubscriptionsByCustomerId(Long customerId) {
        // Admin only method
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }
        
        List<Subscription> subscriptions = subscriptionRepository.findByCustomerCustomerId(customerId);
        return subscriptionMapper.toDtoList(subscriptions);
    }
    
    @Override
    public List<SubscriptionResponse> getSubscriptionsByPlanId(Long planId) {
        // Admin only method
        if (!planRepository.existsById(planId)) {
            throw new ResourceNotFoundException("Plan", "id", planId);
        }
        
        List<Subscription> subscriptions = subscriptionRepository.findByPlanPlanId(planId);
        return subscriptionMapper.toDtoList(subscriptions);
    }
    
    @Override
    public List<SubscriptionResponse> getActiveSubscriptionsByCustomerId(Long customerId) {
        // Admin only method
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }
        
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptionsByCustomerId(
                customerId, LocalDate.now());
        return subscriptionMapper.toDtoList(subscriptions);
    }
    
    @Override
    public List<SubscriptionResponse> getCurrentUserActiveSubscriptions() {
        Customer customer = getCurrentCustomer();
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptionsByCustomerId(
                customer.getCustomerId(), LocalDate.now());
        return subscriptionMapper.toDtoList(subscriptions);
    }
    
    @Override
    @Transactional
    public SubscriptionResponse updateSubscription(Long subscriptionId, SubscriptionRequest request) {
        Subscription subscription = getSubscriptionForCurrentUser(subscriptionId);
        
        Plan plan = null;
        if (request.getPlanId() != null) {
            plan = planRepository.findById(request.getPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", request.getPlanId()));
        }
        
        Subscription updatedSubscription = subscriptionMapper.updateEntityFromDto(subscription, request, null, plan);
        
        // If start date or plan type changed, recalculate end date
        if (request.getStartDate() != null || plan != null) {
            LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : subscription.getStartDate();
            PlanType planType = plan != null ? plan.getPlanType() : subscription.getPlan().getPlanType();
            
            LocalDate endDate = calculateEndDate(startDate, planType);
            updatedSubscription.setEndDate(endDate);
        }
        
        Subscription savedSubscription = subscriptionRepository.save(updatedSubscription);
        
        return subscriptionMapper.toDto(savedSubscription);
    }
    
    @Override
    @Transactional
    public SubscriptionResponse renewSubscription(Long subscriptionId, SubscriptionRequest request) {
        Subscription subscription = getSubscriptionForCurrentUser(subscriptionId);
        
        Plan plan;
        // If plan is changing, update it
        if (request.getPlanId() != null && !subscription.getPlan().getPlanId().equals(request.getPlanId())) {
            plan = planRepository.findById(request.getPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", request.getPlanId()));
            subscription.setPlan(plan);
        } else {
            plan = subscription.getPlan();
        }
        
        // Set new start date
        subscription.setStartDate(request.getStartDate());
        
        // Calculate new end date based on plan type
        LocalDate endDate = calculateEndDate(request.getStartDate(), plan.getPlanType());
        subscription.setEndDate(endDate);
        
        // Reset expired flag
        subscription.setIsExpired(false);
        
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(savedSubscription);
    }
    
    @Override
    @Transactional
    public SubscriptionResponse cancelSubscription(Long subscriptionId) {
        Subscription subscription = getSubscriptionForCurrentUser(subscriptionId);
        
        subscription.setIsExpired(true);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        
        return subscriptionMapper.toDto(savedSubscription);
    }
    
    @Override
    @Transactional
    public void deleteSubscription(Long subscriptionId) {
        // Only admins can delete subscriptions, so we don't check ownership
        if (!subscriptionRepository.existsById(subscriptionId)) {
            throw new ResourceNotFoundException("Subscription", "id", subscriptionId);
        }
        
        subscriptionRepository.deleteById(subscriptionId);
    }
    
    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    public void markExpiredSubscriptions() {
        LocalDate today = LocalDate.now();
        List<Subscription> expiredSubscriptions = subscriptionRepository.findExpiredButNotMarked(today);
        
        for (Subscription subscription : expiredSubscriptions) {
            subscription.setIsExpired(true);
            subscriptionRepository.save(subscription);
            log.info("Marked subscription {} as expired", subscription.getSubscriptionId());
        }
    }
    
    /**
     * Calculate the end date based on the start date and plan type
     * 
     * @param startDate The start date of the subscription
     * @param planType The type of plan (MONTHLY, YEARLY, etc.)
     * @return The calculated end date
     */
    private LocalDate calculateEndDate(LocalDate startDate, PlanType planType) {
        return switch (planType) {
            case MONTHLY -> startDate.plusMonths(1);
//            case QUARTERLY -> startDate.plusMonths(3);
//            case HALF_YEARLY -> startDate.plusMonths(6);
            case YEARLY -> startDate.plusYears(1);
//            case BIENNIAL -> startDate.plusYears(2);
            default -> startDate.plusMonths(1); // Default to monthly
        };
    }
    
    /**
     * Get the current authenticated user's customer profile
     * 
     * @return The customer entity for the current user
     * @throws ResourceNotFoundException if the customer profile doesn't exist
     */
    private Customer getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        return customerRepository.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "userId", user.getUserId()));
    }
    
    /**
     * Get a subscription for the current user, ensuring they own it
     * 
     * @param subscriptionId The ID of the subscription to retrieve
     * @return The subscription if it belongs to the current user
     * @throws ResourceNotFoundException if the subscription doesn't exist or doesn't belong to the current user
     */
    private Subscription getSubscriptionForCurrentUser(Long subscriptionId) {
        Customer customer = getCurrentCustomer();
        
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", subscriptionId));
        
        // Verify ownership
        if (!subscription.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new ResourceNotFoundException("Subscription", "id", subscriptionId);
        }
        
        return subscription;
    }
}
