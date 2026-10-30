package com.example.crm.service;

import com.example.crm.dto.CreateTransactionRequest;
import com.example.crm.dto.TransactionResponse;
import com.example.crm.dto.UpdateTransactionRequest;
import com.example.crm.model.PaymentType;
import com.example.crm.model.Seller;
import com.example.crm.model.Transaction;
import com.example.crm.repository.SellerRepository;
import com.example.crm.repository.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Проверка успешного создания транзакции")
    void shouldCreateTransactionSuccessfully() {

        Seller seller = new Seller();
        seller.setId(1);
        seller.setName("Ivan");
        seller.setDeleted(false);

        CreateTransactionRequest request =
                new CreateTransactionRequest();

        request.setSellerId(1);
        request.setAmount(new BigDecimal("1500.00"));
        request.setPaymentType(PaymentType.CARD);

        when(sellerRepository.findByIdAndDeletedFalse(1))
                .thenReturn(Optional.of(seller));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.createTransaction(request);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSeller().getId())
                .isEqualTo(1);
        Assertions.assertThat(result.getAmount())
                .isEqualByComparingTo(new BigDecimal("1500.00"));
        Assertions.assertThat(result.getPaymentType())
                .isEqualTo(PaymentType.CARD);
        Assertions.assertThat(result.getTransactionDate())
                .isNotNull();
        Assertions.assertThat(result.isDeleted())
                .isFalse();

        verify(sellerRepository).findByIdAndDeletedFalse(1);
        verify(transactionRepository).save(any(Transaction.class));
    }


    @Test
    @DisplayName("Проверка создания транзакции с отрицательной суммой")
    void shouldRejectTransactionWithNegativeAmount() {

        CreateTransactionRequest request =
                new CreateTransactionRequest();

        request.setSellerId(1);
        request.setAmount(new BigDecimal("-500.00"));
        request.setPaymentType(PaymentType.CARD);

        Assertions.assertThatThrownBy(
                        () -> transactionService.createTransaction(request)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(400);
                });

        verifyNoInteractions(sellerRepository);
        verifyNoInteractions(transactionRepository);
    }


    @Test
    @DisplayName("Проверка создания транзакции с нулевой суммой")
    void shouldRejectTransactionWithZeroAmount() {

        CreateTransactionRequest request =
                new CreateTransactionRequest();

        request.setSellerId(1);
        request.setAmount(BigDecimal.ZERO);
        request.setPaymentType(PaymentType.CARD);

        Assertions.assertThatThrownBy(
                        () -> transactionService.createTransaction(request)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(400);
                });

        verifyNoInteractions(sellerRepository);
        verifyNoInteractions(transactionRepository);
    }


    @Test
    @DisplayName("Проверка создания транзакции с несуществующим продавцом")
    void shouldRejectTransactionWhenSellerNotFound() {

        CreateTransactionRequest request =
                new CreateTransactionRequest();

        request.setSellerId(999);
        request.setAmount(new BigDecimal("1500"));
        request.setPaymentType(PaymentType.CARD);

        when(sellerRepository.findByIdAndDeletedFalse(999))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> transactionService.createTransaction(request)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(404);
                });

        verifyNoInteractions(transactionRepository);
        verify(sellerRepository).findByIdAndDeletedFalse(999);
    }


    @Test
    @DisplayName("Проверка создания транзакции без типа оплаты")
    void shouldRejectTransactionWithoutPaymentType() {

        CreateTransactionRequest request =
                new CreateTransactionRequest();

        request.setSellerId(1);
        request.setAmount(new BigDecimal("1500.00"));
        request.setPaymentType(null);

        Assertions.assertThatThrownBy(
                        () -> transactionService.createTransaction(request)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(400);
                });

        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(sellerRepository);
    }


    @Test
    @DisplayName("Проверка получения существующей транзакции")
    void shouldReturnTransactionWhenExists() {

        Seller seller = new Seller();
        seller.setId(5);
        seller.setName("Ivan");

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setSeller(seller);
        transaction.setAmount(new BigDecimal("1500.00"));
        transaction.setPaymentType(PaymentType.CARD);
        transaction.setTransactionDate(
                LocalDateTime.of(2026, 10, 29, 12, 0)
        );
        transaction.setDeleted(false);

        when(transactionRepository.findByIdAndDeletedFalse(1))
                .thenReturn(Optional.of(transaction));

        TransactionResponse result =
                transactionService.getTransaction(1);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId())
                .isEqualTo(1);
        Assertions.assertThat(result.getSellerId())
                .isEqualTo(5);
        Assertions.assertThat(result.getAmount())
                .isEqualByComparingTo(new BigDecimal("1500.00"));
        Assertions.assertThat(result.getPaymentType())
                .isEqualTo(PaymentType.CARD);
        Assertions.assertThat(result.getTransactionDate())
                .isEqualTo(
                        LocalDateTime.of(2026, 10, 29, 12, 0)
                );

        verify(transactionRepository).findByIdAndDeletedFalse(1);
    }


    @Test
    @DisplayName("Проверка получения несуществующей транзакции")
    void shouldReturn404WhenTransactionNotFound() {

        // Arrange
        when(transactionRepository.findByIdAndDeletedFalse(999))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> transactionService.getTransaction(999)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(404);
                });

        verify(transactionRepository).findByIdAndDeletedFalse(999);
        verifyNoInteractions(sellerRepository);
    }


    @Test
    @DisplayName("Проверка успешного обновления транзакции")
    void shouldUpdateTransactionSuccessfully() {

        Seller oldSeller = new Seller();
        oldSeller.setId(1);
        oldSeller.setName("Ivan");

        Seller newSeller = new Seller();
        newSeller.setId(2);
        newSeller.setName("Petya");

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId(10);
        existingTransaction.setSeller(oldSeller);
        existingTransaction.setAmount(new BigDecimal("1500.00"));
        existingTransaction.setPaymentType(PaymentType.CASH);
        existingTransaction.setTransactionDate(
                LocalDateTime.of(2026, 10, 20, 12, 0));

        UpdateTransactionRequest request = new UpdateTransactionRequest();

        request.setSellerId(2);
        request.setAmount(new BigDecimal("3000"));
        request.setPaymentType(PaymentType.CARD);

        when(transactionRepository.findByIdAndDeletedFalse(10))
                .thenReturn(Optional.of(existingTransaction));
        when(sellerRepository.findByIdAndDeletedFalse(2))
                .thenReturn(Optional.of(newSeller));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransactionResponse result =
                transactionService.updateTransaction(10, request);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId())
                .isEqualTo(10);
        Assertions.assertThat(result.getSellerId())
                .isEqualTo(2);
        Assertions.assertThat(result.getAmount())
                .isEqualByComparingTo(new BigDecimal("3000.00"));
        Assertions.assertThat(result.getPaymentType())
                .isEqualTo(PaymentType.CARD);
        Assertions.assertThat(result.getTransactionDate())
                .isEqualTo(LocalDateTime.of(2026, 10, 20, 12, 0));

        verify(transactionRepository).findByIdAndDeletedFalse(10);
        verify(sellerRepository).findByIdAndDeletedFalse(2);
        verify(transactionRepository).save(any(Transaction.class));
    }


    @Test
    @DisplayName("Проверка обновления несуществующей транзакции")
    void shouldRejectUpdateWhenTransactionNotFound() {

        UpdateTransactionRequest request =
                new UpdateTransactionRequest();

        request.setSellerId(2);
        request.setAmount(new BigDecimal("3000.00"));
        request.setPaymentType(PaymentType.CARD);

        when(transactionRepository.findByIdAndDeletedFalse(999))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> transactionService.updateTransaction(999, request)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(404);
                });

        verify(transactionRepository, never())
                .save(any(Transaction.class));
        verifyNoInteractions(sellerRepository);
    }


    @Test
    @DisplayName("Проверка обновления транзакции с отрицательной суммой")
    void shouldRejectUpdateWithNegativeAmount() {

        UpdateTransactionRequest request =
                new UpdateTransactionRequest();

        request.setSellerId(2);
        request.setAmount(new BigDecimal("-1000.00"));
        request.setPaymentType(PaymentType.CARD);

        Assertions.assertThatThrownBy(
                        () -> transactionService.updateTransaction(10, request)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(400);
                });

        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(sellerRepository);
    }


    @Test
    @DisplayName("Проверка обновления транзакции с несуществующим продавцом")
    void shouldRejectUpdateWhenSellerNotFound() {

        Seller seller = new Seller();
        seller.setId(1);
        seller.setName("Ivan");

        Transaction transaction = new Transaction();
        transaction.setId(10);
        transaction.setSeller(seller);
        transaction.setAmount(new BigDecimal("1500.00"));
        transaction.setPaymentType(PaymentType.CASH);

        UpdateTransactionRequest request =
                new UpdateTransactionRequest();

        request.setSellerId(999);
        request.setAmount(new BigDecimal("3000.00"));
        request.setPaymentType(PaymentType.CARD);

        when(transactionRepository.findByIdAndDeletedFalse(10))
                .thenReturn(Optional.of(transaction));
        when(sellerRepository.findByIdAndDeletedFalse(999))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> transactionService.updateTransaction(10, request)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(404);
                });

        Assertions.assertThat(transaction.getSeller().getId())
                .isEqualTo(1);
        Assertions.assertThat(transaction.getAmount())
                .isEqualByComparingTo(new BigDecimal("1500.00"));
        Assertions.assertThat(transaction.getPaymentType())
                .isEqualTo(PaymentType.CASH);

        verify(transactionRepository, never())
                .save(any(Transaction.class));
        verify(transactionRepository)
                .findByIdAndDeletedFalse(10);
        verify(sellerRepository)
                .findByIdAndDeletedFalse(999);
    }


    @Test
    @DisplayName("Проверка логического удаления транзакции")
    void shouldSoftDeleteTransactionSuccessfully() {

        Seller seller = new Seller();
        seller.setId(1);
        seller.setName("Ivan");

        Transaction transaction = new Transaction();
        transaction.setId(10);
        transaction.setSeller(seller);
        transaction.setAmount(new BigDecimal("1500.00"));
        transaction.setPaymentType(PaymentType.CARD);
        transaction.setDeleted(false);

        when(transactionRepository.findByIdAndDeletedFalse(10))
                .thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(10);

        Assertions.assertThat(transaction.getSeller().getId())
                .isEqualTo(1);
        Assertions.assertThat(transaction.getAmount())
                .isEqualByComparingTo(new BigDecimal("1500.00"));
        Assertions.assertThat(transaction.getPaymentType())
                .isEqualTo(PaymentType.CARD);
        Assertions.assertThat(transaction.isDeleted()).isTrue();

        verify(transactionRepository).findByIdAndDeletedFalse(10);
        verify(transactionRepository).save(any(Transaction.class));
        verifyNoInteractions(sellerRepository);
    }


    @Test
    @DisplayName("Проверка удаления несуществующей транзакции")
    void shouldRejectDeleteWhenTransactionNotFound() {

        when(transactionRepository.findByIdAndDeletedFalse(999))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> transactionService.deleteTransaction(999)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(404);
                });

        verify(transactionRepository)
                .findByIdAndDeletedFalse(999);
        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }


    @Test
    @DisplayName("Проверка получения всех активных транзакций")
    void shouldReturnAllActiveTransactions() {

        Seller seller = new Seller();
        seller.setId(1);
        seller.setName("Ivan");

        LocalDateTime firstDate =
                LocalDateTime.of(2026, 10, 20, 12, 0);
        LocalDateTime secondDate =
                LocalDateTime.of(2026, 10, 21, 15, 30);

        Transaction transactionFirst = new Transaction();
        transactionFirst.setId(10);
        transactionFirst.setSeller(seller);
        transactionFirst.setAmount(new BigDecimal("1500.00"));
        transactionFirst.setPaymentType(PaymentType.CASH);
        transactionFirst.setDeleted(false);
        transactionFirst.setTransactionDate(firstDate);

        Transaction transactionSecond = new Transaction();
        transactionSecond.setId(20);
        transactionSecond.setSeller(seller);
        transactionSecond.setAmount(new BigDecimal("3000.00"));
        transactionSecond.setPaymentType(PaymentType.CARD);
        transactionSecond.setDeleted(false);
        transactionSecond.setTransactionDate(secondDate);

        when(transactionRepository.findAllByDeletedFalse())
                .thenReturn(List.of(transactionFirst, transactionSecond));

        List<TransactionResponse> result =
                transactionService.getAllTransactions();

        Assertions.assertThat(result)
                .hasSize(2);
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getId)
                .containsExactly(10, 20);
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getAmount)
                .containsExactly(new BigDecimal("1500.00"), new BigDecimal("3000.00"));
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getPaymentType)
                .containsExactly(PaymentType.CASH, PaymentType.CARD);
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getSellerId)
                .containsExactly(1, 1);
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getTransactionDate)
                .containsExactly(firstDate, secondDate);

        verify(transactionRepository)
                .findAllByDeletedFalse();
    }


    @Test
    @DisplayName("Проверка получения транзакций конкретного продавца")
    void shouldReturnTransactionsBySeller() {

        Seller seller = new Seller();
        seller.setId(1);
        seller.setName("Ivan");

        LocalDateTime firstDate =
                LocalDateTime.of(2026, 10, 20, 12, 0);
        LocalDateTime secondDate =
                LocalDateTime.of(2026, 10, 21, 15, 30);

        Transaction transactionFirst = new Transaction();
        transactionFirst.setId(10);
        transactionFirst.setSeller(seller);
        transactionFirst.setAmount(new BigDecimal("1500.00"));
        transactionFirst.setPaymentType(PaymentType.CASH);
        transactionFirst.setDeleted(false);
        transactionFirst.setTransactionDate(firstDate);

        Transaction transactionSecond = new Transaction();
        transactionSecond.setId(20);
        transactionSecond.setSeller(seller);
        transactionSecond.setAmount(new BigDecimal("3000.00"));
        transactionSecond.setPaymentType(PaymentType.CARD);
        transactionSecond.setDeleted(false);
        transactionSecond.setTransactionDate(secondDate);

        when(sellerRepository.findByIdAndDeletedFalse(1))
                .thenReturn(Optional.of(seller));
        when(transactionRepository.findBySeller_IdAndDeletedFalse(1))
                .thenReturn(List.of(transactionFirst, transactionSecond));

        List<TransactionResponse> result =
                transactionService.getTransactionsBySeller(1);

        Assertions.assertThat(result)
                .hasSize(2);
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getId)
                .containsExactly(10, 20);
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getSellerId)
                .containsExactly(1, 1);
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getAmount)
                .containsExactly(
                        new BigDecimal("1500.00"),
                        new BigDecimal("3000.00")
                );
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getPaymentType)
                .containsExactly(PaymentType.CASH, PaymentType.CARD);
        Assertions.assertThat(result)
                .extracting(TransactionResponse::getTransactionDate)
                .containsExactly(firstDate, secondDate);

        verify(sellerRepository)
                .findByIdAndDeletedFalse(1);
        verify(transactionRepository)
                .findBySeller_IdAndDeletedFalse(1);
    }


    @Test
    @DisplayName("Проверка получения транзакций несуществующего продавца")
    void shouldRejectGettingTransactionsWhenSellerNotFound() {

        when(sellerRepository.findByIdAndDeletedFalse(999))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> transactionService.getTransactionsBySeller(999)
                )
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException ex =
                            (ResponseStatusException) exception;

                    Assertions.assertThat(ex.getStatusCode().value())
                            .isEqualTo(404);
                });

        verify(sellerRepository).findByIdAndDeletedFalse(999);
        verifyNoInteractions(transactionRepository);
    }
}