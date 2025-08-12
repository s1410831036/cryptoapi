package com.example.cryptoapi.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        // 過濾掉 HttpServletRequest 和 HttpServletResponse
        Object[] filteredArgs = Arrays.stream(args)
                .filter(arg -> !(arg instanceof jakarta.servlet.http.HttpServletRequest)
                        && !(arg instanceof jakarta.servlet.http.HttpServletResponse))
                .toArray();

        log.info("呼叫API Called: {} with args = {}", methodName, objectMapper.writeValueAsString(filteredArgs));
        Object result = joinPoint.proceed();
        log.info("回傳API Response: {}", objectMapper.writeValueAsString(result));
        return result;
    }
}

