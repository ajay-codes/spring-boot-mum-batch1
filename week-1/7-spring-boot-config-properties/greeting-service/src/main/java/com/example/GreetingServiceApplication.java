package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class GreetingServiceApplication {

	@Value("${greet.message:'m1'}")
	private String message;

	@Value("${greet.message.city:'default-city'}")
	private String city;

	private final FooComponent fooComponent;
	private final BarComponent barComponent;

	@GetMapping("/greet-me")
	public String greetMe() {
		return message + " from " + city + " (Foo: " + fooComponent.getBranch() + ", " + fooComponent.getCity()
				+ ", Bar: " + barComponent.getBranch() + ", " + barComponent.getCity() + ")";
	}

	public static void main(String[] args) {
		SpringApplication.run(GreetingServiceApplication.class, args);
	}

}
