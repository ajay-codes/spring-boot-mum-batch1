package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.slf4j.Logger;

import com.example.model.Account;
import org.springframework.stereotype.Repository;

/**
 *
 * Repository class for managing accounts using JDBC.
 * author: team-1
 *
 */

@Repository
public class JdbcAccountRepository implements AccountRepository {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("txr-service");

    private DataSource dataSource;

    public JdbcAccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        logger.info("JdbcAccountRepository initialized with DataSource: {}", dataSource);
    }

    public Account findByNumber(String number) {
        logger.info("Finding account by number: {}", number);
        // SQL query to find account by number
        String sql = "SELECT * FROM accounts WHERE number = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, number);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Account account = new Account(
                    rs.getString("number"),
                    rs.getString("holder_name"),
                    rs.getDouble("balance"));
            logger.info("Account found: {}", account);
            return account;
        } catch (Exception e) {
            logger.error("Error finding account by number: {}", number, e);
            throw new RuntimeException("Error finding account by number: " + number, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }

    public void update(Account account) {
        logger.info("Updating account: {}", account);
        // SQL query to update account balance
        String sql = "UPDATE accounts SET balance = ? WHERE number = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setString(2, account.getNumber());
            int rowsUpdated = preparedStatement.executeUpdate();
            logger.info("Account updated: {}, rows affected: {}", account, rowsUpdated);
        } catch (Exception e) {
            logger.error("Error updating account: {}", account, e);
            throw new RuntimeException("Error updating account: " + account, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }

}
