package com.example.demo.service.account_service;

import com.example.demo.model.Account;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WestAccountServiceTest {
    @InjectMocks
    private WestAccountService westAccountService;

    @Qualifier("westTemplate")
    @Mock
    private MongoTemplate westAccountMongoTemplate;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addAccount(){
        Account account = new Account();
        account.setId("662f49b1fa76462748ffa731");
        account.setAccountHolderName("VIRAT KHOLI");
        account.setAccountNumber("977586755647456456");
        account.setCVV("899");
        account.setBalance(100000.0);


        when(westAccountMongoTemplate.insert(account)).thenReturn(account);

        Account savedAccount = westAccountService.addAccount(account);

        assertNotNull(savedAccount);
        assertEquals(account.getId(), savedAccount.getId());
        assertEquals(account.getAccountHolderName(), savedAccount.getAccountHolderName());
        assertEquals(account.getAccountNumber(), savedAccount.getAccountNumber());
        assertEquals(account.getCVV(), savedAccount.getCVV());
        assertEquals(account.getBalance(), savedAccount.getBalance());
        verify(westAccountMongoTemplate,times(1)).insert(account);
    }

    @Test
    void addAccount_exception_is_account_holder_name_is_empty(){
        Account account = new Account();
        account.setAccountHolderName("");

        when(westAccountMongoTemplate.insert(account)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> westAccountService.addAccount(account));
        assertEquals("Failed to add account", exception.getMessage());
    }

    @Test
    void addAccount_exception_is_account_holder_name_is_null(){
        Account account = new Account();
        account.setAccountHolderName("");

        when(westAccountMongoTemplate.insert(account)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> westAccountService.addAccount(account));
        assertEquals("Failed to add account", exception.getMessage());
    }

    @Test
    void findAccountById() {
        Account account = new Account();
        account.setId("662f49b1fa76462748ffa731");

        when(westAccountMongoTemplate.findById("662f49b1fa76462748ffa731", Account.class)).thenReturn(account);

        Optional<Account> foundAccount = westAccountService.findAccountById(account.getId());

        assertNotNull(foundAccount);
        assertEquals(foundAccount.get().getId(), account.getId());
    }

    @Test
    void findAccountById_exception_is_account_id_is_empty(){
        when(westAccountMongoTemplate.findById("", Account.class)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> westAccountService.findAccountById(""));
        assertEquals("Failed to find account by ID", exception.getMessage());
    }


    @Test
    void deleteAccountByID() {
        String accountID="6630ba4534a44e7b63c63895";
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(accountID)));
        when(westAccountMongoTemplate.remove(query, Account.class)).thenReturn(null);

        westAccountService.deleteAccountByID(accountID);
        verify(westAccountMongoTemplate, times(1)).remove(query, Account.class);
    }

    @Test
    void deleteAccountByID_exception(){
        String accountID = "6630ba4534a44e7b63c63895";
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(accountID)));
        doThrow(new DataAccessException("...") {}).when(westAccountMongoTemplate).remove(query, Account.class);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            westAccountService.deleteAccountByID(accountID);
        });

        assertEquals("Failed to delete Account by ID", exception.getMessage());
        verify(westAccountMongoTemplate, times(1)).remove(query, Account.class);
    }

    @Test
    void delete() {
        Account account = new Account();
        account.setId("6630ba4534a44e7b63c63891");

        when(westAccountMongoTemplate.findById("6630ba4534a44e7b63c63891", Account.class)).thenReturn(null);


        westAccountService.delete(account);
        verify(westAccountMongoTemplate,times(1)).remove(new Query(Criteria.where("_id").is(account.getId())), Account.class);
    }

    @Test
    public void delete_exception(){
        Account account = new Account();
        account.setId(null);
        Query query = Query.query(Criteria.where("_id").is(account.getId()));
        doThrow(new DataAccessException("...") {}).when(westAccountMongoTemplate).remove(query, Account.class);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            westAccountService.delete(account);
        });


        assertEquals("Failed to delete account", exception.getMessage());
        verify(westAccountMongoTemplate, times(1)).remove(query, Account.class);
    }

    @Test
    void getAllAccountsInTheRegion() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account());
        accounts.add(new Account());

        when(westAccountMongoTemplate.findAll(Account.class)).thenReturn(accounts);

        List<Account> foundAccounts = westAccountService.getAllAccountsInTheRegion();
        assertNotNull(foundAccounts);
        assertEquals(foundAccounts.size(), accounts.size());
    }

    @Test
    public void getAllAccountsInTheRegion_exception() {
        when(westAccountMongoTemplate.findAll(Account.class)).thenThrow(new DataAccessException("...") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> westAccountService.getAllAccountsInTheRegion());

        assertEquals("Failed to find all accounts in region", exception.getMessage());
    }


    @Test
    public void saveAccount(){
        Account account = new Account();
        account.setId("662f49b1fa76462748ffa731");
        account.setAccountHolderName("VIRAT KHOLI");
        account.setAccountNumber("977586755647456456");
        account.setCVV("899");
        account.setBalance(100000.0);

        when(westAccountMongoTemplate.save(account)).thenReturn(account);

        westAccountService.saveAccount(account);
        verify(westAccountMongoTemplate,times(1)).save(account);
    }


    @Test
    public void savaAccount_exception_accountId_is_null() {
        Account account = new Account();
        account.setId(null);
        when(westAccountMongoTemplate.save(account)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> westAccountService.saveAccount(account));

        assertEquals("Failed to save account", exception.getMessage());
    }
}