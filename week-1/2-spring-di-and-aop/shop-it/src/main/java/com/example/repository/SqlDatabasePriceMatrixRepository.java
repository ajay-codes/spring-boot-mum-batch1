package com.example.repository;

import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

// @Component("sqlDatabasePriceMatrix")
@Repository("sqlDatabasePriceMatrix") // singleton scope by default
public class SqlDatabasePriceMatrixRepository implements PriceMatrix {

    private final JdbcTemplate jdbcTemplate;

    public SqlDatabasePriceMatrixRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public double getPrice(String productId) {
        String sql = "SELECT price FROM price_matrix WHERE product_id = ?";
        Double price = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getDouble("price"), Integer
                .parseInt(productId));
        return price != null ? price : 0.0;
    }
    
}
