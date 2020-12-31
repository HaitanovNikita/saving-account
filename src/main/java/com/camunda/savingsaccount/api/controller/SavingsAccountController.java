package com.camunda.savingsaccount.api.controller;

import com.camunda.savingsaccount.process.model.SavingAccountData;
import com.camunda.savingsaccount.api.service.SavingsAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
@Slf4j
@RestController
@Component
@RequestMapping("saving-accounts-controller-api")
public class SavingsAccountController {

    @Autowired
    SavingsAccountService savingsAccountService;

    @RequestMapping(value = "/write-off-amount", method = RequestMethod.PUT)
    public BigDecimal writeOffAmountByCardNumber(@RequestBody SavingAccountData savingAccountData) {
          if (savingAccountData!=null){
              return savingsAccountService.writingAmountByCardNumber(savingAccountData.getCardNumber(),savingAccountData.getAmountsCreditedSavingsAccount());
          }
        return BigDecimal.ZERO;
    }

}
