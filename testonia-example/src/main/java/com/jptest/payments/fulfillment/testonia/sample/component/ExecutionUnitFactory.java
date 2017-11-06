package com.jptest.payments.fulfillment.testonia.sample.component;

import com.jptest.payments.fulfillment.testonia.business.component.task.BasicFulfillPaymentRequestAsserter;
import com.jptest.payments.fulfillment.testonia.business.component.task.BasicFulfillPlanResponseAsserter;
import com.jptest.payments.fulfillment.testonia.business.component.task.FulfillPaymentExecutionTask;
import com.jptest.payments.fulfillment.testonia.business.component.task.FulfillPaymentRequestBuilderTask;
import com.jptest.payments.fulfillment.testonia.business.component.user.UserCreationTask;
import com.jptest.payments.fulfillment.testonia.core.Unit;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionUnit.ExecutionUnitBuilder;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.fulfillment.testonia.model.UserCreationTaskInput;

public class ExecutionUnitFactory {

    public static Unit getUserCreationUnit(UserCreationTaskInput inputData, String key) {
        return new ExecutionUnitBuilder().addId(key).addTask(new UserCreationTask(inputData))
                .build();
    }

    public static Unit getFulfillPaymentOperationUnit(String key) {
        Unit unit = new ExecutionUnitBuilder().addId(key).addTask(new FulfillPaymentExecutionTask())
                .addAsserters(new BasicFulfillPlanResponseAsserter()).build();
        return unit;
    }

    public static Unit getFulfillPaymentRequestUnit(FulfillPaymentPlanOptions paymentOptions, String key) {
        Unit unit = new ExecutionUnitBuilder().addId(key).addTask(new FulfillPaymentRequestBuilderTask(paymentOptions))
                .addAsserters(new BasicFulfillPaymentRequestAsserter())
                .build();
        return unit;
    }
}
