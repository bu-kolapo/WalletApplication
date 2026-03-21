package com.wallet.controller;

import com.wallet.dto.request.WalletRequest;
import com.wallet.dto.response.WalletResponse;
import com.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet(@RequestBody WalletRequest walletRequest) {
        return ResponseEntity.ok(walletService.CreateWallet(walletRequest));
    }

    @PostMapping("/fund")
    public ResponseEntity<WalletResponse> fundWallet(@RequestBody WalletRequest walletRequest) {
        return ResponseEntity.ok(walletService.FundWallet(walletRequest));
    }

    @PostMapping("/debit")
    public ResponseEntity<WalletResponse> debitWallet(@RequestBody WalletRequest walletRequest) {
        return ResponseEntity.ok(walletService.DebitWallet(walletRequest));
    }

    @GetMapping("/details")
    public ResponseEntity<WalletResponse> getWalletDetails(@RequestBody WalletRequest walletRequest) {
        return ResponseEntity.ok(walletService.GetWalletDetails(walletRequest));
    }
}
