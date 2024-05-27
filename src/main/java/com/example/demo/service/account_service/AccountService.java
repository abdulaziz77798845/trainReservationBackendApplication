package com.example.demo.service.account_service;

import com.example.demo.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account addAccount(Account account);
    Optional<Account> findAccountById(String accountID);
    void deleteAccountByID(String accountID);
    void delete(Account account);
    List<Account> getAllAccountsInTheRegion();
    void saveAccount(Account account);
}
