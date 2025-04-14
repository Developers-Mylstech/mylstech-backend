package com.mylstech.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "plans")
@EqualsAndHashCode(of = "planId")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricing;

    @Column(nullable = false)
    private Boolean status = true;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "plan_type", nullable = false, length = 50)
    private String planType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
