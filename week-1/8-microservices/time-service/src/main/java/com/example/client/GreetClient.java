package com.example.client;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.dto.GreetResponse;

@Component
public class GreetClient {

    private static final Logger log = LoggerFactory.getLogger(GreetClient.class);
    private final RestTemplate restTemplate;
    private final String greetServiceBaseUrl;

    public GreetClient(
            RestTemplate restTemplate,
            @Value("${greet-service.base-url:http://localhost:8080}") String greetServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.greetServiceBaseUrl = greetServiceBaseUrl;
    }

    public String getGreet(String time) {
        String baseUrl = Objects.requireNonNull(greetServiceBaseUrl, "greetServiceBaseUrl cannot be null");
        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/greet")
                .queryParam("time", time)
                .toUriString();
        log.debug("Calling greet-service. url={}", url);

        GreetResponse response = restTemplate.getForObject(url, GreetResponse.class);
        if (response == null || response.greet() == null) {
            log.warn("greet-service returned empty response for time={}", time);
            return "Hello";
        }

        log.debug("Received greet='{}' from greet-service", response.greet());
        return response.greet();
    }
}