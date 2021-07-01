package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
        if (amount.compareTo(accountDao.getBalance(userFrom)) > -1) {
            String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES(2,2,?,?,?);";
            jdbcTemplate.update(sql, userFrom, userTo, amount);
            accountDao.addToBalance(amount, userTo);
            accountDao.subtractFromBalance(amount, userFrom);
            return "Transaction Complete.";
        } else {
            return "Transaction failed due to insufficient funds.";
        }
    }

    @Override
    public String requestTransfer(int userFrom, int userTo, BigDecimal amount) {
        if (userFrom == userTo) {
            return "Can't request money from yourself.";
        }
        if (amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES(1,1,?,?,?);";
            jdbcTemplate.update(sql, userFrom, userTo, amount);
            return "Request was sent.";
        } else {
            return "There was a problem sending the request.";
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
    public List<Transfer> listAllTransfers(int userId) {
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfers " +
                "INNER JOIN accounts a ON t.account_from = a.account_id " +
                "INNER JOIN accounts b on t.account_to = b.account_id " +
                "INNER JOIN users u ON a.user_id = u.user_id " +
                "INNER JOIN users v ON b.user_id = v.user_id " +
                "WHERE a.user_id ? OR b.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            list.add(transfer);
        }
        return list;
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
