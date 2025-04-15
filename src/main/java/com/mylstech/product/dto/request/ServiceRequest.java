package com.mylstech.product.dto.request;

import com.mylstech.product.model.Description;
import com.mylstech.product.model.Service;
import com.mylstech.product.util.ServiceType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ServiceRequest {
    private ServiceType serviceType;
    private String title;
    private String imageUrl;

    // Description fields
    private String shortDescription;
    private String longDescription1;
    private String longDescription2;

    // Highlights as a Map (title -> description)
    private Map<String, String> highlights = new HashMap<> ( );


    /**
     * Convert this request to a Service entity
     */
    public Service toService() {
        Service service = new Service ( );
        service.setServiceType ( this.serviceType );
        service.setTitle ( this.title );
        service.setImageUrl ( this.imageUrl );

        // Set description
        Description description = new Description ( );
        description.setShortDescription ( this.shortDescription );
        description.setLongDescription1 ( this.longDescription1 );
        description.setLongDescription2 ( this.longDescription2 );
        service.setDescription ( description );

        // Set highlights from the map
        if ( this.highlights != null && ! this.highlights.isEmpty ( ) ) {
            service.getHighlightsEmbedded ( ).putAll ( this.highlights );
        }


        return service;
    }


}
