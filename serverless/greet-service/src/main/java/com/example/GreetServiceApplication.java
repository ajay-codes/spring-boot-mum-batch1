package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GreetServiceApplication {

	@GetMapping("/greet")
	public String greet() {
		String hostName = "Unknown Host";
		try {
			hostName = java.net.InetAddress.getLocalHost().getHostName();
			System.out.println("Host Name: " + hostName);
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		}
		return "Hello from Greet Service! Host: " + hostName;
	}

	public static void main(String[] args) {
		SpringApplication.run(GreetServiceApplication.class, args);
	}

}
