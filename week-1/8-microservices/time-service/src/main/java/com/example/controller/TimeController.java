package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dto.TimeGreetResponse;
import com.example.dto.TimeResponse;
import com.example.client.GreetClient;
import com.example.service.TimeService;

@RestController
public class TimeController {

    private static final Logger log = LoggerFactory.getLogger(TimeController.class);

    private final TimeService timeService;
    private final GreetClient greetClient;

    public TimeController(TimeService timeService, GreetClient greetClient) {
        this.timeService = timeService;
        this.greetClient = greetClient;
    }

    @GetMapping("/time")
    public TimeResponse getTime(@RequestParam(required = false) String timeZone) {
        log.debug("Received /time request with timeZone={}", timeZone);
        return timeService.getCurrentTime(timeZone);
    }

    @GetMapping("/time-greet")
    public TimeGreetResponse getTimeAndGreet(@RequestParam(required = false) String timeZone) {
        log.debug("Received /time-greet request with timeZone={}", timeZone);
        TimeResponse timeResponse = timeService.getCurrentTime(timeZone);
        String greet = greetClient.getGreet(timeResponse.currentTime());
        return new TimeGreetResponse(
                timeResponse.timeZone(),
                timeResponse.currentTime(),
                greet);
    }

}
