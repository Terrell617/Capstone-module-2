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

        String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfers t " +
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

    @Override
    public List<Transfer> pendingRequests(int userId) {
        List<Transfer> pendingList = new ArrayList<>();
        String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfers t " +
                "INNER JOIN accounts a ON t.account_from = a.account_id " +
                "INNER JOIN accounts b on t.account_to = b.account_id " +
                "INNER JOIN users u ON a.user_id = u.user_id " +
                "INNER JOIN users v ON b.user_id = v.user_id " +
                "WHERE transfer_status_id = 1 AND (account_from  = ? OR account_to = ?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            pendingList.add(transfer);
        }
        return pendingList;
    }

    @Override
    public Transfer findTransferById(int transactionId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT t.*,u.username AS userFrom, v.username AS userTo, tt.transfer_type_desc, ts.transfer_status_desc FROM transfers t" +
                "INNER JOIN accounts a ON t.account_from = a.account_id " +
                "INNER JOIN accounts b on t.account_to = b.account_id " +
                "INNER JOIN users u ON a.user_id = u.user_id " +
                "INNER JOIN users v ON b.user_id = v.user_id " +
                "INNER JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                "INNER JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE t.transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transactionId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public String updateTransferRequest(Transfer transfer, int statusId) {
        if (statusId == 3) {
            String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql,statusId,transfer.getTransfer_id());
            return "Successful update.";
        }
        if (!(accountDao.getBalance(transfer.getAccount_from()).compareTo(transfer.getAmount()) == -1)) {
            String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId,transfer.getTransfer_id());
            accountDao.addToBalance(transfer.getAmount(),transfer.getAccount_to());
            accountDao.subtractFromBalance(transfer.getAmount(),transfer.getAccount_from());
            return "Successfully updated balance.";
        } else {
            return "Insufficient funds in account from. Please deposit money into your account.";
        }
    }

    private Transfer mapRowToTransfer (SqlRowSet rowSet){
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
