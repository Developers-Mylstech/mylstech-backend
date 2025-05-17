package com.mylstech.product.impl;

import com.mylstech.product.dto.request.HighlightRequest;
import com.mylstech.product.mapper.HighlightMapper;
import com.mylstech.product.model.Highlight;
import com.mylstech.product.repository.HighlightRepository;
import com.mylstech.product.service.HighlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HighlightServiceImpl implements HighlightService {
    private final HighlightRepository highlightRepository;
    private final HighlightMapper highlightMapper;

    @Override
    public Highlight addHighlight(HighlightRequest request) {
        Highlight highlight = highlightMapper.toEntity ( request );
        return highlightRepository.save ( highlight );
    }

    @Override
    public List<Highlight> getAllHighlights() {
        return highlightRepository.findAll ( );
    }

    @Override
    public void deleteHighlight(Long highlightId) {
        highlightRepository.deleteById ( highlightId );
    }

    @Override
    public Highlight updateHighlight(Long highlightId, HighlightRequest request) {
        Highlight highlight = highlightRepository.findById ( highlightId )
                .orElseThrow ( () -> new RuntimeException ( "Highlight not found with id: " + highlightId ) );

        if ( request.getTitle ( ) != null ) {
            highlight.setTitle ( request.getTitle ( ) );
        }

        if ( request.getDescription ( ) != null ) {
            highlight.setDescription ( request.getDescription ( ) );
        }

        return highlightRepository.save ( highlight );
    }
}