package com.camunda.savingsaccount.process.activity.servicetask;

import com.camunda.savingsaccount.config.annotation.Profiling;
import com.camunda.savingsaccount.entity.Product;
import com.camunda.savingsaccount.process.model.InputData;
import com.camunda.savingsaccount.utils.Utils;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
@Slf4j
@Profiling
@Component
@Builder
public class DeterminingAmount implements JavaDelegate, Serializable {

    private final double fixedProcentWithInputSum = 15;
    private final double fixedSumWithInputSum = 100;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        InputData inputData = (InputData) delegateExecution.getVariable("inputData");
        Product product = (Product) delegateExecution.getVariable("detailsProduct");
        BigDecimal amountsCreditedSavingsAccount = BigDecimal.ZERO;

        if (product != null) {
            BigDecimal inputSum = inputData.getSum();
            switch (product.getType()) {
                case PT_1010:
                    //*case - 1: Rounding to hryvnia *//*
                    amountsCreditedSavingsAccount = BigDecimal.valueOf(Math.ceil(inputSum.doubleValue()));
                    break;
                case PT_1020:
                    //*case - 2: fixed procent *//*
                    int percentageAmount = (int) (inputSum.longValue() * (fixedProcentWithInputSum / 100));
                    amountsCreditedSavingsAccount = BigDecimal.valueOf(percentageAmount);
                    break;
                case PT_1030:
                    //*case - 3: fixed sum *//*
                    amountsCreditedSavingsAccount = BigDecimal.valueOf(inputSum.longValue() > fixedSumWithInputSum ? (int) (inputSum.longValue() - fixedSumWithInputSum) : 0);
                    break;
            }
            delegateExecution.setVariable("amountsCreditedSavingsAccount", amountsCreditedSavingsAccount);

            delegateExecution.setVariable("statusAmountsCreditedSavingsAccountErrorCode", Utils.SUCCESS_ERROR_CODE);
            delegateExecution.setVariable("statusAmountsCreditedSavingsAccountErrorMessage", Utils.SUCCESS_ERROR_DESCRIPTION);
            log.info("amountsCreditedSavingsAccount",amountsCreditedSavingsAccount);
        } else {
            delegateExecution.setVariable("amountsCreditedSavingsAccount", BigDecimal.ZERO);
            delegateExecution.setVariable("statusAmountsCreditedSavingsAccountErrorCode", Utils.UN_SUCCESS_ERROR_CODE);
            delegateExecution.setVariable("statusAmountsCreditedSavingsAccountErrorMessage", Utils.UN_SUCCESS_ERROR_DESCRIPTION);
            log.error("Execution broken, errors found from amounts credited savings account" );
        }
    }
}
