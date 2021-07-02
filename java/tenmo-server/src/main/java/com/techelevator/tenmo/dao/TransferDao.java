package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;
<<<<<<< HEAD
import java.util.List;
=======
import java.util.HashMap;
import java.util.List;
import java.util.Map;
>>>>>>> bcb34e3bf748c579a24740004f3f854fa4b627d6

public interface TransferDao {

    String sendTransfer(int userFrom, int userTo, BigDecimal amount);

    String requestTransfer(int userFrom, int userTo, BigDecimal amount);

    Transfer findIdByUsername(String userName);

    List<Transfer> listAllTransfers(int userId);
    }

    List<Transfer> listAllTransfers(int userId);

