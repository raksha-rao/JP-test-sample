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
import com.jptest.transfer.transfercompletion.model.CompleteTransferMessage;
import com.jptest.transfer.transfercompletion.model.CompleteTransferResponse;


/**
 * @JP Inc.
 */
public class CompleTransferMessageOperationTask extends
BasePostPaymentOperationTask<CompleteTransferMessage, CompleteTransferResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompleTransferMessageOperationTask.class);

    @Inject
    TxnCompletionServBridge txnCompletionServBridge;

    @Override
    public CompleteTransferMessage constructPostPayRequest(
            final CompleteTransferMessage paymentCompletionRequest,
            final Context context) {

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
    public CompleteTransferResponse executePostPay(final CompleteTransferMessage paymentCompletionRequest,
            final boolean call2pex) {
        Assert.assertNotNull(paymentCompletionRequest,
                this.getClass().getSimpleName()
                + ".executePostPay() - CompleteTransferMessage Request should not ne null");
        return this.txnCompletionServBridge.completeTransfer(paymentCompletionRequest);
    }

    @Override
    public void assertPostPayResponse(final CompleteTransferResponse paymentCompletionResponse,
            final PostPaymentRequest paymentCompletionRequest, final Context context) {

        final CompleteTransferResponse response = paymentCompletionResponse;
        Assert.assertNotNull(response,
                this.getClass().getSimpleName()
                + ".assertPostPayResponse() - CompleteTransferResponse should not be null");
        Assert.assertEquals(response.getReturnCode().toString(), paymentCompletionRequest.getReturnCode(),
                this.getClass().getSimpleName()
                + ".assertPostPayResponse() - CompleteTransferResponse is not as Expected");
        LOGGER.info("Complete Transfer Operation Passed");
    }

}
