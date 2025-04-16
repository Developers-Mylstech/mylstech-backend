package com.mylstech.product.model;

import com.mylstech.product.util.PlanType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricing;

    @Column(nullable = false)
    private Boolean status = true;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "plan_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "plans")
    private Set<Cart> carts = new HashSet<> ();

    @ElementCollection
    @CollectionTable(
            name = "plan_highlights",
            joinColumns = @JoinColumn(name = "plan_id")
    )
    @Column(name = "highlight")
    private List<String> highlightsEmbedded = new ArrayList<> ();

    /**
     * Add a highlight to the plan
     * @param highlight The highlight text
     */
    public void addHighlight(String highlight) {
        this.highlightsEmbedded.add(highlight);
    }

    /**
     * Remove a highlight
     * @param highlight The highlight to remove
     * @return true if the highlight was removed, false otherwise
     */
    public boolean removeHighlight(String highlight) {
        return this.highlightsEmbedded.remove(highlight);
    }

    /**
     * Check if a highlight exists
     * @param highlight The highlight to check
     * @return true if the highlight exists, false otherwise
     */
    public boolean hasHighlight(String highlight) {
        return this.highlightsEmbedded.contains(highlight);
    }

    /**
     * Get the number of highlights
     * @return The number of highlights
     */
    public int getHighlightCount() {
        return this.highlightsEmbedded.size();
    }
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
