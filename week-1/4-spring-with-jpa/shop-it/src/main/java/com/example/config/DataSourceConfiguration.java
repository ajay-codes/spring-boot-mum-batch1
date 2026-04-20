package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Explicit DataSource configuration using HikariCP.
 * Demonstrates how to manually configure a DataSource bean instead of relying
 * on Spring Boot's auto-configuration (spring.datasource.* properties would
 * normally create one automatically). This @Configuration is active, so it
 * overrides the auto-configured DataSource.
 */
@Configuration
public class DataSourceConfiguration {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean(name = "dataSource")
    public javax.sql.DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        // postgreSQL configuration
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
        

}