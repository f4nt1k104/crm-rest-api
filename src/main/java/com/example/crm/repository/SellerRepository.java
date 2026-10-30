package com.example.crm.repository;

import com.example.crm.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerRepository
        extends JpaRepository<Seller, Integer> {
    Optional<Seller> findByIdAndDeletedFalse(Integer id);
    List<Seller> findAllByDeletedFalse();
    long countByDeletedFalse();
}