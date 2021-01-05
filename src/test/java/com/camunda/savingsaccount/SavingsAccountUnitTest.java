package com.camunda.savingsaccount;

import com.camunda.savingsaccount.entity.Product;
import com.camunda.savingsaccount.entity.ProductType;
import com.camunda.savingsaccount.process.activity.servicetask.DetailsProduct;
import com.camunda.savingsaccount.process.activity.servicetask.DeterminingAmount;
import com.camunda.savingsaccount.process.activity.servicetask.WritingAmountSavingsAccount;
import com.camunda.savingsaccount.process.model.InputData;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.init;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;

@RunWith(PowerMockRunner.class)
public class SavingsAccountUnitTest {

    @ClassRule
    @Rule
    public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

    private static ProcessEngine processEngine;

    @Before
    public void setupMocks() {
        init(rule.getProcessEngine());
        processEngine = processEngine();

        DetailsProduct detailsProduct = new DetailsProduct();
        Mocks.register("detailsProduct", detailsProduct);

        DeterminingAmount determiningAmount = new DeterminingAmount();
        Mocks.register("determiningAmount", determiningAmount);

        WritingAmountSavingsAccount writingAmountSavingsAccount = new WritingAmountSavingsAccount();
        Mocks.register("writingAmountSavingsAccount", writingAmountSavingsAccount);
    }

    @Test
    @Deployment(resources = "savings-account.bpmn")
    public void testParsingAndDeployment() {
    }

    @Test
    @Deployment(resources = "savings-account.bpmn")
    public void testHappyCase1() {

        InputData inputData = InputData
                .builder()
                .cardNumber("4444-5555-6666-7777")
                .sum(BigDecimal.valueOf(12345))
                .build();

        Map<String, Object> variables = new HashMap<>();
        variables.put("inputData", inputData);

        ProcessInstance processInstance = processEngine.getRuntimeService()
//                .startProcessInstanceByKey(PROCESS_DEFINITION_KEY, Variables.putValue("startSavingsMessage", variables));
                .startProcessInstanceByMessage("startSavingsMessage", variables);

        ProcessEngineAssertions
                .assertThat(processInstance)
                .hasPassedInOrder(
                        "start_process",
                        "details_product"
                ).task();

        Product product = (Product) runtimeService().createVariableInstanceQuery()
                .processInstanceIdIn(processInstance.getProcessInstanceId())
                .variableName("detailsProduct")
                .singleResult().getValue();

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("product-1");
        assertThat(product.getType()).isEqualTo(ProductType.PT_1010);

        ProcessEngineAssertions
                .assertThat(processInstance)
                .hasPassed(
                        "writing_amount_savings_account"
                ).task();

        BigDecimal amountCreditedBonusAccount = (BigDecimal) runtimeService().createVariableInstanceQuery()
                .processInstanceIdIn(processInstance.getProcessInstanceId())
                .variableName("AmountCreditedBonusAccount")
                .singleResult().getValue();

        assertThat(amountCreditedBonusAccount).isNotNull();

    }
}
