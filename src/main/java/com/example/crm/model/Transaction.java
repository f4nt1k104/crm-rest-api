package com.example.crm.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;


    public Transaction(){};

    public Transaction(
            Seller seller,
            BigDecimal amount,
            PaymentType paymentType,
            LocalDateTime transactionDate
    ) {
        this.seller = seller;
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

    public void setSeller(Seller seller){
        this.seller = seller;
    }
    public Seller getSeller(){
        return this.seller;
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

    public boolean isDeleted() {
        return this.deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
