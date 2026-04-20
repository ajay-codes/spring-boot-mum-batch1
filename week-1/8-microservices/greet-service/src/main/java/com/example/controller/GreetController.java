package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dto.GreetResponse;
import com.example.service.GreetService;

@RestController
public class GreetController {

    private static final Logger log = LoggerFactory.getLogger(GreetController.class);

    private final GreetService greetService;

    public GreetController(GreetService greetService) {
        this.greetService = greetService;
    }

    @GetMapping("/greet")
    public GreetResponse getGreet(@RequestParam(required = false) String time) {
        log.debug("Received /greet request with time={}", time);
        return greetService.getGreet(time);
    }
}