package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;

@Component
public class JdbcTransferDao implements TransferDao {

    private AccountDao accountDao;
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public String sendTransfer(int userFrom, int userTo, BigDecimal amount) {
        if (userFrom == userTo) {
            return "Can't send money to yourself.";
        }
        if (amount.compareTo(accountDao.getBalance(userFrom)) > -1 ) {
            String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES(2,2,?,?,?);";
           jdbcTemplate.update(sql, userFrom, userTo, amount);
           accountDao.addToBalance(amount,userTo);
           accountDao.subtractFromBalance(amount,userFrom);
           return "Transaction Complete.";
        } else {
            return "Transaction failed due to insufficient funds.";
        }
    }

    @Override
    public Transfer findIdByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT username FROM users " +
                "INNER JOIN accounts ON users.user_id = accounts.user_id " +
                "WHERE users.user_id = accounts.user_id AND username ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()) {
            return mapRowToTransfer(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public BigDecimal createTransfer(Transfer newTransfer, Principal principal) {
        return null;
    }


    private BigDecimal getTransfer(int newTransferId) {
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(rowSet.getInt("transfer_id"));
        transfer.setTransfer_type_id(rowSet.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rowSet.getInt("transfer_status_id"));
        transfer.setAccount_to(rowSet.getInt("account_to"));
        transfer.setAccount_from(rowSet.getInt("account_from"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }

}
