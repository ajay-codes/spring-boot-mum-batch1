package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class GreetingConfiguration {

    @Bean
    @Profile("!test")
    public String bean1() {
        return "BEAN1";
    }

    @Bean
    @Profile("mumbai")
    public String bean2() {
        return "BEAN2";
    }

}
