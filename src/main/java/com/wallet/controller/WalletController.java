package com.wallet.controller;

import com.wallet.dto.request.WalletRequest;
import com.wallet.dto.response.WalletResponse;
import com.wallet.model.Transaction;
import com.wallet.service.WalletService;
import com.wallet.util.AppUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody WalletRequest walletRequest) {
        WalletResponse response = walletService.createWallet(walletRequest);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PostMapping("/fund")
    public ResponseEntity<WalletResponse> fundWallet(@Valid @RequestBody WalletRequest walletRequest) {
        WalletResponse response = walletService.fundWallet(walletRequest);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PostMapping("/debit")
    public ResponseEntity<WalletResponse> debitWallet(@Valid @RequestBody WalletRequest walletRequest) {
        WalletResponse response = walletService.debitWallet(walletRequest);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/details/{userId}")
    public ResponseEntity<WalletResponse> getWalletDetails(@PathVariable String userId) {
        WalletRequest walletRequest = WalletRequest.builder().userId(userId).build();
        WalletResponse response = walletService.getWalletDetails(walletRequest);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<?> getTransactionHistory(@PathVariable String userId) {
        List<Transaction> transactions = walletService.getTransactionHistory(userId);

        if (transactions.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(AppUtils.walletResponse(404, false, "No transactions found for userId: " + userId));
        }

        return ResponseEntity.status(200).body(transactions);
    }
}
