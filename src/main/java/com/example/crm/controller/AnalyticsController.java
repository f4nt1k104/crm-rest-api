package com.example.crm.controller;

import com.example.crm.dto.TopSellerResponse;
import com.example.crm.model.AnalyticsPeriod;
import com.example.crm.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }


    @Operation(
            summary = "Получение лучшего продавца",
            description = "Возвращает продавца с максимальной суммой продаж за выбранный период"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Лучший продавец найден"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный период"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продажи за выбранный период отсутствуют"
            )
    })
    @GetMapping("/top-seller")
    public TopSellerResponse getTopSeller(@RequestParam AnalyticsPeriod period){
        return analyticsService.getTopSeller(period);
    }
}
