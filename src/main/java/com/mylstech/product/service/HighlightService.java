package com.mylstech.product.service;

import com.mylstech.product.dto.request.HighlightRequest;
import com.mylstech.product.model.Highlight;

import java.util.List;

public interface HighlightService {
    Highlight addHighlight(HighlightRequest request);

    List<Highlight> getAllHighlights();

    void deleteHighlight(Long highlightId);

    Highlight updateHighlight(Long highlightId, HighlightRequest request);
}