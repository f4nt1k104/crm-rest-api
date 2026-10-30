
package com.example.crm.service;

import com.example.crm.dto.TopSellerResponse;
import com.example.crm.model.AnalyticsPeriod;
import com.example.crm.repository.TransactionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class AnalyticsService {

    private final TransactionRepository transactionRepository;

    public AnalyticsService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TopSellerResponse getTopSeller(AnalyticsPeriod period) {

        LocalDate today = LocalDate.now();

        LocalDate startDate = null;
        LocalDate endDate = null;


        if(period == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Period is null"
            );
        }


        switch (period){
            case DAY:
                startDate = today;
                endDate = today.plusDays(1);
                break;
            case MONTH:
                startDate = today.withDayOfMonth(1);
                endDate = startDate.plusMonths(1);
                break;
            case QUARTER:
                int firstMonth = ((today.getMonthValue() - 1) / 3) * 3 + 1;
                startDate = today.withMonth(firstMonth).withDayOfMonth(1);
                endDate = startDate.plusMonths(3);
                break;
            case YEAR:
                startDate = today.withDayOfYear(1);
                endDate = startDate.plusYears(1);
                break;
        }

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atStartOfDay();

        List<TopSellerResponse> ranking = transactionRepository.findSellerSalesRanking(start, end);


        if(ranking.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Transactions not found"
            );
        }

        return ranking.get(0);
    }
}
