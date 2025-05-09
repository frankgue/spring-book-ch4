package com.example.spring_book_ch4.dao;

import com.example.spring_book_ch4.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    Account find(long accountId);
    Optional<Account> findByOwnerAndLocked(String ownerName, boolean locked);
    List<Account> find(List<Long> accountId);
    List<Account> find(boolean locked);
    Account find(String ownerName);
    void save(Account account);
    void insert(Account account);
    void update(Account account);
    void delete(long accountId);
}
