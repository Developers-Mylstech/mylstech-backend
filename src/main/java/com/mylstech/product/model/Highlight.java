package com.mylstech.product.model;

import com.mylstech.product.dto.request.HighlightRequest;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Highlight {
    private String title;
    private String description;

    public Highlight(HighlightRequest request) {
        this.title =request.getTitle();
        this.description =request.getDescription();
    }
}