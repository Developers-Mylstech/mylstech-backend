package com.mylstech.product.model;

import com.mylstech.product.util.PlanType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "plans")
@EqualsAndHashCode(of = "planId")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private ImageEntity image;

    @Column(nullable = false)
    private Double pricing;

    @Column(nullable = false)
    private Boolean isActive = true;


    private Integer trailDuration;

    @Column(name = "plan_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ElementCollection
    @CollectionTable(
        name = "plan_highlights", 
        joinColumns = @JoinColumn(name = "plan_id")
    )
    @Column(name = "highlight_title", nullable = false)
    private List<String> highlights = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Add a highlight to the plan
     *
     * @param highlight The highlight title to add
     */
    public void addHighlight(String highlight) {
        this.highlights.add(highlight);
    }

    /**
     * Remove a highlight
     *
     * @param highlight The highlight title to remove
     * @return true if the highlight was removed, false otherwise
     */
    public boolean removeHighlight(String highlight) {
        return this.highlights.remove(highlight);
    }

    /**
     * Get the number of highlights
     *
     * @return The number of highlights
     */
    public int getHighlightCount() {
        return this.highlights.size();
    }
}
