package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.util.function.Function;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.PlanPaymentV2Request;
import com.jptest.money.PlanPaymentV2Response;
import com.jptest.payments.fulfillment.testonia.bridge.MoneyplanningServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableBaseTask;
import com.jptest.payments.fulfillment.testonia.business.component.task.exception.PlanCreationFailedException;
import com.jptest.payments.fulfillment.testonia.core.Context;

/**
 * MpsPlanPaymentV2Task calls plan_payment_v2 operation on moneyplanningserv with request built using provided Function
 * reference provided by the test
 * 
 * @JP Inc.
 *         PlanPaymentV2Request. Request is built using data available in context.
 */
public class MpsPlanPaymentV2Task extends RetriableBaseTask<PlanPaymentV2Response> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MpsPlanPaymentV2Task.class);

    @Inject
    private MoneyplanningServBridge moneyPlanningBridge;

    private Function<Context, PlanPaymentV2Request> requestBuilder;

    public MpsPlanPaymentV2Task(Function<Context, PlanPaymentV2Request> requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    private PlanPaymentV2Response getPlanResponse(Context context) {
        try {
            PlanPaymentV2Request rq = requestBuilder.apply(context);
            return moneyPlanningBridge.planPaymentV2(rq);
        } catch (Exception e) {
            LOGGER.warn("planning call failed, it will be retried if possible Exception: ", e);
            return null;
        }
    }

    @Override
    protected boolean isDesiredOutput(PlanPaymentV2Response output) {
        return output != null;
    }

    @Override
    protected PlanPaymentV2Response retriableExecute(Context context) {
        return getPlanResponse(context);
    }

    @Override
    protected PlanPaymentV2Response onSuccess(Context context, PlanPaymentV2Response output) {
        return output;
    }

    @Override
    protected PlanPaymentV2Response onFailure(Context context, PlanPaymentV2Response output) {
        throw new PlanCreationFailedException("Cannot get response from planning");
    }
}
