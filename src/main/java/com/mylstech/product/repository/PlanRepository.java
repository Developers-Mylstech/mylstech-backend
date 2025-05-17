package com.mylstech.product.repository;

import com.mylstech.product.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    // Add this method to check if an image is used by any plan
    boolean existsByImageImageUrl(String imageUrl);
}
