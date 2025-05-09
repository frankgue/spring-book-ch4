package com.example.spring_book_ch4.dao;

import com.example.spring_book_ch4.exception.DeleteFailedException;
import com.example.spring_book_ch4.exception.InsertFailedException;
import com.example.spring_book_ch4.exception.UpdateFailedException;
import com.example.spring_book_ch4.model.Account;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

public class AccountDaoJdbcImpl implements AccountDao {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private MappingSqlQuery<Account> accountMappingSqlQuery;

    public void setAccountMappingSqlQuery(MappingSqlQuery<Account> accountMappingSqlQuery) {
        this.accountMappingSqlQuery = accountMappingSqlQuery;
    }

    public AccountDaoJdbcImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    /*   public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
           this.jdbcTemplate = jdbcTemplate;
           namedParameterJdbcTemplate= new NamedParameterJdbcTemplate(jdbcTemplate);
       }*/
   /* @Override
    public Account find(long accountId) {
        String sql = "SELECT * FROM account where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{accountId}, new AccountRowMapper());
    }*/
    @Override
    public Account find(long accountId) {
        return accountMappingSqlQuery.findObject(accountId);
    }

    @Override
    public Optional<Account> findByOwnerAndLocked(String ownerName, boolean locked) {
        String sql = "SELECT * FROM account WHERE owner_name = :ownerName AND locked = :locked";

        Map<String, Object> params = new HashMap<>();
        params.put("ownerName", ownerName);
        params.put("locked", locked);

        try {
            Account account = namedParameterJdbcTemplate.queryForObject(sql, params, new AccountRowMapper());
            return Optional.of(account);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }


    }

    @Override
    public List<Account> find(List<Long> accountIds) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("accountIds", accountIds);
        String sql = "SELECT * FROM account where id in (:accountIds)";
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, new AccountRowMapper());
    }

    /* @Override
     public List<Account> find(boolean locked) {
         String sql = "SELECT * FROM account where locked = ?";
         PreparedStatementCreatorFactory psCreatorFactory = new PreparedStatementCreatorFactory(sql, new int[]{Types.BOOLEAN});
         return jdbcTemplate.query(psCreatorFactory.newPreparedStatementCreator(new Object[]{locked}), new AccountRowMapper()) ;
     }*/
    @Override
    public List<Account> find(boolean locked) {
        String sql = "SELECT * FROM account where locked = ?";
        return jdbcTemplate.query(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBoolean(1, locked);
            }
        }, new AccountRowMapper());
    }

    @Override
    public Account find(String ownerName) {
        String sql = "SELECT * FROM account where owner_name = :ownerName";
        return namedParameterJdbcTemplate.queryForObject(sql, Collections.singletonMap("ownerName", ownerName), new AccountRowMapper());
    }

    @Override
    public void save(Account account) {
        String sql = "INSERT INTO account (id, owner_name, balance, access_time, locked) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql, account.getId(), account.getOwnerName(), account.getBalance(), account.getAccessTime(), account.isLocked());
    }

    @Override
    public void insert(Account account) {
        String sql = "INSERT INTO account(owner_name,balance,access_time,locked) values(?,?,?,?)";
        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(sql,
                new int[]{Types.VARCHAR, Types.DOUBLE, Types.TIMESTAMP, Types.BOOLEAN});
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true); // <--- NÉCESSAIRE pour récupérer l'ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int count = jdbcTemplate.update(
                preparedStatementCreatorFactory.newPreparedStatementCreator(new Object[]{
                        account.getOwnerName(), account.getBalance(), account.getAccessTime(), account.isLocked()
                }), keyHolder
        );
        if (count != 1) throw new InsertFailedException("Cannot insert account");
        account.setId(keyHolder.getKey().longValue());
    }

    /*@Override
    public void update(Account account) {
        String sql = "UPDATE account SET owner_name = ?, balance = ?, access_time = ?, locked = ? WHERE id = ?";
        jdbcTemplate.update(sql, account.getOwnerName(), account.getBalance(), account.getAccessTime(), account.isLocked(), account.getId());
    }*/

    @Override
    public void update(Account account) {
        String sql = "UPDATE account SET (owner_name, balance, access_time, locked) = (?,?,?,?) WHERE id = ?";
        int count = jdbcTemplate.update(sql, account.getOwnerName(), account.getBalance(), account.getAccessTime(), account.isLocked(), account.getId());
        if (count != 1) throw new UpdateFailedException("Cannot update account");
    }

    /*@Override
    public void delete(long accountId) {
        String sql = "DELETE FROM account WHERE id = ?";
        jdbcTemplate.update(sql, accountId);
    }*/

    @Override
    public void delete(long accountId) {
        String sql = "DELETE FROM account WHERE id = ?";
        int count = jdbcTemplate.update(sql, accountId);
        if (count != 1) throw new DeleteFailedException("Cannot delete account");
    }

    // RowMapper pour transformer les résultats SQL en objets Account
    private static class AccountRowMapper implements RowMapper<Account> {

        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            Account account = new Account();
            account.setId(rs.getLong("id"));
            account.setOwnerName(rs.getString("owner_name"));
            account.setBalance(rs.getDouble("balance"));
            account.setAccessTime(rs.getTimestamp("access_time"));
            account.setLocked(rs.getBoolean("locked"));
            return account;
        }
    }
}
