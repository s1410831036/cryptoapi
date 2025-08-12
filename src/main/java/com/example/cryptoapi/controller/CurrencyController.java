package com.example.cryptoapi.controller;

import com.example.cryptoapi.model.Currency;
import com.example.cryptoapi.model.CurrencyId;
import com.example.cryptoapi.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public List<Currency> list() {
        return currencyService.getAll();
    }

    @PostMapping
    public Currency create(@RequestBody Currency currency) {
        return currencyService.save(currency);
    }

    @PutMapping("/{code}")
    public Currency update(@PathVariable String code, @RequestBody Currency currency) {
        currency.setCode(code);
        return currencyService.save(currency);
    }

    @DeleteMapping("/{chartName}/{code}")
    public void delete(@PathVariable String chartName, @PathVariable String code) {
        currencyService.delete(chartName, code);
    }
}
