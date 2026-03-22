package com.wallet.service;

import com.wallet.dto.request.WalletRequest;
import com.wallet.dto.response.WalletResponse;
import com.wallet.model.Transaction;

import java.util.List;

public interface WalletService {

   WalletResponse createWallet(WalletRequest walletRequest);
   WalletResponse fundWallet(WalletRequest walletRequest);
   WalletResponse debitWallet(WalletRequest walletRequest);
   WalletResponse getWalletDetails(WalletRequest walletRequest);
   List<Transaction> getTransactionHistory(String userId);
}
