package com.example.cryptoapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomCurrencyInfo {
    private String code;
    private String chineseName;
    private String rate;
    private double rateFloat;
}
