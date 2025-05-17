package com.mylstech.product.dto.request;

import com.mylstech.product.util.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    private ServiceType serviceType;
    private String title;
    private Long imageId;
    private String description;
    private String longDescription1;
    private String longDescription2;
}
