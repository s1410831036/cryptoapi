package com.example.cryptoapi.service;

import com.example.cryptoapi.config.LoggingRestTemplate;
import com.example.cryptoapi.dto.CoindeskApiResponse;
import com.example.cryptoapi.dto.CustomCoindeskResponse;
import com.example.cryptoapi.dto.CustomCurrencyInfo;
import com.example.cryptoapi.model.Currency;
import com.example.cryptoapi.model.CurrencyId;
import com.example.cryptoapi.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CoindeskService {
    protected final LoggingRestTemplate loggingRestTemplate;
    protected final ObjectMapper objectMapper;

    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private CurrencyService currencyService;

    private final String apiUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";

public CoindeskService(LoggingRestTemplate loggingRestTemplate, ObjectMapper objectMapper) {
    this.loggingRestTemplate = loggingRestTemplate;
    this.objectMapper = objectMapper;
}

    public CoindeskApiResponse getCurrentPrice() {
        try {
            // 呼叫API
            return loggingRestTemplate.getForObject(apiUrl, CoindeskApiResponse.class);
        } catch (Exception ex) {
            // 載入預設JSON
            return loadMockData();
        }
    }
    private CoindeskApiResponse loadMockData() {
        try {
            ClassPathResource resource = new ClassPathResource("mock/coindesk-mock.json");
            try (InputStream is = resource.getInputStream()) {
                return objectMapper.readValue(is, CoindeskApiResponse.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("無法載入JSON", e);
        }
    }
    public CustomCoindeskResponse getTransformedData() {
        CoindeskApiResponse apiData = getCurrentPrice();
        CustomCoindeskResponse result = new CustomCoindeskResponse();

        // 時間轉換
        LocalDateTime time = ZonedDateTime.parse(apiData.getTime().getUpdatedISO()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        result.setUpdateTime(time.format(formatter));

        List<CustomCurrencyInfo> currencyList = apiData.getBpi().entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // 依 code 排序
                .map(entry -> {
                    String code = entry.getKey();
                    CoindeskApiResponse.CurrencyInfo info = entry.getValue();
                    CurrencyId currencyId = new CurrencyId(apiData.getChartName(), code);
                    Currency currency = currencyRepository.findById(currencyId).orElse(null);

                    CustomCurrencyInfo custom = new CustomCurrencyInfo();
                    custom.setCode(code);
                    custom.setChineseName(currency != null ? currency.getChineseName() : "未知");
                    custom.setRate(info.getRate());
                    custom.setRateFloat(info.getRate_float());

                    return custom;
                })
                .collect(Collectors.toList());

        result.setCurrencyList(currencyList);
        result.setChartName(apiData.getChartName());

        return result;
    }
    // 同步匯率並寫入資料庫
    public void syncRates() {
        CoindeskApiResponse apiData = getCurrentPrice();

        if (apiData == null || apiData.getBpi() == null) {
            System.err.println("取得匯率資料失敗，無法同步");
            return;
        }

        apiData.getBpi().forEach((code, info) -> {
            CurrencyId currencyId = new CurrencyId(apiData.getChartName(), code);
            Currency currency = currencyRepository.findById(currencyId).orElse(new Currency());
            currency.setChartName(apiData.getChartName());
            currency.setCode(code);
            currency.setChineseName(mapChineseName(apiData.getChartName()));
            currency.setRate(info.getRate());
            currencyRepository.save(currency);
            System.out.printf("更新幣別 %s: %s，匯率: %s\n", code, currency.getChineseName(), info.getRate());
        });

        System.out.println("匯率同步完成");
    }

    // 幣別代碼對應中文名稱（示範）
    private String mapChineseName(String chartName) {
        return switch (chartName) {
            case "BTC", "Bitcoin" -> "比特幣";
            case "ETH" -> "以太幣";
            default -> "未知";
        };
    }

    @Scheduled(cron = "0 0/1 * * * ?")  // 每 5 分鐘執行
    public void scheduledSyncRates() {
        try {
            syncRates();
        } catch (Exception e) {
            System.err.println("同步匯率失敗：" + e.getMessage());
        }
    }
}
