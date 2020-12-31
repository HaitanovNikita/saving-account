package com.camunda.savingsaccount.process.model;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Builder
public class SavingAccountData {
    @NotNull
    private final String cardNumber;
    @NotNull
    private final BigDecimal amountsCreditedSavingsAccount;
}
