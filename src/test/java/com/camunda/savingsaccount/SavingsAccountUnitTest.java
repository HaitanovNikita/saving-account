package com.camunda.savingsaccount;

import com.camunda.savingsaccount.entity.Product;
import com.camunda.savingsaccount.entity.ProductType;
import com.camunda.savingsaccount.process.activity.servicetask.DetailsProduct;
import com.camunda.savingsaccount.process.activity.servicetask.DeterminingAmount;
import com.camunda.savingsaccount.process.activity.servicetask.WritingAmountSavingsAccount;
import com.camunda.savingsaccount.process.model.InputData;
import com.camunda.savingsaccount.process.model.SavingAccountData;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.runtime.VariableInstanceQuery;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SavingsAccountUnitTest {

    @ClassRule
    @Rule
    public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

    @Mock
    private RestTemplate restTemplateProduct;
    @Mock
    private RestTemplate restTemplateWriting;
    @Mock
    private ResponseEntity responseEntityProduct;
    @Mock
    private ResponseEntity responseEntityWriting;

    private ProcessEngine processEngine;

    private String writeOffAmountByCardNumberUrl = "http://localhost:8080/saving-accounts-controller-api/write-off-amount";
    private String detailsProductUrl = "http://localhost:8080/product-controller-api/get-details-product";

    @Before
    public void setupMocks() {
        processEngine = processEngine();

        DetailsProduct detailsProduct = DetailsProduct
                .builder()
                .restTemplate(restTemplateProduct)
                .getDetailsProductUrl(detailsProductUrl)
                .build();

        Mocks.register("detailsProduct", detailsProduct);

        DeterminingAmount determiningAmount = DeterminingAmount.builder().build();
        Mocks.register("determiningAmount", determiningAmount);

        WritingAmountSavingsAccount writingAmountSavingsAccount = WritingAmountSavingsAccount
                .builder()
                .restTemplate(restTemplateWriting)
                .writeOffAmountByCardNumberUrl(writeOffAmountByCardNumberUrl)
                .build();

        Mocks.register("writingAmountSavingsAccount", writingAmountSavingsAccount);

        when(restTemplateProduct.exchange(anyString(),
                any(),
                any(),
                (Class<Object>) any()))
                .thenReturn(responseEntityProduct);

        when(responseEntityProduct.getBody()).thenReturn(Product.builder()
                .productId(UUID.randomUUID())
                .name("product-1")
                .type(ProductType.PT_1010)
                .build());

        when(restTemplateWriting.exchange(anyString(),
                any(),
                any(),
                (Class<Object>) any()))
                .thenReturn(responseEntityWriting);

        when(responseEntityWriting.getBody()).thenReturn(new BigDecimal(12345));
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

        ProcessInstance processInstance = runtimeService()
                .startProcessInstanceByMessage("start_process", variables);

        ProcessEngineTests.assertThat(processInstance)
                .hasPassedInOrder(
                        "start_process",
                        "details_product"
                ).task();

        Product product = (Product) processEngine.getRuntimeService()
                .getVariable(processInstance.getProcessInstanceId(), "detailsProduct");

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("product-1");
        assertThat(product.getType()).isEqualTo(ProductType.PT_1010);

        ProcessEngineTests.assertThat(processInstance)
                .hasPassed(
                        "rounding_to_hryvnia"
                ).task();

        BigDecimal amountsCreditedSavingsAccount = (BigDecimal) processEngine.getRuntimeService().getActivityInstance("rounding_to_hryvnia");

        SavingAccountData savingAccountData = SavingAccountData
                .builder()
                .cardNumber(inputData.getCardNumber())
                .amountsCreditedSavingsAccount(amountsCreditedSavingsAccount)
                .build();



        ProcessEngineTests.assertThat(processInstance)
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
