package com.example.crm.controller;

import com.example.crm.dto.TransactionResponse;
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
@RequestMapping("/api/sellers")
@Tag(
        name = "Sellers",
        description = "Управление продавцами CRM"
)
public class SellerController
{
    private final SellerService sellerService;
    private final TransactionService transactionService;

    public SellerController(SellerService sellerService, TransactionService transactionService) {
        this.sellerService = sellerService;
        this.transactionService = transactionService;
    }


    @Operation(
            summary = "Получение количества продавцов",
            description = "Выводит количество продавцов"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Количество продавцов успешно выведен"
            ),
    })
    @GetMapping("/count")
    public Map<String, Long> sellersCount(){
        Map<String, Long> data = Map.of("count", sellerService.getSellerCount());
        return data;
    }


    @Operation(
            summary = "Получить продавца",
            description = "Возвращает продавца по его ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Продавец успешно выведен"
            ),
    })
    @GetMapping("/{id}")
    public Seller getSeller(@PathVariable int id) {
        if (id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seller ID must be greater than 0"
            );
        }

        Seller seller = sellerService.getSeller(id);

        if (seller == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Seller not found"
            );
        }

        return seller;
    }


    @Operation(
            summary = "Добавить продавца",
            description = "Добавляет продавца с переданными данными"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Продавец успешно добавлен"
            ),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Seller postSeller(@RequestBody Seller seller){
        return sellerService.addSeller(seller);
    }


    @Operation(
            summary = "Обновление продавца",
            description = "Изменяет имя и контактные данные продавца"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Продавец успешно изменен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный ID"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продавец не найден"
            )
    })
    @PutMapping("/{id}")
    public Seller putSeller(@PathVariable int id, @RequestBody Seller seller){
        if (id <= 0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seller ID must be greater than 0");
        }

        Seller updatedSeller = sellerService.updateSeller(id, seller);

        if(updatedSeller == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found");
        }

        return updatedSeller;
    }


    @Operation(summary = "Удаление продавца")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Продавец успешно удалён"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный ID"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продавец не найден"
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeller(@PathVariable int id){
        if (id <= 0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seller ID must be greater than 0");
        }

        boolean deleted = sellerService.deleteSeller(id);

        if (!deleted){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found");
        }
    }


    @Operation(
            summary = "Получение всех продавцов",
            description = "Выдаёт всех продавцов"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Все продавцы успешно выведены"
            ),
    })
    @GetMapping()
    public Collection<Seller> getAllSellers(){
        return sellerService.getAllSellers();
    }


}
