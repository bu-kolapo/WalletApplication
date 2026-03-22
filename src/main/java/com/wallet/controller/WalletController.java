package com.wallet.controller;

import com.wallet.dto.request.WalletRequest;
import com.wallet.dto.response.WalletResponse;
import com.wallet.model.Transaction;
import com.wallet.service.WalletService;
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
    public ResponseEntity<WalletResponse> createWallet(@RequestBody WalletRequest walletRequest) {
        return ResponseEntity.ok(walletService.createWallet(walletRequest));
    }

    @PostMapping("/fund")
    public ResponseEntity<WalletResponse> fundWallet(@RequestBody WalletRequest walletRequest) {
        return ResponseEntity.ok(walletService.fundWallet(walletRequest));
    }

    @PostMapping("/debit")
    public ResponseEntity<WalletResponse> debitWallet(@RequestBody WalletRequest walletRequest) {
        return ResponseEntity.ok(walletService.debitWallet(walletRequest));
    }

    @GetMapping("/details")
    public ResponseEntity<WalletResponse> getWalletDetails(@RequestBody WalletRequest walletRequest) {
        return ResponseEntity.ok(walletService.getWalletDetails(walletRequest));
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String userId) {
        return ResponseEntity.ok(walletService.getTransactionHistory(userId));
    }
}
