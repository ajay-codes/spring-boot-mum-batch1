package com.example.repository;

public class AccountRepositoryFactory {

    public static AccountRepository createAccountRepository(String repositoryType) {
        if (repositoryType.equalsIgnoreCase("jdbc")) {
            return new JdbcAccountRepository();
        } else if (repositoryType.equalsIgnoreCase("jpa")) {
            return new JpaAccountRepository();
        } else {
            throw new IllegalArgumentException("Invalid repository type: " + repositoryType);
        }
    }

}
