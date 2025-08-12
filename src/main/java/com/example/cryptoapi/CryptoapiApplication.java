package com.example.cryptoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CryptoapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoapiApplication.class, args);
	}

}
