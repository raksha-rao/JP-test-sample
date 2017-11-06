package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.FulfillmentRequestStatus;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;


public class BasicFulfillPaymentResponseAsserter extends BaseAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicFulfillPaymentResponseAsserter.class);

    @Override
    public void validate(final Context context) {

        final FulfillPaymentResponse fulfillPaymentResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PAYMENT_RESPONSE_KEY.getName());

        Assert.assertNotNull(fulfillPaymentResponse, "fulfillPaymentResponse should not be NULL");
        Assert.assertEquals(fulfillPaymentResponse.getFulfillmentRequestStatusAsEnum(),
                FulfillmentRequestStatus.PROCESSED, this.getClass().getSimpleName() + ".validate() failed for status:");
        Assert.assertNotNull(fulfillPaymentResponse.getTransactionUnitStatus());
        Assert.assertEquals(1, fulfillPaymentResponse.getTransactionUnitStatus().size(), 
        		this.getClass().getSimpleName() + ".validate() failed for TransactionUnitStatus.size");
        Assert.assertNotNull(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle());
        Assert.assertNotNull(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails());
        Assert.assertNotNull(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails()
                .getCreditSideHandle());
        Assert.assertNotNull(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails()
                .getDebitSideHandle());

        context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                fulfillPaymentResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle());

        // since transaction handle for unilateral transactions is not supported by transaction explorer, adding debit
        // side handle to kibana
        context.addReportingAttribute(ReportingAttributes.PAYMENT_ENCRYPTED_ID,
                fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails().getDebitSideHandle());

        context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(),
                fulfillPaymentResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle());

        LOGGER.info("\n FulfillPayment Encrypted Transaction Handle:{}",
                fulfillPaymentResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle());
    }
}
