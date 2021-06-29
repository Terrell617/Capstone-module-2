package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transfer_id;
    private int transfer_type_id;
    private int transfer_status_id;
    private int amount_from;
    private int amount_to;
    private BigDecimal amount;

    public int getTransfer_id(){
        return transfer_id;
    }
    public void setTransfer_id(int transfer_id){
        this.transfer_id = transfer_id;
    }

    public int getTransfer_type_id(){
        return transfer_type_id;
    }
    public void setTransfer_type_id(int transfer_type_id){
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransfer_status_id(){
        return transfer_status_id;
    }
    public void setTransfer_status_id(int transfer_status_id){
        this.transfer_status_id = transfer_status_id;
    }

    public int getAmount_from(){
        return amount_from;
    }
    public void setAmount_from(int amount_from){
        this.amount_from = amount_from;
    }

    public int getAmount_to(){
        return transfer_id;
    }
    public void setAmount_to(int amount_to){
        this.amount_to = amount_to;
    }


    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
