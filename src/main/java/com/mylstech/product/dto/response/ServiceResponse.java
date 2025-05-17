package com.mylstech.product.dto.response;

import com.mylstech.product.model.Service;
import com.mylstech.product.util.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {
    private Long serviceId;
    private ServiceType serviceType;
    private String title;
    private ImageResponse image;
    private String description;
    private String longDescription1;
    private String longDescription2;


    public ServiceResponse(Service service) {
        this.serviceId = service.getServiceId ( );
        this.serviceType = service.getServiceType ( );
        this.title = service.getTitle ( );
        this.description = service.getDescription ( );
        this.longDescription1 = service.getLongDescription1 ( );
        this.longDescription2 = service.getLongDescription2 ( );

        // Image URL mapping
        if ( service.getImage ( ) != null ) {
            this.image = ImageResponse.builder ( )
                    .imageId ( service.getImage ( ).getImageId ( ) )
                    .imageUrl ( service.getImage ( ).getImageUrl ( ) )
                    .createdAt ( service.getImage ( ).getCreatedAt ( ) )
                    .build ( );
        }
    }
}