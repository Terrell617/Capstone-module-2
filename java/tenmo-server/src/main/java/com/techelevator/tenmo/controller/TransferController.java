package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;

    public TransferController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }



    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public String sendTransferRequest(@RequestBody Transfer transfer, Principal principal) {
        String loggedInUserName = principal.getName();
        int accountId = accountDao.getAccountIdFromUsername(loggedInUserName);
        int accountFromUserId = transfer.getAccount_from();
        int accountFrom = accountDao.getAccountIdByUserId(accountFromUserId);
        if (accountId != accountFrom) {
        String results = transferDao.requestTransfer(accountFrom, accountId,transfer.getAmount());
        return results;
        } return "Cannot request money from own account or request to account that isn't yours.";
    }

    @RequestMapping(path = "/transfer/users", method = RequestMethod.GET)
    public List<User> findAll(Principal principal){
        return userDao.findAll(principal.getName());
    }

    @RequestMapping(path = "/account/transfer/{id}", method = RequestMethod.GET)
    public List<Transfer> listAllTransfers(@PathVariable int id) {
        List<Transfer> allTransfers = transferDao.listAllTransfers(id);
        return allTransfers;
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getSelectedTransfer(@PathVariable int id) {
        Transfer transfer = transferDao.findTransferById(id);
        return transfer;
    }

    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public String requestTransferRequest(@RequestBody Transfer transfer) {
        String results = transferDao.requestTransfer(transfer.getAccount_from(),transfer.getAccount_to(),transfer.getAmount());
        return results;
    }
    @RequestMapping(path = "/request/{id}", method = RequestMethod.GET)
    public List<Transfer> allTransferRequests(@RequestBody int id) {
        List<Transfer> specificTransfer = transferDao.pendingRequests(id);
        return specificTransfer;
    }

    @RequestMapping(path = "/transfer/status/{statusId}",method = RequestMethod.PUT)
    public String updateRequest(@RequestBody Transfer transfer,@PathVariable int statusId) {
        String output = transferDao.updateTransferRequest(transfer, statusId);
        return output;
    }

}
