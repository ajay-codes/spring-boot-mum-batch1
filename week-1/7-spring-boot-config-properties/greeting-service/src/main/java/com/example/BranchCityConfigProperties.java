package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "service")
public class BranchCityConfigProperties {

    private String branch = "default-branch";
    private String city = "default-city";

    public String getBranch() {
        return branch;
    }

    public String getCity() {
        return city;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
