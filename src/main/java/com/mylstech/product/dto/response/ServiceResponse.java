package com.mylstech.product.dto.response;

import com.mylstech.product.model.Highlight;
import com.mylstech.product.model.Service;
import com.mylstech.product.util.ServiceType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ServiceResponse {
    private Long serviceId;
    private ServiceType serviceType;
    private String title;
    private String imageUrl;

    // Description fields
    private String shortDescription;
    private String longDescription1;
    private String longDescription2;

    // Highlights as a Map (title -> description)
    private List<Highlight> highlights = new ArrayList<> ( );


    public ServiceResponse(Service service) {
        this.serviceId = service.getServiceId ( );
        this.serviceType = service.getServiceType ( );
        this.title = service.getTitle ( );
        this.imageUrl = service.getImageUrl ( );

        // Map description if available
        if ( service.getDescription ( ) != null ) {
            this.shortDescription = service.getDescription ( ).getShortDescription ( );
            this.longDescription1 = service.getDescription ( ).getLongDescription1 ( );
            this.longDescription2 = service.getDescription ( ).getLongDescription2 ( );
        }

        // Map highlights from the embedded map
        if ( service.getHighlightsEmbedded ( ) != null && ! service.getHighlightsEmbedded ( ).isEmpty ( ) ) {
            // Direct map copy
            this.highlights.addAll ( service.getHighlightsEmbedded ( ) );
        }
    }


}
