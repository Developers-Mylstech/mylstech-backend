package com.mylstech.product.dto.response;

import com.mylstech.product.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long clientId;
    private String name;
    private ImageResponse image;
    
    public ClientResponse(Client client) {
        this.clientId = client.getClientId();
        this.name = client.getName();
        
        if (client.getImage() != null) {
            this.image = ImageResponse.builder()
                    .imageId(client.getImage().getImageId())
                    .imageUrl(client.getImage().getImageUrl())
                    .createdAt(client.getImage().getCreatedAt())
                    .build();
        }
    }
}