package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
// @Configuration is intentionally commented out.
// Spring Boot auto-configures a DataSource from application.properties,
// so this manual bean definition is not needed. Kept here as a reference
// to show how you would configure a DataSource explicitly if needed.
import com.zaxxer.hikari.HikariDataSource;

// @Configuration  (see comment above)
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