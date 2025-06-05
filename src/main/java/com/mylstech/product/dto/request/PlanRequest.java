package com.mylstech.product.dto.request;

import com.mylstech.product.util.PlanType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlanRequest {
    private Long imageId;
    private String title;
    private String description;
    private Double pricing;
    private Boolean isActive;
    private Integer trailDuration;
    private PlanType planType;
    private List<String> highlights = new ArrayList<>();
}
