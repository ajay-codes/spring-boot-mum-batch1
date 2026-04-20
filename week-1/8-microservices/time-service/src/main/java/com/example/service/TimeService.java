package com.example.service;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.dto.TimeResponse;

@Service
public class TimeService {

    private static final Logger log = LoggerFactory.getLogger(TimeService.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public TimeResponse getCurrentTime(String timeZone) {
        String requestedTimeZone = (timeZone == null || timeZone.isBlank()) ? "UTC" : timeZone;
        log.debug("Resolving current time for requested timeZone={}, effective timeZone={}", timeZone,
                requestedTimeZone);

        final ZoneId zoneId;
        try {
            zoneId = ZoneId.of(requestedTimeZone);
        } catch (DateTimeException ex) {
            log.warn("Invalid timeZone received: {}", requestedTimeZone);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid timeZone. Use values like UTC or Asia/Kolkata");
        }

        String timeString = LocalTime.now(zoneId).format(TIME_FORMATTER);
        log.debug("Resolved timeString={} for zoneId={}", timeString, zoneId.getId());
        return new TimeResponse(zoneId.getId(), timeString);
    }
}
