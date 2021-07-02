package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface TransferDao {

    String sendTransfer(int userFrom, int userTo, BigDecimal amount);

    String requestTransfer(int userFrom, int userTo, BigDecimal amount);

    Transfer findIdByUsername(String userName);

    List<Transfer> listAllTransfers(int userId);

    List<Transfer> pendingRequests(int userId);

    Transfer findTransferById(int transactionId);

    String updateTransferRequest(Transfer transfer, int statusId);
    }



