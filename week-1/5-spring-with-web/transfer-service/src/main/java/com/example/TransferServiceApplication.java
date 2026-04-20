package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.service.TransferService;

@SpringBootApplication
public class TransferServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransferServiceApplication.class, args);
	}

	// @Bean
	public CommandLineRunner commandLineRunner(
		TransferService transferService
	){
		return args -> {
			System.out.println("Transfer Service is running...");
			// Example transfer
			transferService.transfer("1", "2", 100.0);
		};
	}

}
