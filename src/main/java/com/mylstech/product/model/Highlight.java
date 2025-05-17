package com.mylstech.product.model;

import com.mylstech.product.dto.request.HighlightRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "highlights")
@Data
@NoArgsConstructor
public class Highlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long highlightId;

    private String title;
    private String description;

    // Service reference has been removed

    public Highlight(HighlightRequest request) {
        this.title = request.getTitle ( );
        this.description = request.getDescription ( );
    }
}