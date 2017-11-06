package com.jptest.payments.fulfillment.testonia.business.component.task;

import com.jptest.money.FulfillPaymentRequest;
import com.jptest.money.PaymentFlagsVO;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;


/**
 * Unit adds the specific flags in the fulfillment Request
 */
public class AddPaymentFlagsInFulfillmentRequestTask extends BaseTask<Void> {

    @Override
    public Void process(final Context context) {
        final PaymentFlagsVO paymentFlagsToBeAdded = (PaymentFlagsVO) this.getDataFromContext(context,
                ContextKeys.FULFILL_PAYMENT_FLAGS.getName());

        final FulfillPaymentRequest fulfillPayRequest = this
                .getFulfillmentRequest(ContextKeys.FULFILL_PAYMENT_REQUEST_KEY.getName(), context);

        final PaymentFlagsVO paymentFlagsInRequest = fulfillPayRequest.getFulfillmentPlan().getPaymentStrategy()
                .getTransactionUnits().get(0).getTransactionUnitContext().getPaymentFlags();

        this.setPaymentFlagsInFulFillmentRequest(paymentFlagsInRequest, paymentFlagsToBeAdded);

        return null;
    }

    /**
     * @param fulfillmentRequestContextKey
     * @param context
     * @return fulfillPayRequest
     */
    private FulfillPaymentRequest getFulfillmentRequest(final String fulfillmentRequestContextKey,
            final Context context) {
        final FulfillPaymentRequest fulfillPayRequest = (FulfillPaymentRequest) this.getDataFromContext(context,
                fulfillmentRequestContextKey);
        if (fulfillPayRequest == null) {
            throw new TestExecutionException("Couldn't find user FulfillPaymentRequest in context");
        }
        return fulfillPayRequest;
    }

    /**
     * @param paymentFlagsInRequest
     * @param paymentFlagsToBeAdded
     */
    private void setPaymentFlagsInFulFillmentRequest(final PaymentFlagsVO paymentFlagsInRequest,
            final PaymentFlagsVO paymentFlagsToBeAdded) {
        if (paymentFlagsToBeAdded.getFlags() != null) {
            if (paymentFlagsInRequest.getFlags() != null) {
                paymentFlagsInRequest.setFlags(paymentFlagsInRequest.getFlags().or(paymentFlagsToBeAdded.getFlags()));
            }
            else {
                paymentFlagsInRequest.setFlags(paymentFlagsToBeAdded.getFlags());
            }
        }

        if (paymentFlagsToBeAdded.getFlags2() != null) {
            if (paymentFlagsInRequest.getFlags2() != null) {
                paymentFlagsInRequest
                        .setFlags2(paymentFlagsInRequest.getFlags2().or(paymentFlagsToBeAdded.getFlags2()));
            }
            else {
                paymentFlagsInRequest.setFlags2(paymentFlagsToBeAdded.getFlags2());
            }
        }

        if (paymentFlagsToBeAdded.getFlags3() != null) {
            if (paymentFlagsInRequest.getFlags3() != null) {
                paymentFlagsInRequest
                        .setFlags3(paymentFlagsInRequest.getFlags3().or(paymentFlagsToBeAdded.getFlags3()));
            }
            else {
                paymentFlagsInRequest.setFlags3(paymentFlagsToBeAdded.getFlags3());
            }
        }

        if (paymentFlagsToBeAdded.getFlags4() != null) {
            if (paymentFlagsInRequest.getFlags4() != null) {
                paymentFlagsInRequest
                        .setFlags4(paymentFlagsInRequest.getFlags4().or(paymentFlagsToBeAdded.getFlags4()));
            }
            else {
                paymentFlagsInRequest.setFlags4(paymentFlagsToBeAdded.getFlags4());
            }
        }

        if (paymentFlagsToBeAdded.getFlags5() != null) {
            if (paymentFlagsInRequest.getFlags5() != null) {
                paymentFlagsInRequest
                        .setFlags5(paymentFlagsInRequest.getFlags5().or(paymentFlagsToBeAdded.getFlags5()));
            }
            else {
                paymentFlagsInRequest.setFlags5(paymentFlagsToBeAdded.getFlags5());
            }
        }

        if (paymentFlagsToBeAdded.getFlags6() != null) {
            if (paymentFlagsInRequest.getFlags6() != null) {
                paymentFlagsInRequest
                        .setFlags6(paymentFlagsInRequest.getFlags6().or(paymentFlagsToBeAdded.getFlags6()));
            }
            else {
                paymentFlagsInRequest.setFlags6(paymentFlagsToBeAdded.getFlags6());
            }
        }
    }
}
