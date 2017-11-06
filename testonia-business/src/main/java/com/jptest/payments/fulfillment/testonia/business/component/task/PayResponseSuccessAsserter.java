package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.testng.Assert;
import com.jptest.money.PayResponse;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * Performs very basic validation to check if the {@link PayResponse} is successful or not.
 */
public class PayResponseSuccessAsserter extends BaseAsserter {

    @Override
    public void validate(Context context) {
        PayResponse response = (PayResponse) this.getDataFromContext(context, ContextKeys.PAY_RESPONSE_KEY.getName());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getPaymentResponse().getPpfeResponseStatus().getReturnCode() == 0L);
        context.addReportingAttribute(ReportingAttributes.PAYMENT_ENCRYPTED_ID, response
                .getPaymentResponse().getEncryptedTransactionId());
        context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                response.getPaymentResponse().getEncryptedTransactionId());
    }
}
