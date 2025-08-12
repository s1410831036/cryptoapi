package com.example.cryptoapi.service;

import com.example.cryptoapi.model.Currency;
import com.example.cryptoapi.model.CurrencyId;
import com.example.cryptoapi.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    private Currency sampleCurrency;

    @BeforeEach
    void setup() {
        sampleCurrency = new Currency("BTC", "比特幣","USD","30,000.00");
    }

    @Test
    void testGetAll() {
        List<Currency> currencies = List.of(
                new Currency("BTC", "比特幣","USD","30,000.00"),
                new Currency("ETH", "以太幣","USD","1500.00")
        );

        when(currencyRepository.findAll()).thenReturn(currencies);

        List<Currency> result = currencyService.getAll();

        assertEquals(2, result.size());
        assertEquals("BTC", result.get(0).getChartName());
        assertEquals("USD", result.get(0).getCode());
        assertEquals("比特幣", result.get(0).getChineseName());
        assertEquals("30,000.00", result.get(0).getRate());
        assertEquals("ETH", result.get(1).getChartName());
        assertEquals("USD", result.get(1).getCode());
        assertEquals("以太幣", result.get(1).getChineseName());
        assertEquals("1500.00", result.get(1).getRate());
    }

    @Test
    void testGetById_Found() {
        when(currencyRepository.findById(new CurrencyId("BTC","USD"))).thenReturn(Optional.of(sampleCurrency));

        Currency result = currencyService.getById("BTC","USD");

        assertNotNull(result);
        assertEquals("BTC", result.getChartName());
        assertEquals("USD", result.getCode());
        assertEquals("比特幣", result.getChineseName());
        assertEquals("30,000.00", result.getRate());
    }

    @Test
    void testGetById_NotFound() {
        when(currencyRepository.findById(new CurrencyId("ADA","USD"))).thenReturn(Optional.empty());

        Currency result = currencyService.getById("ADA","USD");

        assertNull(result);
    }

    @Test
    void testSave() {
        when(currencyRepository.save(sampleCurrency)).thenReturn(sampleCurrency);

        Currency result = currencyService.save(sampleCurrency);

        assertNotNull(result);
        assertEquals("BTC", result.getChartName());
        verify(currencyRepository, times(1)).save(sampleCurrency);
    }

    @Test
    void testDelete() {
        doNothing().when(currencyRepository).deleteById(new CurrencyId("BTC","USD"));

        currencyService.delete("BTC","USD");

        verify(currencyRepository, times(1)).deleteById(new CurrencyId("BTC","USD"));
    }
}
