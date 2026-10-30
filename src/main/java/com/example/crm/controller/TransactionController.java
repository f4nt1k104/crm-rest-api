package com.example.crm.controller;

import com.example.crm.dto.TransactionResponse;
import com.example.crm.dto.CreateTransactionRequest;
import com.example.crm.dto.UpdateTransactionRequest;
import com.example.crm.model.Transaction;
import com.example.crm.service.SellerService;
import com.example.crm.model.Seller;
import com.example.crm.service.TransactionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/api/transactions")
@Tag(
        name = "Transactions",
        description = "Управление транзакциями CRM"
)
public class TransactionController
{
    private final TransactionService transactionService;


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @Operation(
            summary = "Создание транзакции",
            description = "Создаёт новую транзакцию для активного продавца"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Транзакция успешно создана"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные транзакции"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продавец не найден"
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void postTransaction(@RequestBody CreateTransactionRequest request) {
        transactionService.createTransaction(request);
    }


    @Operation(
            summary = "Получение транзакции",
            description = "Выдаёт транзакцию по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Транзакция успешно получена"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный ID транзакции"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Транзакция не найдена"
            )
    })
    @GetMapping("/{id}")
    public TransactionResponse getTransaction(@PathVariable int id) {
        return transactionService.getTransaction(id);
    }


    @Operation(
            summary = "Получение всех транзакций",
            description = "Выдаёт все транзакции"
    )
    @GetMapping()
    public List<TransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }


    @Operation(
            summary = "Получение транзакции по ID продавца",
            description = "Выдаёт транзакции конкретного продавца"
    )
    @GetMapping("/{id}/transactions")
    public List<TransactionResponse> getSellerTransactions(@PathVariable int id) {
        return transactionService.getTransactionsBySeller(id);
    }


    @Operation(
            summary = "Изменение транзакции",
            description = "Полное изменение транзакции"
    )
    @PutMapping("/{id}")
    public TransactionResponse putTransaction(
            @PathVariable int id,
            @RequestBody UpdateTransactionRequest request
    ) {
        return transactionService.updateTransaction(id, request);
    }


    @Operation(
            summary = "Удаление транзакции",
            description = "Удаление транзакции"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable int id) {
        transactionService.deleteTransaction(id);
    }
}