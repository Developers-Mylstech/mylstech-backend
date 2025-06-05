package com.mylstech.product.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylstech.product.dto.request.ContactRequest;
import com.mylstech.product.dto.response.ContactResponse;
import com.mylstech.product.model.AppSetting;
import com.mylstech.product.repository.AppSettingRepository;
import com.mylstech.product.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.SerializationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {
    private static final String CONTACT_INFO_KEY = "contact_info";
    private final AppSettingRepository appSettingRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ContactResponse getContactInfo() {
        return appSettingRepository.findById ( CONTACT_INFO_KEY )
                .map ( this::deserializeContactInfo )
                .orElse ( ContactResponse.builder ( )
                        .phoneNumber ( "" )
                        .email ( "" )
                        .address ( "" )
                        .build ( ) );
    }

    @Override
    @Transactional
    public ContactResponse updateContactInfo(ContactRequest request) {
        String serializedValue;
        try {
            serializedValue = objectMapper.writeValueAsString ( request );
        }
        catch ( JsonProcessingException e ) {
            log.error ( "Error serializing contact info", e );
            throw new SerializationException ( "Error updating contact info", e );
        }

        AppSetting setting = appSettingRepository.findById ( CONTACT_INFO_KEY )
                .orElse ( new AppSetting ( CONTACT_INFO_KEY, null, "Company contact information" ) );

        setting.setValue ( serializedValue );
        appSettingRepository.save ( setting );

        return ContactResponse.builder ( )
                .phoneNumber ( request.getPhoneNumber ( ) )
                .email ( request.getEmail ( ) )
                .address ( request.getAddress ( ) )
                .postalCode ( request.getPostalCode ( ) )
                .build ( );
    }

    private ContactResponse deserializeContactInfo(AppSetting setting) {
        try {
            return objectMapper.readValue ( setting.getValue ( ), ContactResponse.class );
        }
        catch ( JsonProcessingException e ) {
            log.error ( "Error deserializing contact info", e );
            return ContactResponse.builder ( )
                    .phoneNumber ( "" )
                    .email ( "" )
                    .address ( "" )
                    .postalCode ( "" )
                    .build ( );
        }
    }
}