package com.wallet.dao;

import com.wallet.model.Transaction;
import com.wallet.model.Wallet;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Repository
public class WalletDAO {

    private final Map<String, Wallet> walletDb = new ConcurrentHashMap<>();
    private final Map<String, List<Transaction>> transactionDb = new ConcurrentHashMap<>();
//
//    private void loadSampleRequest() {
//        walletDb.put("CUST001", new Wallet("001", "CUST001", new BigDecimal("5000.00")));
//        walletDb.put("CUST002", new Wallet("002", "CUST002", new BigDecimal("7000.00")));
//        walletDb.put("CUST003", new Wallet("003", "CUST003", new BigDecimal("10000.00")));
//    }
    private void loadSampleRequest() {
        walletDb.put("CUST001", Wallet.builder()
                .id("001")
                .userId("CUST001")
                .balance(new BigDecimal("5000.00"))
                .build());

        walletDb.put("CUST002", Wallet.builder()
                .id("002")
                .userId("CUST002")
                .balance(new BigDecimal("7000.00"))
                .build());

        walletDb.put("CUST003", Wallet.builder()
                .id("003")
                .userId("CUST003")
                .balance(new BigDecimal("10000.00"))
                .build());
    }
    public  WalletDAO(){
        loadSampleRequest();
    }

    public boolean doesWalletExist(String userId) {
        return walletDb.values().stream()
                .anyMatch(wallet -> wallet.getUserId().equals(userId));
    }
    public void saveWallet(String userId, Wallet wallet) {
        walletDb.put(userId, wallet);
        transactionDb.put(userId, new ArrayList<>());  // init empty transaction list
    }

    // Get wallet by userId
    public Wallet getWalletByUserId(String userId) {
        return walletDb.get(userId);
    }

    // Update wallet after fund/debit
    public void updateWallet(String userId, Wallet wallet) {
        walletDb.put(userId, wallet);
    }

    public void saveTransaction(String userId, Transaction transaction) {
        transactionDb.computeIfAbsent(userId, k -> new ArrayList<>()).add(transaction);
    }

    public List<Transaction> getTransactionHistory(String userId) {
        return transactionDb.getOrDefault(userId, new ArrayList<>());
    }
}
