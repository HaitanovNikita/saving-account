package com.camunda.savingsaccount.api.service;

import com.camunda.savingsaccount.config.annotation.Profiling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Slf4j
@Profiling
@Service
public class SavingsAccountService {
    public BigDecimal writingAmountByCardNumber(@NotNull String cardNumber, @NotNull BigDecimal amountsCreditedSavingsAccount ){
        /*TODO operations with BD for writing amount by card number */
        if(cardNumber.isEmpty()&&cardNumber.length()==16){
            //...return amountsCreditedSavingsAccount to notify the recorded amount
            log.info("writingAmountByCardNumber()",amountsCreditedSavingsAccount);
            return amountsCreditedSavingsAccount;
        }else{
            log.info("writingAmountByCardNumber() / null)");
            return BigDecimal.ZERO;
        }
    }

}
