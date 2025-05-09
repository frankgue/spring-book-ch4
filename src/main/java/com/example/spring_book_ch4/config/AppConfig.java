package com.example.spring_book_ch4.config;

import com.example.spring_book_ch4.AccountByIdQuery;
import com.example.spring_book_ch4.dao.AccountDao;
import com.example.spring_book_ch4.dao.AccountDaoJdbcImpl;
import com.example.spring_book_ch4.model.Account;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public BasicDataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public AccountDao accountDao() {
        AccountDaoJdbcImpl accountDaoJdbc = new AccountDaoJdbcImpl(dataSource());
        accountDaoJdbc.setAccountMappingSqlQuery(accountMappingSqlQuery());
        return accountDaoJdbc;
//        return new AccountDaoJdbcImpl(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    @Bean
    public MappingSqlQuery<Account> accountMappingSqlQuery(){
        AccountByIdQuery query = new AccountByIdQuery(dataSource());
        return query;
    }

}
