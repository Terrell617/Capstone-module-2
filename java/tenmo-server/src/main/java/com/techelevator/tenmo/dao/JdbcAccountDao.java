package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    //public JdbcAccountDao(){}

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
        BigDecimal balance = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    return balance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int accountId) {
        Account account = null;
        BigDecimal newBalance = account.getBalance().add(amountToAdd);
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.update(sql,newBalance,accountId);
        return account.getBalance();
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountToWithdraw, int accountId) {
        Account account = null;
        BigDecimal newBalance = account.getBalance().subtract(amountToWithdraw);
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.update(sql,newBalance,accountId);
        return account.getBalance();
    }

    @Override
    public int getAccountIdFromUsername(String username) {
        int accountId = 0;
        String sql = "SELECT account_id FROM accounts " +
                "INNER JOIN users ON users.user_id = accounts.user_id " +
                "WHERE username = ?;";
        try {
            accountId = jdbcTemplate.queryForObject(sql, Integer.class, username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return accountId;
    }

    @Override
    public int getAccountIdByUserId(int userId) {
        int accountId = 0;
        String sql = "SELECT account_id FROM accounts " +
                "WHERE user_id = ?;";
        try {
            accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return accountId;
    }

    private Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setBalance(rowSet.getBigDecimal("balance"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setAccountId(rowSet.getInt("account_id"));
        return account;
        }
    }

