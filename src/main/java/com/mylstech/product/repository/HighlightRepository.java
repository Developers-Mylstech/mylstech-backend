package com.mylstech.product.repository;

import com.mylstech.product.model.Highlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HighlightRepository extends JpaRepository<Highlight, Long> {
    // Service-related methods have been removed
}