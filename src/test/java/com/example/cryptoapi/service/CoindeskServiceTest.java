package com.example.cryptoapi.service;

import com.example.cryptoapi.config.LoggingRestTemplate;
import com.example.cryptoapi.dto.CoindeskApiResponse;
import com.example.cryptoapi.dto.CustomCoindeskResponse;
import com.example.cryptoapi.dto.CustomCurrencyInfo;
import com.example.cryptoapi.model.Currency;
import com.example.cryptoapi.model.CurrencyId;
import com.example.cryptoapi.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CoindeskServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private LoggingRestTemplate loggingRestTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CoindeskService coindeskService; // 完整注入依賴的實例

    private CoindeskService spyService;  // 這裡先定義 spyService

    @BeforeEach
    void setup() {
        spyService = Mockito.spy(coindeskService);  // 對 coindeskService 做 spy
        // 再次注入 mock 的 currencyRepository，避免 NullPointer
        ReflectionTestUtils.setField(spyService, "currencyRepository", currencyRepository);
    }

    @Test
    void testGetTransformedData() {
        CoindeskApiResponse apiData = new CoindeskApiResponse();
        CoindeskApiResponse.Time time = new CoindeskApiResponse.Time();
        time.setUpdatedISO("2025-08-11T12:34:56Z");
        apiData.setTime(time);
        apiData.setChartName("Bitcoin");

        CoindeskApiResponse.CurrencyInfo usdInfo = new CoindeskApiResponse.CurrencyInfo();
        usdInfo.setRate("23,342.0112");
        usdInfo.setRate_float(23342.0112);

        CoindeskApiResponse.CurrencyInfo eurInfo = new CoindeskApiResponse.CurrencyInfo();
        eurInfo.setRate("22,738.5269");
        eurInfo.setRate_float(22738.5269);

        apiData.setBpi(Map.of("USD", usdInfo, "EUR", eurInfo));

        // stub spyService.getCurrentPrice()
        doReturn(apiData).when(spyService).getCurrentPrice();

        // stub currencyRepository

//        Mockito.when(currencyRepository.findById(new CurrencyId("Bitcoin", "USD")))
//                .thenReturn(Optional.of(new Currency("Bitcoin", "比特幣", "USD", "23,342.0112")));
//
//        Mockito.when(currencyRepository.findById(new CurrencyId("ADA", "USD")))
//                .thenReturn(Optional.empty());
        Mockito.when(currencyRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new Currency("Bitcoin", "比特幣", "USD", "23,342.0112")));



        // 使用 spyService 進行測試
        CustomCoindeskResponse result = spyService.getTransformedData();

        // 驗證結果
        assertEquals("2025/08/11 12:34:56", result.getUpdateTime());
        assertEquals(2, result.getCurrencyList().size());
        assertEquals("EUR", result.getCurrencyList().get(0).getCode());
        assertEquals("USD", result.getCurrencyList().get(1).getCode());
    }
}
