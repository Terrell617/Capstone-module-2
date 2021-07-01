package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalance(int userId);

    BigDecimal addToBalance(BigDecimal amountToAdd, int accountId);

    BigDecimal subtractFromBalance(BigDecimal amountToWithdraw, int accountId);

    int getAccountIdFromUsername(String username);

    int getAccountIdByUserId(int userId);

}
