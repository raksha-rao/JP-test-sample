/**
 *
 */
package com.jptest.payments.fulfillment.testonia.business.component.paymentcompletion;

import java.math.BigInteger;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.payments.fulfillment.testonia.bridge.TxnCompletionServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.BasePostPaymentOperationTask;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.transfer.transfercompletion.model.completeTransferReviewMessage;
import com.jptest.transfer.transfercompletion.model.completeTransferReviewResponse;


/**
 * @JP Inc.
 */
public class CompleteTransferReviewMessageOperationTask extends
BasePostPaymentOperationTask<completeTransferReviewMessage, completeTransferReviewResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompleteTransferReviewMessageOperationTask.class);

    @Inject
    TxnCompletionServBridge txnCompletionServBridge;

    @Override
    public completeTransferReviewMessage constructPostPayRequest(
            final completeTransferReviewMessage paymentCompletionRequest, final Context context) {

        /*
         * This is to test the invalid transaction handle use case. If input is passed from testdata input json, set it
         * as transactionHandle else set the transactionHandle from context
         */

        if (paymentCompletionRequest.getTransactionHandle() != null) {
            paymentCompletionRequest
            .setTransactionHandle(paymentCompletionRequest.getTransactionHandle());
        }
        else {
            paymentCompletionRequest
            .setTransactionHandle((BigInteger) context.getData(ContextKeys.WTRANSACTION_ID_KEY.getName()));
        }

        return paymentCompletionRequest;
    }

    @Override
    public completeTransferReviewResponse executePostPay(final completeTransferReviewMessage request,
            final boolean call2pex) {
        Assert.assertNotNull(request,
                this.getClass().getSimpleName()
                + ".executePostPay() - completeTransferReviewMessage request should not be null");
        return this.txnCompletionServBridge.completeTransferReview(request);

    }

    @Override
    public void assertPostPayResponse(final completeTransferReviewResponse paymentCompletionResponse,
            final PostPaymentRequest paymentCompletionRequest, final Context context) {

        final completeTransferReviewResponse response = paymentCompletionResponse;
        Assert.assertNotNull(response,
                this.getClass().getSimpleName()
                + ".assertPostPayResponse() - completeTransferReviewResponse should not be null");
        Assert.assertEquals(response.getReturnCode().toString(),
                paymentCompletionRequest.getReturnCode(),
                this.getClass().getSimpleName()
                + ".assertPostPayResponse()-  completeTransferReviewResponse is not as expected");
        LOGGER.info("Complete Transfer Review Operation Passed");
    }
}
