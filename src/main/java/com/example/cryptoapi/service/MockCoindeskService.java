package com.example.cryptoapi.service;

import com.example.cryptoapi.config.LoggingRestTemplate;
import com.example.cryptoapi.dto.CoindeskApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@Profile("mock")
public class MockCoindeskService extends CoindeskService {

    @Autowired
    public MockCoindeskService(LoggingRestTemplate loggingRestTemplate, ObjectMapper objectMapper) {
        super(loggingRestTemplate, objectMapper);
    }

    @Override
    public CoindeskApiResponse getCurrentPrice() {
        try {
            // 用 getInputStream() 避免 jar 環境下 getFile() 失敗
            ClassPathResource resource = new ClassPathResource("mock/coindesk-mock.json");
            String json = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(json, CoindeskApiResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("無法載入 Mock JSON", e);
        }
    }
}

