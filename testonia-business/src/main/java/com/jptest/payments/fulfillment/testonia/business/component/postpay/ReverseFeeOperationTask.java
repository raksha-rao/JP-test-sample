package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.ReverseFeeRequest;
import com.jptest.money.ReverseFeeResponse;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.component.validation.EngineActivityIdRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.test.money.constants.WTransactionConstants;

/**
 * ReverseFeeOperationTask takes care about constructing ReverseFeeRequest, call it against paymentServ.reverse_fee and
 * validates the ReverseFeeResponse.
 */
public class ReverseFeeOperationTask extends BasePostPaymentOperationTask<ReverseFeeRequest, ReverseFeeResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReverseFeeOperationTask.class);

    @Inject
    private PostPaymentExecService postPaymentService;

    @Inject
    private CryptoUtil cryptoUtil;

    @Override
    public ReverseFeeRequest constructPostPayRequest(final ReverseFeeRequest reverseFeeRequest,
            final Context context) {

        @SuppressWarnings("unchecked")
        final List<WTransactionVO> wTransactionList = (List<WTransactionVO>) this.getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());

        for (final WTransactionVO tr : wTransactionList) {
            if (tr.getType() == WTransactionConstants.Type.FEE.getByte()) {
                reverseFeeRequest.setFeeHandle(String.valueOf(tr.getId()));
                break;
            }
        }

        return reverseFeeRequest;
    }

    @Override
    public ReverseFeeResponse executePostPay(final ReverseFeeRequest reverseFeeRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(reverseFeeRequest);

        return this.postPaymentService.reverseFeeService(reverseFeeRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final ReverseFeeResponse reverseFeeResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        final ReverseFeeResponse response = reverseFeeResponse;
        Assert.assertNotNull(response,
                "post payment response should not be null");
        Assert.assertEquals(response.getResponseCode().toString(), postPayRequest.getReturnCode(),
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (reverseFeeResponse.getEncryptedFeeReversalTransId() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), reverseFeeResponse.getEncryptedFeeReversalTransId());

        LOGGER.info("Reverse Fee - Encrypted Txn Id - {}", reverseFeeResponse.getEncryptedFeeReversalTransId());
        LOGGER.info("Reverse Fee Operation Passed");
    }

    @Override
    protected void populateActivityId(Context context, ReverseFeeResponse response) {

        final String encTransactionId = response.getEncryptedFeeReversalTransId();
        long transactionId;
        try {
            transactionId = this.cryptoUtil.decryptTxnId(encTransactionId);
        } catch (final Exception e) {
            throw new TestExecutionException("Decryption of Transaction ID failed", e);
        }
        Assert.assertNotNull(transactionId, this.getClass().getSimpleName() 
        		+ ". populateActivityId() - Found null transactionId");
        final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(
                BigInteger.valueOf(transactionId));
        final BigInteger activityId = activityIdRetriever.process(context);
        Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
        		+ ". populateActivityId() - Found null activityId");

        context.setData(this.getActivityIdKey(), activityId);
        LOGGER.info("REVERSAL_ACTIVITY_ID: Activity ID - {}, Transaction ID - {}", activityId,
                transactionId);
    }
}
