package com.jptest.payments.fulfillment.testonia.sample.tests;

import static com.jptest.payments.fulfillment.testonia.sample.component.ExecutionUnitFactory.getFulfillPaymentOperationUnit;
import static com.jptest.payments.fulfillment.testonia.sample.component.ExecutionUnitFactory.getFulfillPaymentRequestUnit;
import static com.jptest.payments.fulfillment.testonia.sample.component.ExecutionUnitFactory.getUserCreationUnit;

import org.testng.annotations.Guice;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.jptest.payments.fulfillment.testonia.business.guice.DefaultTestoniaGuiceModule;
import com.jptest.payments.fulfillment.testonia.core.dataprovider.TestData;
import com.jptest.payments.fulfillment.testonia.core.dataprovider.TestoniaDataProvider;
import com.jptest.payments.fulfillment.testonia.core.impl.ConcurrentExecutionUnit;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.impl.TestCaseGlobalContext;
import com.jptest.payments.fulfillment.testonia.core.impl.TestExecutor;
import com.jptest.payments.fulfillment.testonia.core.impl.TestExecutor.TestExecutorBuilder;
import com.jptest.payments.fulfillment.testonia.core.reporting.TestResultSummaryReporter;
import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;
import com.jptest.payments.fulfillment.testonia.model.group.FlowType;
import com.jptest.payments.fulfillment.testonia.model.group.FundingType;
import com.jptest.payments.fulfillment.testonia.model.group.Operation;
import com.jptest.payments.fulfillment.testonia.model.group.Priority;

/**
 * This is a sample functional test that shows the usage of Testonia framework.
 * <p>
 * It relies on {@link TestoniaDataProvider} for preparing input data object from json
 */
@Guice(modules = DefaultTestoniaGuiceModule.class)
@Listeners({ TestResultSummaryReporter.class })
public class TestoniaSampleTest {

    @Test(dataProviderClass = TestoniaDataProvider.class, dataProvider = "input", groups = { FlowType.SALE,
            FundingType.IACH, Operation.FULFILL_PAYMENT, Priority.P1 })
    @TestData(filename = "testonia_sample_ft_input.json", type = TestCaseInputData.class, indices = 0)
    public void testFulfillPaymentForRealTimeFlow(TestCaseInputData inputData) throws Exception {
        // Create a context for the test case
        TestCaseGlobalContext context = new TestCaseGlobalContext(inputData.getTestCaseId());
        // Cook up the test case by adding appropriate ExecutionUnits to the test executor
        TestExecutor executor = new TestExecutorBuilder().addContext(context)
                .addUnit(new ConcurrentExecutionUnit("setupStage",
                        getUserCreationUnit(inputData.getBuyerData(), ContextKeys.BUYER_VO_KEY.getName()),
                        getUserCreationUnit(inputData.getSellerData(),
                                ContextKeys.SELLER_VO_KEY.getName())))
                .addUnit(getFulfillPaymentRequestUnit(inputData.getFulfillPaymentPlanOptions(),
                        ContextKeys.FULFILL_PAYMENT_REQUEST_KEY.getName()))
                .addUnit(getFulfillPaymentOperationUnit(ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName()))
                .build();
        // Execute the execution units
        executor.execute();
    }

}
