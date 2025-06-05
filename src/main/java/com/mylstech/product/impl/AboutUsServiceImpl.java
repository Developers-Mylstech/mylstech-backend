package com.mylstech.product.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylstech.product.dto.request.AboutUsRequest;
import com.mylstech.product.dto.response.AboutUsResponse;
import com.mylstech.product.model.AppSetting;
import com.mylstech.product.repository.AppSettingRepository;
import com.mylstech.product.service.AboutUsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AboutUsServiceImpl implements AboutUsService {
    private final AppSettingRepository appSettingRepository;
    private final ObjectMapper objectMapper;
    
    private static final String ABOUT_US_KEY = "about_us_info";
    
    @Override
    public AboutUsResponse getAboutUsInfo() {
        return appSettingRepository.findById(ABOUT_US_KEY)
                .map(this::deserializeAboutUsInfo)
                .orElse(createDefaultAboutUsResponse());
    }
    
    @Override
    @Transactional
    public AboutUsResponse updateAboutUsInfo(AboutUsRequest request) {
        String serializedValue;
        try {
            serializedValue = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("Error serializing about us info", e);
            throw new RuntimeException("Error updating about us info", e);
        }
        
        AppSetting setting = appSettingRepository.findById(ABOUT_US_KEY)
                .orElse(new AppSetting(ABOUT_US_KEY, null, "Company about us information"));
        
        setting.setValue(serializedValue);
        appSettingRepository.save(setting);
        
        return mapToResponse(request);
    }
    
    private AboutUsResponse deserializeAboutUsInfo(AppSetting setting) {
        try {
            return objectMapper.readValue(setting.getValue(), AboutUsResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing about us info", e);
            return createDefaultAboutUsResponse();
        }
    }
    
    private AboutUsResponse createDefaultAboutUsResponse() {
        return AboutUsResponse.builder()
                .achievement(AboutUsResponse.Achievement.builder()
                        .completedProject("")
                        .happyCustomer("")
                        .yearOfMastery("")
                        .workloadsHours("")
                        .build())
                .section1(AboutUsResponse.Section1.builder()
                        .title("")
                        .description("")
                        .image1("")
                        .image2("")
                        .image3("")
                        .build())
                .section2(AboutUsResponse.Section2.builder()
                        .title("")
                        .description("")
                        .image("")
                        .build())
                .build();
    }
    
    private AboutUsResponse mapToResponse(AboutUsRequest request) {
        return AboutUsResponse.builder()
                .achievement(AboutUsResponse.Achievement.builder()
                        .completedProject(request.getAchievement().getCompletedProject())
                        .happyCustomer(request.getAchievement().getHappyCustomer())
                        .yearOfMastery(request.getAchievement().getYearOfMastery())
                        .workloadsHours(request.getAchievement().getWorkloadsHours())
                        .build())
                .section1(AboutUsResponse.Section1.builder()
                        .title(request.getSection1().getTitle())
                        .description(request.getSection1().getDescription())
                        .image1(request.getSection1().getImage1())
                        .image2(request.getSection1().getImage2())
                        .image3(request.getSection1().getImage3())
                        .build())
                .section2(AboutUsResponse.Section2.builder()
                        .title(request.getSection2().getTitle())
                        .description(request.getSection2().getDescription())
                        .image(request.getSection2().getImage())
                        .build())
                .build();
    }
}