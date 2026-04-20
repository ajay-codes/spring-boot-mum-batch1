package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BarComponent {

    private final BranchCityConfigProperties branchCityConfigProperties;

    public String getBranch() {
        return branchCityConfigProperties.getBranch();
    }

    public String getCity() {
        return branchCityConfigProperties.getCity();
    }

}
