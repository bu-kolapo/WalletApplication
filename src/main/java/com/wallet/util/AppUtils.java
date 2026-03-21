package com.wallet.util;

import com.wallet.dto.response.WalletResponse;

public class AppUtils {

    public static WalletResponse walletResponse(int responseCode, boolean success, String message){
        return WalletResponse.builder()
                .responseCode(responseCode)
                .success(success)
                .message(message)
                .build();
    }
}
