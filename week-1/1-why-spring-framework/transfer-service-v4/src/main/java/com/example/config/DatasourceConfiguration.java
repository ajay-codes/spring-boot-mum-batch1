package com.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DatasourceConfiguration {

    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/postgres}")
    private String url;
    @Value("${spring.datasource.username:postgres}")
    private String username;
    @Value("${spring.datasource.password:mysecretpassword}")
    private String password;
    @Value("${spring.datasource.maximumPoolSize:15}")
    private int maximumPoolSize;

    @Bean(name = "dataSource")
    @Description("Configures the HikariCP DataSource for PostgreSQL database connection.")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new org.springframework.jdbc.datasource.DataSourceTransactionManager(dataSource);
    }



}
