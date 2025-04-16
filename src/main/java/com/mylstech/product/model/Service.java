package com.mylstech.product.model;

import com.mylstech.product.util.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

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
    private List<Highlight> highlightsEmbedded = new ArrayList<> ( );

    public void addHighlight(String title, String description) {
        if ( title == null || description == null ) {
            throw new IllegalArgumentException ( "Title and description cannot be null" );
        }
        Highlight highlight = new Highlight ( );
        highlight.setTitle ( title );
        highlight.setDescription ( description );
        this.highlightsEmbedded.add ( highlight );
    }

    public boolean hasHighlight(String title) {
        return this.highlightsEmbedded.stream ( )
                .anyMatch ( h -> h.getTitle ( ).equalsIgnoreCase ( title ) );
    }

    public boolean removeHighlight(String title) {
        return this.highlightsEmbedded.removeIf ( h -> h.getTitle ( ).equalsIgnoreCase ( title ) );
    }

    public void setHighlights(List<Highlight> highlights) {
        if ( highlights == null ) {
            throw new IllegalArgumentException ( "Highlights list cannot be null" );
        }
        this.highlightsEmbedded = new ArrayList<> ( highlights );
    }

    public void clearHighlights() {
        this.highlightsEmbedded.clear ( );
    }
}
