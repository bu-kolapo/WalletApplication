package com.wallet.service.implementation;

import com.wallet.dao.WalletDAO;
import com.wallet.dto.request.WalletRequest;
import com.wallet.dto.response.WalletResponse;
import com.wallet.exception.WalletNotFoundException;
import com.wallet.model.Transaction;
import com.wallet.model.Wallet;
import com.wallet.service.WalletService;
import com.wallet.util.AppUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {



    private final WalletDAO walletDAO;

    public WalletServiceImpl(WalletDAO walletDAO) {
        this.walletDAO = walletDAO;
    }

    @Override
    public WalletResponse createWallet(WalletRequest walletRequest) {
        try {
            if (walletDAO.doesWalletExist(walletRequest.getUserId())) {
                return AppUtils.walletResponse(400, false, "Wallet already exists for userId: " + walletRequest.getUserId());
            }

            String userId = "CUST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String walletId = "WAL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            Wallet wallet = Wallet.builder()
                    .id(walletId)
                    .userId(userId)
                    .balance(BigDecimal.ZERO)
                    .build();

            walletDAO.saveWallet(userId, wallet);
            return AppUtils.walletResponse(201, true, "Wallet created successfully. UserId: " + userId);

        } catch (Exception ex) {
            return AppUtils.walletResponse(500, false, "Error creating wallet: " + ex.getMessage());
        }
    }


    @Override
    public WalletResponse fundWallet(WalletRequest walletRequest) {
        try {
            // Rule 1: Wallet must exist
            checkIfWalletExist(walletRequest.getUserId());

            // Rule 2: Amount must be greater than 0
            if (walletRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return AppUtils.walletResponse(400, false, "Funding amount must be greater than 0");
            }

            // Fund the wallet
            Wallet wallet = walletDAO.getWalletByUserId(walletRequest.getUserId());
            wallet.setBalance(wallet.getBalance().add(walletRequest.getAmount()));
            walletDAO.updateWallet(walletRequest.getUserId(), wallet);

            walletDAO.saveTransaction(walletRequest.getUserId(), Transaction.builder()
                    .transactionId(UUID.randomUUID().toString())
                    .userId(walletRequest.getUserId())
                    .amount(walletRequest.getAmount())
                    .type("CREDIT")
                    .description("Wallet funded")
                    .timestamp(LocalDateTime.now())
                    .build());

            return AppUtils.walletResponse(200, true, "Wallet funded successfully. New balance: " + wallet.getBalance());

        } catch (WalletNotFoundException ex) {
            return AppUtils.walletResponse(404, false, ex.getMessage());
        }
    }

    @Override
    public WalletResponse debitWallet(WalletRequest walletRequest) {
        try {
            // Rule 1: Wallet must exist
            checkIfWalletExist(walletRequest.getUserId());

            // Rule 2: Amount must be greater than 0
            if (walletRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return AppUtils.walletResponse(400, false, "Debit amount must be greater than 0");
            }

            // Rule 3: Cannot debit beyond available balance
            Wallet wallet = walletDAO.getWalletByUserId(walletRequest.getUserId());
            if (walletRequest.getAmount().compareTo(wallet.getBalance()) > 0) {
                return AppUtils.walletResponse(400, false, "Insufficient funds. Available balance: " + wallet.getBalance());
            }

            // Debit the wallet
            wallet.setBalance(wallet.getBalance().subtract(walletRequest.getAmount()));
            walletDAO.updateWallet(walletRequest.getUserId(), wallet);

            walletDAO.saveTransaction(walletRequest.getUserId(), Transaction.builder()
                    .transactionId(UUID.randomUUID().toString())
                    .userId(walletRequest.getUserId())
                    .amount(walletRequest.getAmount())
                    .type("DEBIT")
                    .description("Wallet debited")
                    .timestamp(LocalDateTime.now())
                    .build());

            return AppUtils.walletResponse(200, true, "Debit successful. New balance: " + wallet.getBalance());

        } catch (WalletNotFoundException ex) {
            return AppUtils.walletResponse(404, false, ex.getMessage());
        }
    }

    @Override
    public WalletResponse getWalletDetails(WalletRequest walletRequest) {
        try {
            // Rule 1: Wallet must exist
            checkIfWalletExist(walletRequest.getUserId());

            Wallet wallet = walletDAO.getWalletByUserId(walletRequest.getUserId());
            return AppUtils.walletResponse(200, true, "Wallet Details - UserId: " + wallet.getUserId()
                    + " | WalletId: " + wallet.getId()
                    + " | Balance: " + wallet.getBalance());

        } catch (WalletNotFoundException ex) {
            return AppUtils.walletResponse(404, false, ex.getMessage());
        }
    }

    private void checkIfWalletExist(String userId) throws WalletNotFoundException {

        if (!walletDAO.doesWalletExist(userId)) {
            throw new WalletNotFoundException("Wallet not found for userId");
        }

    }

    @Override
    public List<Transaction> getTransactionHistory(String userId) {
        return walletDAO.getTransactionHistory(userId);
    }
}
