package com.example.crm.repository;

import com.example.crm.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.crm.dto.TopSellerResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySeller_Id(Integer sellerId);
    List<Transaction> findAllByDeletedFalse();
    Optional<Transaction> findByIdAndDeletedFalse(Integer id);
    List<Transaction> findBySeller_IdAndDeletedFalse(Integer sellerId);


    @Query("""
    SELECT new com.example.crm.dto.TopSellerResponse(
        t.seller.id,
        t.seller.name,
        SUM(t.amount)
    )
    FROM Transaction t
    WHERE t.deleted = false
      AND t.seller.deleted = false
      AND t.transactionDate >= :start
      AND t.transactionDate < :end
    GROUP BY t.seller.id, t.seller.name
    ORDER BY SUM(t.amount) DESC
    """)
    List<TopSellerResponse> findSellerSalesRanking(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}