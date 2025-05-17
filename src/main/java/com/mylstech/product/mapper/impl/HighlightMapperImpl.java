package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.HighlightRequest;
import com.mylstech.product.mapper.HighlightMapper;
import com.mylstech.product.model.Highlight;
import org.springframework.stereotype.Component;

/**
 * Implementation of the HighlightMapper interface
 */
@Component
public class HighlightMapperImpl implements HighlightMapper {

    @Override
    public HighlightRequest toDto(Highlight highlight) {
        if ( highlight == null ) {
            return null;
        }

        HighlightRequest highlightRequest = new HighlightRequest ( );
        highlightRequest.setTitle ( highlight.getTitle ( ) );
        highlightRequest.setDescription ( highlight.getDescription ( ) );

        return highlightRequest;
    }

    @Override
    public Highlight toEntity(HighlightRequest highlightRequest) {
        if ( highlightRequest == null ) {
            return null;
        }

        Highlight highlight = new Highlight ( );
        highlight.setTitle ( highlightRequest.getTitle ( ) );
        highlight.setDescription ( highlightRequest.getDescription ( ) );

        return highlight;
    }
}
