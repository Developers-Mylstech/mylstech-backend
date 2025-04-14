package com.mylstech.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Description {
    @Column(name = "short_description", length = 255)
    private String shortDescription;

    @Column(name = "long_description1", length = 2000)
    private String longDescription1;

    @Column(name = "long_description2", length = 2000)
    private String longDescription2;
}
