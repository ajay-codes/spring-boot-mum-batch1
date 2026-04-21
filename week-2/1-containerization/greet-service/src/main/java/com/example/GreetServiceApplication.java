package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@RestController
public class GreetServiceApplication {

	private String logDirectory = "data/logs";
	private String logFilePath = logDirectory + "/gs-log.txt";

	@org.springframework.beans.factory.annotation.Value("${TIMESERVICE_ENDPOINT:http://timeservice:8080/time}")
	private String TIMESERVICE_ENDPOINT;

	private RestTemplate restTemplate = new RestTemplate();

	@PostConstruct
	public void init() {
		// Create log directory if it doesn't exist
		java.io.File logDir = new java.io.File(logDirectory);
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		java.io.File logFile = new java.io.File(logFilePath);
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}
	}

	@org.springframework.web.bind.annotation.GetMapping("/greet-me")
	public String greetMe() {

		// Log the request with timestamp
		try (java.io.FileWriter fw = new java.io.FileWriter(logFilePath, true);
				java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
				java.io.PrintWriter out = new java.io.PrintWriter(bw)) {
			out.println(java.time.LocalDateTime.now() + " - /greet-me request received");
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		String timeServiceResponse = restTemplate.getForObject(TIMESERVICE_ENDPOINT, String.class);

		String hostName = "unknown";
		try {
			hostName = java.net.InetAddress.getLocalHost().getHostName();
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		}
		return "Hello Npci, from " + hostName + ". Time service says: " + timeServiceResponse;
	}

	public static void main(String[] args) {
		SpringApplication.run(GreetServiceApplication.class, args);
	}

}
