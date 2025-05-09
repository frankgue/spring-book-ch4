package com.example.spring_book_ch4.sql;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class AccountUpdate extends SqlUpdate {
    public AccountUpdate(DataSource dataSource) {
        super(dataSource, "UPDATE account SET (owner_name,balance,access_time,locked) = (?,?,?,?) WHERE id = ?");
        setParameters(new SqlParameter[]{
                new SqlParameter(Types.VARCHAR),
                new SqlParameter(Types.DOUBLE),
                new SqlParameter(Types.TIMESTAMP),
                new SqlParameter(Types.BOOLEAN),
                new SqlParameter(Types.BIGINT)
        });
        compile();
    }
}
