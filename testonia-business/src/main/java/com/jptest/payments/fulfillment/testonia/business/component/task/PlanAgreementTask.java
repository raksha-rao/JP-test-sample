package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.util.function.Function;

import javax.inject.Inject;

import com.jptest.money.PlanAgreementRequest;
import com.jptest.money.PlanAgreementResponse;
import com.jptest.payments.fulfillment.testonia.bridge.MoneyplanningServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;

/**
@JP Inc.
 *
 * Task to create plan agreement (Order plan).
 * Ex : bulk order planning
 * Calls mps' plan_agreement API
 */
public class PlanAgreementTask extends BaseTask<PlanAgreementResponse> {
    @Inject
    private MoneyplanningServBridge moneyplanningServBridge;

    private Function<Context, PlanAgreementRequest> requestBuilder;

    public PlanAgreementTask(Function<Context, PlanAgreementRequest> requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @Override
    public PlanAgreementResponse process(Context context) {
        PlanAgreementRequest rq = requestBuilder.apply(context);
        return moneyplanningServBridge.planAgreement(rq);
    }
}
