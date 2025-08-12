package com.example.cryptoapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoindeskApiResponse {
    private Time time;
    private String disclaimer;
    private String chartName;
    private Map<String, CurrencyInfo> bpi;

    @Data
    public static class Time {
        private String updated;
        private String updatedISO;
        private String updateduk;
    }

    @Data
    public static class CurrencyInfo {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        private double rate_float;
    }

}
