package com.example.spring_book_ch4.sql;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class AccountInsert extends SqlUpdate {
    public AccountInsert(DataSource dataSource) {
        super(dataSource, "INSERT INTO account(owner_name,balance,access_time,locked) VALUES (?,?,?,?)");
        setParameters(new SqlParameter[]{
                new SqlParameter(Types.VARCHAR),
                new SqlParameter(Types.DOUBLE),
                new SqlParameter(Types.TIMESTAMP),
                new SqlParameter(Types.BOOLEAN)
        });
        setReturnGeneratedKeys(true);
        setGeneratedKeysColumnNames(new String[]{"id"});
        compile();
    }
}
