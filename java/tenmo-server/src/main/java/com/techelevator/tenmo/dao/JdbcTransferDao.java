package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.Principal;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int findIdByUsername(String userName) {
        String sql = "SELECT username FROM users " +
                "INNER JOIN accounts ON users.user_id = accounts.user_id "+
                "WHERE users.user_id = accounts.user_id;";
    }

    @Override
    public BigDecimal createTransfer(Transfer newTransfer, Principal principal) {
//        Transfer transfer = null;
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "+
                "VALUES(2,2,?,?,?) " +
                "RETURNING transfer_id;";
        int newTransferId = jdbcTemplate.queryForObject(sql, int.class, newTransfer.getTransfer_type_id(),
                newTransfer.getTransfer_status_id(), newTransfer.getAccount_from(), newTransfer.getAccount_to(), newTransfer.getAmount());
        return getTransfer(newTransferId);

    }

    private Transfer mapRowToTranfer (SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(rowSet.getInt("transfer_id"));
        transfer.setTransfer_type_id(rowSet.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rowSet.getInt("transfer_status_id"));
        transfer.setAccount_to(rowSet.getInt("account_to"));
        transfer.setAccount_from(rowSet.getInt("account_from"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
    }

}
