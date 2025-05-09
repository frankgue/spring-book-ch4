package com.example.spring_book_ch4;

import com.example.spring_book_ch4.config.AppConfig;
import com.example.spring_book_ch4.dao.AccountDao;
import com.example.spring_book_ch4.model.Account;
import com.example.spring_book_ch4.sql.storeProcedure.ConcatStoredProcedure;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        AccountDao accountDao = context.getBean(AccountDao.class);

        /*ConcatStoredProcedure storedProcedure = context.getBean(ConcatStoredProcedure.class);
        String result = storedProcedure.execute("Hello ", "world");
        System.out.println(result);*/

        // Créer un compte
        Account account = new Account();
        account.setId(1);
        account.setOwnerName("John Doe");
        account.setBalance(500.0);
        account.setAccessTime(new java.util.Date());
        account.setLocked(false);

        Account account1 = new Account();
        account1.setOwnerName("Joe Smith");
        account1.setBalance(20.0);
        account1.setAccessTime(new Date());
        account1.setLocked(false);

        // Sauvegarder le compte
        accountDao.save(account);

        accountDao.insert(account1);

        // Récupérer le compte
        Account foundAccount = accountDao.find(1);
        System.out.println("Found Account: " + foundAccount.getOwnerName() + " with balance " + foundAccount.getBalance());

        // Mettre à jour le compte
        foundAccount.setBalance(1000.0);
        accountDao.update(foundAccount);

        // Récupérer à nouveau le compte
        Account updatedAccount = accountDao.find(1);
        System.out.println("Updated Account: " + updatedAccount.getOwnerName() + " with balance " + updatedAccount.getBalance());

        // Supprimer le compte
        /*accountDao.delete(1);
        System.out.println("Account deleted.");*/

        Account foundAccountByOwner = accountDao.find("John Doe");
        System.out.println("foundAccountByOwner");
        System.out.println("found Account By Owner Name: " + foundAccountByOwner.getOwnerName() + " with balance " + foundAccountByOwner.getBalance());


        List<Account> foundAccountByLockedStatus = accountDao.find(false);
        System.out.println("foundAccountByLockedStatus");
        System.out.println("found Account By Status Locked: " + foundAccountByLockedStatus.size() + " with balance " + foundAccountByLockedStatus.get(0).getBalance());

        Account account2 = new Account();
        account2.setOwnerName("Frank GUEKENG");
        account2.setBalance(100.0);
        account2.setAccessTime(new Date());
        account2.setLocked(false);


        accountDao.insert(account2);

        account2 = accountDao.find(account2.getId());

        System.out.println(account2.getId());
        System.out.println(account2.getOwnerName());
        System.out.println(account2.getBalance());
        System.out.println(account2.getAccessTime());
        System.out.println(account2.isLocked());

        account2.setBalance(5000);
        accountDao.update(account2);

        account2 = accountDao.find(account2.getId());

        System.out.println(account2.getId());
        System.out.println(account2.getOwnerName());
        System.out.println(account2.getBalance());

        List<Account> accounts = accountDao.find(Arrays.asList(account2.getId()));
        System.out.println(accounts.size());

        System.out.println("Account deleted account2.getId()." + account2.getId());
        accountDao.delete(account2.getId());
        System.out.println("Account deleted.");
         accounts = accountDao.find(Arrays.asList(account2.getId()));
        System.out.println(accounts.size());

        context.close();
    }
}
