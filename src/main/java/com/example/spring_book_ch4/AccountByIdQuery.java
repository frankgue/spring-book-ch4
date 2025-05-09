package com.example.spring_book_ch4;

import com.example.spring_book_ch4.model.Account;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class AccountByIdQuery extends MappingSqlQuery<Account> {

    public AccountByIdQuery(DataSource dataSource) {
        super(dataSource, "SELECT id,owner_name,balance,access_time,locked FROM account where id = ?");
        declareParameter(new SqlParameter(Types.BIGINT));
        compile();
    }

    @Override
    protected Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setOwnerName(rs.getString("owner_name"));
        account.setBalance(rs.getDouble("balance"));
        account.setAccessTime(rs.getTimestamp("access_time"));
        account.setLocked(rs.getBoolean("locked"));
        return account;
    }
}
