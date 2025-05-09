package com.example.spring_book_ch4.sql;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class AccountDelete extends SqlUpdate {
    public AccountDelete(DataSource dataSource) {
        super(dataSource, "DELETE account WHERE id = ?");
        setParameters(new SqlParameter[]{
                new SqlParameter(Types.BIGINT)
        });
        compile();
    }
}
