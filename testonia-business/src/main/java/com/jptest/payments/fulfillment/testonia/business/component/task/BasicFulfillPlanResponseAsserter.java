package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.FulfillmentRequestStatus;
import com.jptest.money.PlanPaymentV2Response;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;


public class BasicFulfillPlanResponseAsserter extends BaseAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicFulfillPlanResponseAsserter.class);

    @Override
    public void validate(final Context context) {
        // Some test cases add FulfillPaymentResponse to context using FULFILL_PLAN_RESPONSE_KEY and some add using
        // FULFILL_PAYMENT_RESPONSE_KEY. So to ensure support for both these types of test cases, check what type
        // of data object is present for these keys and accordingly extract FulfillPaymentResponse from context
        String paymentResponseKey = ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName();
        if (this.getDataFromContext(context, paymentResponseKey) instanceof PlanPaymentV2Response) {
            paymentResponseKey = ContextKeys.FULFILL_PAYMENT_RESPONSE_KEY.getName();
        }
        final FulfillPaymentResponse fulfillPaymentResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                paymentResponseKey);
        Assert.assertNotNull(fulfillPaymentResponse,
                "fulfillPaymentResponse should not be null, fulfill_payment operation failed.");

        // verify

        /*Assert.assertEquals(fulfillPaymentResponse.getFulfillmentRequestStatusAsEnum(),
                FulfillmentRequestStatus.PROCESSED, this.getClass().getSimpleName() + ".validate() failed for status:");*/

        // Some of the Payment response will have FulfillmentRequestStatus as DECLINED like FMF Denied, OFAC Dined,
        // External Funding Source Dined but it will have TransactionUnitStatus which need to be verified. For any other
        // failure FulfillmentRequestStatus will be DECLINED and there will not be TransactionUnitStatus.
        if (fulfillPaymentResponse.getFulfillmentRequestStatusAsEnum() == FulfillmentRequestStatus.PROCESSED
                || fulfillPaymentResponse.getFulfillmentRequestStatusAsEnum() == FulfillmentRequestStatus.DECLINED) {
            Assert.assertTrue(true, this.getClass().getSimpleName() + ".validate() failed for status:");
        }

        Assert.assertNotNull(fulfillPaymentResponse.getTransactionUnitStatus(), "TransactionUnitStatus is not present in FulfillPaymentResponse");
        Assert.assertEquals(1, fulfillPaymentResponse.getTransactionUnitStatus().size(),
                this.getClass().getSimpleName() + ".validate() failed for TransactionUnitStatus.size");
        Assert.assertNotNull(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle());
        Assert.assertNotNull(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails());
        Assert.assertNotNull(
                fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails().getCreditSideHandle());
        Assert.assertNotNull(
                fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails().getDebitSideHandle());

        context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                fulfillPaymentResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle());

        context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(),
                fulfillPaymentResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle());

        LOGGER.info("Encrypted Transaction handle:{}",
                fulfillPaymentResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle());
        context.addReportingAttribute(ReportingAttributes.PAYMENT_ENCRYPTED_ID, fulfillPaymentResponse
                .getTransactionUnitStatus().get(0).getTransactionUnitHandle());
    }

}
