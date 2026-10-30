package com.example.crm.dto;

import com.example.crm.model.PaymentType;
import com.example.crm.model.Seller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Integer id;
    private Integer sellerId;
    private BigDecimal amount;
    private PaymentType paymentType;
    private LocalDateTime transactionDate;


    public TransactionResponse(Integer id,
                               Integer sellerId,
                               BigDecimal amount,
                               PaymentType paymentType,
                               LocalDateTime transactionDate){
        this.id = id;
        this.sellerId = sellerId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.transactionDate = transactionDate;
    }


    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }

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

    public void setTransactionDate(LocalDateTime transactionDate){
        this.transactionDate = transactionDate;
    }
    public LocalDateTime getTransactionDate(){
        return this.transactionDate;
    }
}