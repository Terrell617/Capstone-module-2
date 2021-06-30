package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;

public interface TransferDao {

    int findIdByUsername(String userName);

    BigDecimal createTransfer(Transfer newTransfer, Principal principal);




}
