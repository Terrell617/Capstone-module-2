package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(){}

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new JdbcTemplate();
    }


    @Override
    public BigDecimal getBalance(int userId) {
        BigDecimal balance = null;
        String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            balance = results.getBigDecimal("balance");
        }
    return balance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int accountId) {
        return null;
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountToWithdraw, int accountId) {
        return null;
    }

    @Override
    public void updateAccountBalance(AccountDao accountDao) {

    }

    private Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setBalance(rowSet.getBigDecimal("balance"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setAccountId(rowSet.getInt("account_id"));
return account;
        }
    }

