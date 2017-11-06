package com.jptest.payments.fulfillment.testonia.business.component.task;

import com.jptest.money.PlanAgreementResponse;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import org.springframework.util.Assert;

/**
@JP Inc.
 */
public class PlanAgreementResponseAsserter extends BaseAsserter {
    @Override
    public void validate(Context context) {
        PlanAgreementResponse response = (PlanAgreementResponse) context
                .getData(ContextKeys.PLAN_AGREEMENT_RESPONSE_KEY.getName());

        Assert.notNull(response, this.getClass().getSimpleName()
                + ".validate() failed - Plan Agreement Response is null");
        Assert.notNull(response.getDefaultAgreementPlan(), this.getClass().getSimpleName()
                + ".validate() failed - Default plan is null");
        Assert.notNull(response.getAvailableAgreementPlans(), this.getClass().getSimpleName()
                + ".validate() failed - Available plan is null");
        Assert.isTrue(response.getAvailableAgreementPlans().size() > 0, this.getClass().getSimpleName()
                + ".validate() failed - Available plan length is zero");
    }
}
