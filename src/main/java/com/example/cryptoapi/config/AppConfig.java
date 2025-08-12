package com.example.cryptoapi.config; // 這行要依你的實際路徑調整

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public LoggingRestTemplate loggingRestTemplate() {
        return new LoggingRestTemplate();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
