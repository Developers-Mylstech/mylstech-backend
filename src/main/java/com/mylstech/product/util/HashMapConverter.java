package com.mylstech.product.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = false)
public class HashMapConverter implements AttributeConverter<Map<String, String>, String> {
    private final ObjectMapper objectMapper;

    public HashMapConverter() {
        this.objectMapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    @Override
    public String convertToDatabaseColumn(Map<String, String> data) {
        if (data == null || data.isEmpty()) {
            return "{}";
        }
        try {
            Map<String, String> cleanData = new HashMap<>();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                String key = entry.getKey() != null ? entry.getKey().trim() : "";
                String value = entry.getValue() != null ? 
                    entry.getValue()
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t")
                        .replace("\"", "\\\"") : "";
                cleanData.put(key, value);
            }
            return objectMapper.writeValueAsString(cleanData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty() || "{}".equals(json)) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<HashMap<String, String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to map", e);
        }
    }
}