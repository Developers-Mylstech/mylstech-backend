package com.mylstech.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AboutUsRequest {
    private Achievement achievement;
    private Section1 section1;
    private Section2 section2;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Achievement {
        private String completedProject;
        private String happyCustomer;
        private String yearOfMastery;
        private String workloadsHours;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Section1 {
        private String title;
        private String description;
        private String image1;
        private String image2;
        private String image3;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Section2 {
        private String title;
        private String description;
        private String image;
    }
}