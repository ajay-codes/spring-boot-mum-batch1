package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TimeServiceApplication {

	@GetMapping("/time")
	public String getCurrentTime() {
		String hostName = "unknown";
		try {
			hostName = java.net.InetAddress.getLocalHost().getHostName();
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		}
		return "Current Time is: " + java.time.LocalDateTime.now() + " from " + hostName;
	}

	public static void main(String[] args) {
		SpringApplication.run(TimeServiceApplication.class, args);
	}

}
