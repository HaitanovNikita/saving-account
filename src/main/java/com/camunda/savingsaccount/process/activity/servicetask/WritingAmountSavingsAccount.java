package com.camunda.savingsaccount.process.activity.servicetask;

import com.camunda.savingsaccount.config.annotation.Profiling;
import com.camunda.savingsaccount.process.model.InputData;
import com.camunda.savingsaccount.process.model.SavingAccountData;
import com.camunda.savingsaccount.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
@Slf4j
@Profiling
@Component
public class WritingAmountSavingsAccount implements JavaDelegate {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${application.savings-account-controller-api.write-off-amount-by-card-number.url}")
    private String writeOffAmountByCardNumberUrl;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        InputData inputData = (InputData) delegateExecution.getVariable("inputData");
        BigDecimal amountsCreditedSavingsAccount = (BigDecimal) delegateExecution.getVariable("amountsCreditedSavingsAccount");
        if (inputData != null && amountsCreditedSavingsAccount != null) {
            SavingAccountData savingAccountData = SavingAccountData
                    .builder()
                    .cardNumber(inputData.getCardNumber())
                    .amountsCreditedSavingsAccount(amountsCreditedSavingsAccount)
                    .build();

            ResponseEntity<BigDecimal> exchange = restTemplate.exchange(
                    writeOffAmountByCardNumberUrl,
                    HttpMethod.PUT,
                    new HttpEntity(savingAccountData, Utils.getHttpHeaders()),
                    BigDecimal.class);

            delegateExecution.setVariable("AmountCreditedBonusAccount", exchange.getBody());

            delegateExecution.setVariable("statusAmountCreditedBonusAccountErrorCode", Utils.SUCCESS_ERROR_CODE);
            delegateExecution.setVariable("statusAmountCreditedBonusAccountErrorMessage", Utils.SUCCESS_ERROR_DESCRIPTION);
            log.info("AmountCreditedBonusAccount", exchange.getBody());
        } else {
            delegateExecution.setVariable("statusAmountCreditedBonusAccountErrorCode", Utils.UN_SUCCESS_ERROR_CODE);
            delegateExecution.setVariable("statusAmountCreditedBonusAccountErrorMessage", Utils.UN_SUCCESS_ERROR_DESCRIPTION);
            log.error("Execution broken, errors found from amount credited bonus account" );
        }
    }
}
