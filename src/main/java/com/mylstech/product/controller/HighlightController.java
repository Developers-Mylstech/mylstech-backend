package com.mylstech.product.controller;

import com.mylstech.product.dto.request.HighlightRequest;
import com.mylstech.product.model.Highlight;
import com.mylstech.product.service.HighlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/highlights")
@RequiredArgsConstructor
public class HighlightController {
    private final HighlightService highlightService;

    @PostMapping
    public ResponseEntity<Highlight> addHighlight(@RequestBody HighlightRequest request) {
        return ResponseEntity.ok ( highlightService.addHighlight ( request ) );
    }

    @GetMapping
    public ResponseEntity<List<Highlight>> getAllHighlights() {
        return ResponseEntity.ok ( highlightService.getAllHighlights ( ) );
    }

    @DeleteMapping("/{highlightId}")
    public ResponseEntity<Void> deleteHighlight(@PathVariable Long highlightId) {
        highlightService.deleteHighlight ( highlightId );
        return ResponseEntity.noContent ( ).build ( );
    }

    @PutMapping("/{highlightId}")
    public ResponseEntity<Highlight> updateHighlight(
            @PathVariable Long highlightId,
            @RequestBody HighlightRequest request) {
        return ResponseEntity.ok ( highlightService.updateHighlight ( highlightId, request ) );
    }
}