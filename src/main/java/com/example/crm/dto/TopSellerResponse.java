package com.example.crm.dto;

import java.math.BigDecimal;

public class TopSellerResponse {

    private Integer sellerId;
    private String sellerName;
    private BigDecimal totalAmount;


    public TopSellerResponse(Integer sellerId, String sellerName, BigDecimal totalAmount){
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.totalAmount = totalAmount;
    }


    public Integer getSellerId() {
        return sellerId;
    }
    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
