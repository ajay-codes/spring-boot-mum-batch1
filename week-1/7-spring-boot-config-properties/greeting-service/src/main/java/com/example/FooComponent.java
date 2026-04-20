package com.example;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FooComponent {

    private final BranchCityConfigProperties branchCityConfigProperties;

    public String getBranch() {
        return branchCityConfigProperties.getBranch();
    }

    public String getCity() {
        return branchCityConfigProperties.getCity();
    }

}
