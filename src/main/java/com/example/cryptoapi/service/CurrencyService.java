package com.example.cryptoapi.service;

import com.example.cryptoapi.model.Currency;
import com.example.cryptoapi.model.CurrencyId;
import com.example.cryptoapi.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    public Currency getById(String chartName, String code) {
        CurrencyId id = new CurrencyId(chartName, code);
        return currencyRepository.findById(id).orElse(null);
    }

    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    public void delete(String chartName, String code) {
        CurrencyId id = new CurrencyId(chartName, code);
        currencyRepository.deleteById(id);
    }

}
