package com.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private String transactionId;
    private String userId;
    private BigDecimal amount;
    private String type;          // CREDIT or DEBIT
    private String description;
    private LocalDateTime timestamp;
}
