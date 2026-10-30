package com.example.crm.dto;

import com.example.crm.model.PaymentType;

import java.math.BigDecimal;

public class CreateTransactionRequest
{
    private Integer sellerId;
    private BigDecimal amount;
    private PaymentType paymentType;


    public CreateTransactionRequest(){}


    public void setSellerId(Integer sellerId){
        this.sellerId = sellerId;
    }
    public Integer getSellerId(){
        return this.sellerId;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public BigDecimal getAmount(){
        return this.amount;
    }

    public void setPaymentType(PaymentType paymentType){
        this.paymentType = paymentType;
    }
    public PaymentType getPaymentType(){
        return this.paymentType;
    }
}