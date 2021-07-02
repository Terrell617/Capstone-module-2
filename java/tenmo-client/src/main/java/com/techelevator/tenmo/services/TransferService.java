package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {
    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        BASE_URL = url;
    }

    public Transfer[] transferList() {
        Transfer[] output = null;
        try {
            output = restTemplate.exchange(BASE_URL + "/account/transfer/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            System.out.println("------------------------------------------\n" +
                    "Transfers\n" +
                    "ID        From/To          Amount\n" +
                    "-----------------------------------------\n");
            String fromOrTo = "";
            String name = "";
            for (Transfer t : output) {
                if (currentUser.getUser().getId() == t.getAccountFrom()) {
                    fromOrTo = "From: ";
                    name = t.getUserTo();

                } else {
                    fromOrTo = "To: ";
                    name = t.getUserFrom();
                }
                System.out.println(t.getTransferId() + fromOrTo + name + "$" + t.getAmount());
            }
            System.out.println("------------------------\n" +
                    "Please Enter Transfer Id to View Details (0 to cancel): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (Integer.parseInt(input) != 0) {
                boolean doesTransferIdExist = false;
                System.out.println("Must Be Valid Transfer");
                for (Transfer t : output) {
                    if (Integer.parseInt(input) == t.getTransferId()) {
                        Transfer newOutput = restTemplate.exchange(BASE_URL + "transfer/" + t.getTransferId(), HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
                        doesTransferIdExist = true;
                        System.out.println("----------------------------------------\n"+
                                "Transfer Details \n"+
                                "---------------------------------------\n"+
                                "Id: " + newOutput.getTransferId() + "\n" +
                                "From: " + newOutput.getUserFrom() + "\n"+
                                "To: " + newOutput.getUserTo() + "\n" +
                                "Type: " + newOutput.getTransferType() + "\n" +
                                "Status: " + newOutput.getTransferStatus() + "\n" +
                                "Amount: " + newOutput.getAmount());
                    }
                }
                if (!doesTransferIdExist){
                    System.out.println("Transfer ID Does Not Exist.");
                }
            }
        } catch (Exception e) {
        System.out.println("Thank you, please come back.");
        }
        return output;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }


}
