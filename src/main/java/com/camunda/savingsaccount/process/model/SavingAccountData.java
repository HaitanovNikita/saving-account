package com.camunda.savingsaccount.process.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
