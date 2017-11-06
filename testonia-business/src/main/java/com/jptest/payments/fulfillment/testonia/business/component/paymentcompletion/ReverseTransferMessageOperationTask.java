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
import com.jptest.transfer.transfercompletion.model.ReverseTransferMessage;
import com.jptest.transfer.transfercompletion.model.ReverseTransferResponse;


/**
 * @JP Inc.
 */
public class ReverseTransferMessageOperationTask extends
BasePostPaymentOperationTask<ReverseTransferMessage, ReverseTransferResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReverseTransferMessageOperationTask.class);

    @Inject
    TxnCompletionServBridge txnCompletionServBridge;

    @Override
    public ReverseTransferMessage constructPostPayRequest(
            final ReverseTransferMessage paymentCompletionRequest,
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
    public ReverseTransferResponse executePostPay(final ReverseTransferMessage request,
            final boolean call2pex) {
        Assert.assertNotNull(request,
                this.getClass().getSimpleName() + ".executePostPay() - ReverseTransferMessage should not be null");
        return this.txnCompletionServBridge.reverseTransfer(request);
    }

    @Override
    public void assertPostPayResponse(final ReverseTransferResponse paymentCompletionResponse,
            final PostPaymentRequest paymentCompletionRequest, final Context context) {

        final ReverseTransferResponse response = paymentCompletionResponse;
        Assert.assertNotNull(response,
                this.getClass().getSimpleName()
                + ".assertPostPayResponse() - ReverseTransferResponse should not be null");
        Assert.assertEquals(response.getReturnCode().toString(), paymentCompletionRequest.getReturnCode(),
                this.getClass().getSimpleName()
                + ".assertPostPayResponse() - ReverseTransferResponse is not as Expected");
        LOGGER.info("Reverse Transfer  Operation Passed");
    }
}
