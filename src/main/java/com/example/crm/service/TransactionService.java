
package com.example.crm.service;

import com.example.crm.dto.TransactionResponse;
import com.example.crm.dto.UpdateTransactionRequest;
import com.example.crm.model.Seller;
import com.example.crm.model.Transaction;
import com.example.crm.repository.TransactionRepository;
import com.example.crm.repository.SellerRepository;
import com.example.crm.dto.CreateTransactionRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;


    public TransactionService(
            TransactionRepository transactionRepository,
            SellerRepository sellerRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.sellerRepository = sellerRepository;
    }


    public Transaction createTransaction(CreateTransactionRequest request) {

        Integer sellerId = request.getSellerId();


        if (sellerId == null || sellerId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid seller ID"
            );
        }


        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Amount must be greater than zero"
            );
        }


        if (request.getPaymentType() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment type is required"
            );
        }


        Seller seller = sellerRepository.findByIdAndDeletedFalse(sellerId).orElse(null);

        if (seller == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Seller not found"
            );
        }


        Transaction transaction = new Transaction(
                seller,
                request.getAmount(),
                request.getPaymentType(),
                LocalDateTime.now()
        );


        return transactionRepository.save(transaction);
    }


    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(int id) {
        if (id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transaction ID must be greater than 0"
            );
        }


        Transaction transaction = transactionRepository.findByIdAndDeletedFalse(id).orElse(null);


        if (transaction == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found"
            );
        }

        return new TransactionResponse(transaction.getId(),
                transaction.getSeller().getId(),
                transaction.getAmount(),
                transaction.getPaymentType(),
                transaction.getTransactionDate());
    }





    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {

        List<Transaction> transactions = transactionRepository.findAllByDeletedFalse();

        List<TransactionResponse> responses = new ArrayList<>();

        for (Transaction transaction : transactions) {

            TransactionResponse response = new TransactionResponse(
                    transaction.getId(),
                    transaction.getSeller().getId(),
                    transaction.getAmount(),
                    transaction.getPaymentType(),
                    transaction.getTransactionDate()
            );

            responses.add(response);
        }

        return responses;
    }


    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsBySeller(int sellerId) {

        if (sellerId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seller ID must be greater than 0"
            );
        }

        sellerRepository.findByIdAndDeletedFalse(sellerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Seller not found"
                ));

        List<Transaction> allTransactions = transactionRepository.findBySeller_IdAndDeletedFalse(sellerId);

        List<TransactionResponse> transactions = new ArrayList<>();

        for (Transaction transaction : allTransactions) {

            TransactionResponse response = new TransactionResponse(
                    transaction.getId(),
                    sellerId,
                    transaction.getAmount(),
                    transaction.getPaymentType(),
                    transaction.getTransactionDate()
            );

            transactions.add(response);
        }

        return transactions;
    }



    @Transactional
    public TransactionResponse updateTransaction(
            int id,
            UpdateTransactionRequest request
    )
    {
        if (id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid transaction ID"
            );
        }


        if (request.getSellerId() == null ||
                request.getSellerId() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid seller ID"
            );
        }

        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Amount must be greater than zero"
            );
        }

        if (request.getPaymentType() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment type is required"
            );
        }


        Transaction transaction = transactionRepository
                .findByIdAndDeletedFalse(id)
                .orElse(null);

        if (transaction == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found"
            );
        }


        Seller seller = sellerRepository
                .findByIdAndDeletedFalse(request.getSellerId())
                .orElse(null);

        if (seller == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Seller not found"
            );
        }


        transaction.setSeller(seller);
        transaction.setAmount(request.getAmount());
        transaction.setPaymentType(request.getPaymentType());


        Transaction savedTransaction =
                transactionRepository.save(transaction);


        return new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getSeller().getId(),
                savedTransaction.getAmount(),
                savedTransaction.getPaymentType(),
                savedTransaction.getTransactionDate()
        );
    }


    @Transactional
    public void deleteTransaction(int id) {

        if(id<=0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id must be greater than zero"
            );
        }


        Transaction transaction = transactionRepository
                .findByIdAndDeletedFalse(id)
                .orElse(null);


        if(transaction == null || transaction.isDeleted()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found"
            );
        }


        transaction.setDeleted(true);
        transactionRepository.save(transaction);
    }
}