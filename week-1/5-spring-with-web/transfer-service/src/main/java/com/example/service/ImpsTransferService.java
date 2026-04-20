package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.repository.AccountRepository;

@Service
public class ImpsTransferService implements TransferService {

    private final AccountRepository accountRepository;
    private final com.example.repository.TransferHistoryRepository transferHistoryRepository;

    public ImpsTransferService(AccountRepository accountRepository,
            com.example.repository.TransferHistoryRepository transferHistoryRepository) {
        this.accountRepository = accountRepository;
        this.transferHistoryRepository = transferHistoryRepository;
    }

    @Transactional
    @Override
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {

        // Fetch accounts
        var fromAccount = accountRepository.findById(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("From account not found"));
        var toAccount = accountRepository.findById(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        // Check balance
        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        // Save updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Save transfer history
        var transferHistory = new com.example.entity.TransferHistory();
        transferHistory.setFromAccountNumber(fromAccount.getNumber());
        transferHistory.setToAccountNumber(toAccount.getNumber());
        transferHistory.setAmount(amount);
        transferHistory.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
        transferHistoryRepository.save(transferHistory);

    }

}
