package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new JdbcTemplate();
    }


    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
        SqlRowSet
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

    private AccountDao mapRowToAccount(SqlRowSet rowSet){
        AccountDao account = new AccountDao() {
        }
    }
}
