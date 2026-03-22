package com.wallet;

import com.wallet.dao.WalletDAO;
import com.wallet.dto.request.WalletRequest;
import com.wallet.dto.response.WalletResponse;
import com.wallet.model.Transaction;
import com.wallet.model.Wallet;
import com.wallet.service.implementation.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletDAO walletDAO;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet testWallet;
    private WalletRequest walletRequest;

    @BeforeEach
    void setUp() {
        testWallet = Wallet.builder()
                .id("001")
                .userId("CUST001")
                .balance(new BigDecimal("5000.00"))
                .build();

        walletRequest = WalletRequest.builder()
                .userId("CUST001")
                .amount(new BigDecimal("1000.00"))
                .build();
    }

    // --- Fund Wallet Tests ---


    @Test
    void fundWallet_Success() {
        when(walletDAO.doesWalletExist("CUST001")).thenReturn(true);
        when(walletDAO.getWalletByUserId("CUST001")).thenReturn(testWallet);
        doNothing().when(walletDAO).saveTransaction(eq("CUST001"), any(Transaction.class)); // 👈 add this

        WalletResponse response = walletService.fundWallet(walletRequest);

        assertEquals(200, response.getResponseCode());
        assertTrue(response.isSuccess());
        verify(walletDAO).updateWallet(eq("CUST001"), any(Wallet.class));
        verify(walletDAO).saveTransaction(eq("CUST001"), any(Transaction.class));
    }

    @Test
    void fundWallet_WalletNotFound() {
        when(walletDAO.doesWalletExist("CUST001")).thenReturn(false);

        WalletResponse response = walletService.fundWallet(walletRequest);

        assertEquals(404, response.getResponseCode());
        assertFalse(response.isSuccess());
    }

    @Test
    void fundWallet_InvalidAmount() {
        when(walletDAO.doesWalletExist("CUST001")).thenReturn(true);
        walletRequest.setAmount(BigDecimal.ZERO);

        WalletResponse response = walletService.fundWallet(walletRequest);

        assertEquals(400, response.getResponseCode());
        assertFalse(response.isSuccess());
    }

    // --- Debit Wallet Tests ---
    @Test
    void debitWallet_Success() {
        when(walletDAO.doesWalletExist("CUST001")).thenReturn(true);
        when(walletDAO.getWalletByUserId("CUST001")).thenReturn(testWallet);
        doNothing().when(walletDAO).saveTransaction(eq("CUST001"), any(Transaction.class)); // 👈 add this

        WalletResponse response = walletService.debitWallet(walletRequest);

        assertEquals(200, response.getResponseCode());
        assertTrue(response.isSuccess());
        verify(walletDAO).updateWallet(eq("CUST001"), any(Wallet.class));
        verify(walletDAO).saveTransaction(eq("CUST001"), any(Transaction.class));
    }

    @Test
    void debitWallet_InsufficientFunds() {
        when(walletDAO.doesWalletExist("CUST001")).thenReturn(true);
        when(walletDAO.getWalletByUserId("CUST001")).thenReturn(testWallet);
        walletRequest.setAmount(new BigDecimal("9000.00")); // more than balance

        WalletResponse response = walletService.debitWallet(walletRequest);

        assertEquals(400, response.getResponseCode());
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Insufficient funds"));
    }

    @Test
    void debitWallet_WalletNotFound() {
        when(walletDAO.doesWalletExist("CUST001")).thenReturn(false);

        WalletResponse response = walletService.debitWallet(walletRequest);

        assertEquals(404, response.getResponseCode());
        assertFalse(response.isSuccess());
    }

    // --- Get Wallet Details Tests ---
    @Test
    void getWalletDetails_Success() {
        when(walletDAO.doesWalletExist("CUST001")).thenReturn(true);
        when(walletDAO.getWalletByUserId("CUST001")).thenReturn(testWallet);

        WalletResponse response = walletService.getWalletDetails(walletRequest);

        assertEquals(200, response.getResponseCode());
        assertTrue(response.isSuccess());
    }

    @Test
    void getWalletDetails_WalletNotFound() {
        when(walletDAO.doesWalletExist("CUST001")).thenReturn(false);

        WalletResponse response = walletService.getWalletDetails(walletRequest);

        assertEquals(404, response.getResponseCode());
        assertFalse(response.isSuccess());
    }

    // --- Transaction History Tests ---
    @Test
    void getTransactionHistory_Success() {
        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .transactionId(UUID.randomUUID().toString())
                        .userId("CUST001")
                        .amount(new BigDecimal("1000.00"))
                        .type("CREDIT")
                        .description("Wallet funded")
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        when(walletDAO.getTransactionHistory("CUST001")).thenReturn(transactions);

        List<Transaction> result = walletService.getTransactionHistory("CUST001");

        assertEquals(1, result.size());
        assertEquals("CREDIT", result.get(0).getType());
    }
}