package com.example.cryptoapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class LoggingRestTemplate {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) {
        log.info("External API GET Request URL: {}", url);
        ResponseEntity<T> response = restTemplate.getForEntity(url, responseType, uriVariables);
        log.info("External API GET Response: {}", response.getBody());
        return response;
    }

    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
        log.info("External API GET Request URL: {}", url);
        T response = restTemplate.getForObject(url, responseType, uriVariables);
        log.info("External API GET Response: {}", response);
        return response;
    }

    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables) {
        log.info("External API POST Request URL: {}, Body: {}", url, request);
        ResponseEntity<T> response = restTemplate.postForEntity(url, request, responseType, uriVariables);
        log.info("External API POST Response: {}", response.getBody());
        return response;
    }
}
