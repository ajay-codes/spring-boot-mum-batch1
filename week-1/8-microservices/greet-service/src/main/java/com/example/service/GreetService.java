package com.example.service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.dto.GreetResponse;

@Service
public class GreetService {

    private static final Logger log = LoggerFactory.getLogger(GreetService.class);

    public GreetResponse getGreet(String time) {
        log.debug("Computing greet for time={}", time);
        final int hour;
        if (time == null || time.isBlank()) {
            hour = LocalTime.now().getHour();
            log.debug("No time provided, using current local hour={}", hour);
        } else {
            try {
                hour = LocalTime.parse(time).getHour();
            } catch (DateTimeParseException ex) {
                log.warn("Invalid time format received: {}", time);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "time must be in HH:mm:ss format");
            }
        }

        final String greet;
        if (hour >= 5 && hour < 12) {
            greet = "Good morning";
        } else if (hour >= 12 && hour < 17) {
            greet = "Good afternoon";
        } else if (hour >= 17 && hour < 21) {
            greet = "Good evening";
        } else {
            greet = "Good night";
        }

        log.debug("Resolved greet='{}' for hour={}", greet, hour);
        return new GreetResponse(greet);
    }
}