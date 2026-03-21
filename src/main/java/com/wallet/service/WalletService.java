package com.wallet.service;

import com.wallet.dto.request.WalletRequest;
import com.wallet.dto.response.WalletResponse;

public interface WalletService {

   WalletResponse CreateWallet(WalletRequest walletRequest);
   WalletResponse FundWallet(WalletRequest walletRequest);
   WalletResponse DebitWallet(WalletRequest walletRequest);
   WalletResponse GetWalletDetails(WalletRequest walletRequest);
}
