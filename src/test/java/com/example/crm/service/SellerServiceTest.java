package com.example.crm.service;

import com.example.crm.model.Seller;
import com.example.crm.repository.SellerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SellerService sellerService;


    @Test
    @DisplayName("Проверка получения количества активных продавцов")
    void shouldReturnActiveSellersCount() {

        when(sellerRepository.countByDeletedFalse()).thenReturn(3L);

        long result = sellerService.getSellerCount();

        Assertions.assertThat(result).isEqualTo(3L);

        verify(sellerRepository).countByDeletedFalse();
    }


    @Test
    @DisplayName("Проверка получения существующего продавца")
    void shouldReturnSellerWhenExists() {

        Seller seller = new Seller();
        seller.setId(1);
        seller.setName("Ivan");

        when(sellerRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(seller));

        Seller result = sellerService.getSeller(1);

        Assertions.assertThat(result).isNotNull();

        Assertions.assertThat(result.getId()).isEqualTo(1);

        Assertions.assertThat(result.getName()).isEqualTo("Ivan");

        verify(sellerRepository).findByIdAndDeletedFalse(1);
    }


    @Test
    @DisplayName("Проверка получения несуществующего продавца")
    void shouldReturnNullWhenSellerNotFound() {

        when(sellerRepository.findByIdAndDeletedFalse(999)).thenReturn(Optional.empty());

        Seller result = sellerService.getSeller(999);

        Assertions.assertThat(result).isNull();

        verify(sellerRepository).findByIdAndDeletedFalse(999);
    }



    @Test
    @DisplayName("Проверка создания нового продавца")
    void shouldCreateSellerSuccessfully() {

        Seller seller = new Seller();
        seller.setName("Ivan");
        seller.setDeleted(true);
        seller.setId(100);

        when(sellerRepository.save(any(Seller.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Seller result = sellerService.addSeller(seller);

        Assertions.assertThat(result).isNotNull();

        Assertions.assertThat(result.getId()).isNull();
        Assertions.assertThat(result.getName()).isEqualTo("Ivan");
        Assertions.assertThat(result.isDeleted()).isFalse();
        Assertions.assertThat(result.getRegistrationDate()).isNotNull();

        ArgumentCaptor<Seller> captor =
                ArgumentCaptor.forClass(Seller.class);

        verify(sellerRepository).save(captor.capture());

        Seller capturedSeller = captor.getValue();

        Assertions.assertThat(capturedSeller.getId()).isNull();
        Assertions.assertThat(capturedSeller.isDeleted()).isFalse();
        Assertions.assertThat(capturedSeller.getName()).isEqualTo("Ivan");
    }


    @Test
    @DisplayName("Проверка успешного обновления продавца")
    void shouldUpdateSellerSuccessfully() {

        Seller existingSeller = new Seller();
        existingSeller.setId(1);
        existingSeller.setName("Ivan");

        LocalDateTime registrationDate =
                LocalDateTime.of(2026, 10, 1, 12, 0);

        existingSeller.setRegistrationDate(registrationDate);

        Seller updatedSeller = new Seller();

        updatedSeller.setName("Petr");
        updatedSeller.setContactInfo("petr@mail.com");

        when(sellerRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(existingSeller));
        when(sellerRepository.save(any(Seller.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Seller result = sellerService.updateSeller(1, updatedSeller);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Assertions.assertThat(result.getName()).isEqualTo("Petr");
        Assertions.assertThat(result.getContactInfo()).isEqualTo("petr@mail.com");
        Assertions.assertThat(result.getRegistrationDate())
                .isEqualTo(registrationDate);

        verify(sellerRepository).findByIdAndDeletedFalse(1);
        verify(sellerRepository).save(existingSeller);
    }


    @Test
    @DisplayName("Проверка обновления несуществующего продавца")
    void shouldReturnNullWhenUpdatingNonExistingSeller() {

        Seller updatedSeller = new Seller();
        updatedSeller.setName("Petr");
        updatedSeller.setContactInfo("petr@mail.com");

        when(sellerRepository.findByIdAndDeletedFalse(999))
                .thenReturn(Optional.empty());

        Seller result = sellerService.updateSeller(999, updatedSeller);

        Assertions.assertThat(result).isNull();

        verify(sellerRepository).findByIdAndDeletedFalse(999);
        verify(sellerRepository, never())
                .save(any(Seller.class));
    }


    @Test
    @DisplayName("Проверка логического удаления продавца")
    void shouldSoftDeleteSellerSuccessfully() {

        Seller seller = new Seller();
        seller.setId(1);
        seller.setName("Ivan");
        seller.setDeleted(false);

        when(sellerRepository.findByIdAndDeletedFalse(1))
                .thenReturn(Optional.of(seller));

        sellerService.deleteSeller(1);

        Assertions.assertThat(seller.isDeleted()).isTrue();

        verify(sellerRepository).save(seller);
        verify(sellerRepository).findByIdAndDeletedFalse(1);
        verify(sellerRepository, never()).delete(any(Seller.class));
    }



    @Test
    @DisplayName("Проверка удаления несуществующего продавца")
    void shouldNotDeleteSellerWhenNotFound() {

        when(sellerRepository.findByIdAndDeletedFalse(999))
                .thenReturn(Optional.empty());

        sellerService.deleteSeller(999);

        verify(sellerRepository).findByIdAndDeletedFalse(999);
        verify(sellerRepository, never()).save(any(Seller.class));
    }


    @Test
    @DisplayName("Проверка получения списка активных продавцов")
    void shouldReturnAllActiveSellers() {

        Seller seller1 = new Seller();
        seller1.setId(1);
        seller1.setName("Ivan");

        Seller seller2 = new Seller();
        seller2.setId(2);
        seller2.setName("Petr");

        when(sellerRepository.findAllByDeletedFalse())
                .thenReturn(List.of(seller1, seller2));

        Collection<Seller> result = sellerService.getAllSellers();

        Assertions.assertThat(result)
                .extracting(Seller::getName)
                .containsExactly("Ivan", "Petr");

        verify(sellerRepository).findAllByDeletedFalse();
    }
}