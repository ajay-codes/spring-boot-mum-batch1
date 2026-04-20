package com.example.repository;

import com.example.model.Account;

public interface AccountRepository {
    Account findByNumber(String number);

    void update(Account account);
}
