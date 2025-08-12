package com.example.cryptoapi.controller;

import com.example.cryptoapi.dto.CoindeskApiResponse;
import com.example.cryptoapi.dto.CustomCoindeskResponse;
import com.example.cryptoapi.service.CoindeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coindesk")
public class CoindeskController {
    @Autowired
    private CoindeskService coindeskService;

    @GetMapping("/raw")
    public CoindeskApiResponse getRawData() {
        return coindeskService.getCurrentPrice();
    }

    @GetMapping("/custom")
    public CustomCoindeskResponse getCustomData() {
        return coindeskService.getTransformedData();
    }
}
