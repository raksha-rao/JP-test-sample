package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.PaymentCompletionResult;
import com.jptest.money.ReleasePendingPaymentsHoldsRequest;
import com.jptest.money.ReleasePendingPaymentsHoldsResponse;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.FulfillmentHoldData;
import com.jptest.test.money.util.servicehelper.payment.PaymentServHelper;
import com.jptest.test.money.util.voutil.money.ReleasePendingPaymentsHoldsConstructBuilder;


/**
 * Releases the hold on the transaction because of flags like OFAC or FMF or any other restrictions. This task takes the
 * {@link FulfillmentHoldData} as input to understand which holdType it needs to release.
 */
public class TransactionHoldReleaseTask extends BaseTask<Void> {

    private static final int SUCCESS_CODE = 0;

    @Inject
    private PaymentServHelper paymentServHelper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHoldReleaseTask.class);

    private FulfillmentHoldData input;

    public TransactionHoldReleaseTask(final FulfillmentHoldData input) {
        this.input = input;
    }

    @Override
    public Void process(final Context context) {
        // releasing the hold
        try {
            final String pendingID = (String) this.getDataFromContext(context,
                    ContextKeys.WTRANSACTION_PENDING_ID_KEY.getName());
            final ReleasePendingPaymentsHoldsConstructBuilder blder = new ReleasePendingPaymentsHoldsConstructBuilder.Builder()
                    .payment_transaction_id(new BigInteger(pendingID))
                    .hold_to_release(this.input.getHoldType()).build();

            // get ReleasePendingPaymentsHoldsRequest
            final ReleasePendingPaymentsHoldsRequest relPendPmtHldRequest = blder
                    .getReleasePendingPaymentsHoldsRequest();

            // get ReleasePendingPaymentsHoldsResponse by calling
            // release_pending_payments_holds api
            final ReleasePendingPaymentsHoldsResponse relPendPmtHldResponse = this.paymentServHelper
                    .releasePendingPaymentsHolds(relPendPmtHldRequest);

            // get PaymentCompletionResult list from response
            final java.util.List<PaymentCompletionResult> list = relPendPmtHldResponse
                    .getPaymentCompletionResult();

            // get PaymentCompletionResult from the list
            final PaymentCompletionResult result = list.get(0);
            final Integer returnCode = result.getResult();
			LOGGER.info("rc returned by release_pending_payments_holds:{} " + returnCode);
            if (returnCode != SUCCESS_CODE) {
                throw new TestExecutionException("Failed while releasing the hold, return code is: " + returnCode);
            }

        }
        catch (final Exception e) {
            if (e instanceof TestExecutionException) {
                throw e;
            }
            else {
                final String message = "Error while releasing the hold";
                LOGGER.error(message, e);
                throw new TestExecutionException(message, e);
            }
        }
        return null;
    }
}
