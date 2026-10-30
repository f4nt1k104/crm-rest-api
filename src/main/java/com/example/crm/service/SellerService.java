package com.example.crm.service;
import com.example.crm.repository.SellerRepository;

import com.example.crm.model.Seller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class SellerService
{
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }


    public Seller addSeller(Seller seller) {
        seller.setId(null);
        seller.setRegistrationDate(LocalDateTime.now());
        seller.setDeleted(false);


        return sellerRepository.save(seller);
    }


    public Seller getSeller(int id) {
        return sellerRepository.findByIdAndDeletedFalse(id).orElse(null);
    }

    public long getSellerCount(){
        return sellerRepository.countByDeletedFalse();
    }


    public Seller updateSeller(int id, Seller updatedSeller){
        Seller seller = sellerRepository.findByIdAndDeletedFalse(id).orElse(null);
        if(seller != null){
            seller.setName(updatedSeller.getName());
            seller.setContactInfo(updatedSeller.getContactInfo());

            return sellerRepository.save(seller);
        }
        return seller;

    }


    @Transactional
    public boolean deleteSeller(int id) {Seller seller = sellerRepository
            .findByIdAndDeletedFalse(id)
            .orElse(null);


        if (seller == null) {
            return false;
        }


        seller.setDeleted(true);
        sellerRepository.save(seller);


        return true;
    }


    public Collection<Seller> getAllSellers() {
        return sellerRepository.findAllByDeletedFalse();
    }
}
