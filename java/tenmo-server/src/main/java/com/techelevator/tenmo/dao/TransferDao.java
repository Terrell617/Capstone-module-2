package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;

public interface TransferDao {

    String sendTransfer(int userFrom, int userTo, BigDecimal amount);

    String requestTransfer(int userFrom, int userTo, BigDecimal amount);

    Transfer findIdByUsername(String userName);





}
