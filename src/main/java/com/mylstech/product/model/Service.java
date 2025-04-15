package com.mylstech.product.model;

import com.mylstech.product.util.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "services")
@EqualsAndHashCode(of = "serviceId")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Embedded
    private Description description;

    /**
     * Collection of embedded highlights as a Map of title -> description
     * This replaces the previous one-to-many relationship with Highlight entity
     */
    @ElementCollection
    @CollectionTable(
        name = "service_highlights",
        joinColumns = @JoinColumn(name = "service_id")
    )
    @MapKeyColumn(name = "title")
    @Column(name = "description")
    private Map<String, String> highlightsEmbedded = new HashMap<>();

//
    /**
     * Add a highlight to the service
     * @param title The highlight title (key)
     * @param description The highlight description (value)
     */
    public void addHighlight(String title, String description) {
        this.highlightsEmbedded.put(title, description);
    }

    /**
     * Get a highlight description by title
     * @param title The highlight title (key)
     * @return The highlight description or null if not found
     */
    public String getHighlightDescription(String title) {
        return this.highlightsEmbedded.get(title);
    }

    /**
     * Remove a highlight by title
     * @param title The highlight title (key)
     * @return The removed description or null if not found
     */
    public String removeHighlight(String title) {
        return this.highlightsEmbedded.remove(title);
    }

    /**
     * Check if a highlight with the given title exists
     * @param title The highlight title (key)
     * @return true if the highlight exists, false otherwise
     */
    public boolean hasHighlight(String title) {
        return this.highlightsEmbedded.containsKey(title);
    }

    /**
     * Get the number of highlights
     * @return The number of highlights
     */
    public int getHighlightCount() {
        return this.highlightsEmbedded.size();
    }
}