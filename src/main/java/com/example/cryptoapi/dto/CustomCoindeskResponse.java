package com.example.cryptoapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomCoindeskResponse {
    private String updateTime;
    private String chartName;
    private List<CustomCurrencyInfo> currencyList;
}
