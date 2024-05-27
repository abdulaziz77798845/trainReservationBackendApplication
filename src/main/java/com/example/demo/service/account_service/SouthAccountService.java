package com.example.demo.service.account_service;

import com.example.demo.model.Account;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SouthAccountService implements AccountService {
    @Qualifier("southTemplate")
    @Autowired
    private MongoTemplate southAccountTemplate;

    public Account addAccount(Account account) {
        try {
            southAccountTemplate.insert(account);
            return account;
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to add account", exp);
        }
    }

    public Optional<Account> findAccountById(String accountID){
        try {
            return Optional.ofNullable(southAccountTemplate.findById(accountID, Account.class));
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to find account by ID", exp);
        }
    }

    public void deleteAccountByID(String accountID){
        try {
            Query query = Query.query(Criteria.where("_id").is(new ObjectId(accountID)));
            southAccountTemplate.remove(query, Account.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to delete Account by ID", exp);
        }
    }

    public void delete(Account account){
        try {
            Query query = Query.query(Criteria.where("_id").is(account.getId()));
            southAccountTemplate.remove(query, Account.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to delete account", exp);
        }
    }

    public List<Account> getAllAccountsInTheRegion(){
        try {
            return southAccountTemplate.findAll(Account.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to find all accounts in region", exp);
        }
    }

    public void saveAccount(Account account){
        try {
            southAccountTemplate.save(account);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to save account", exp);
        }
    }
}
