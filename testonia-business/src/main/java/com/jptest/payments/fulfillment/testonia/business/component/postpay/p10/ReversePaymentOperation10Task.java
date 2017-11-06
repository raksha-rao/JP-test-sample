package com.jptest.payments.fulfillment.testonia.business.component.postpay.p10;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.ReversePaymentRequest;
import com.jptest.money.ReversePaymentResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionP10DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * ReversePaymentOperationTask takes care of constructing the Reverse Payment Request and executing reverse payment and
 * validates the response
 *
 * @JP Inc.
 */
public class ReversePaymentOperation10Task
        extends BasePostPayment10OperationTask<ReversePaymentRequest, ReversePaymentResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReversePaymentOperation10Task.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP10DaoImpl wTxnDAO;
    @Inject
    private PostPaymentExecService postPaymentService;

    @Override
    public ReversePaymentRequest constructPostPayRequest(final ReversePaymentRequest reversePaymentRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String accountNumber = seller.getAccountNumber();
        List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndType(
                new BigInteger(accountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()));

        // Check if buyer side txn is available e.g. to handle cases like unilateral txns
        if (txnId.size() == 0) {
            final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
            txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndType(
                    new BigInteger(buyer.getAccountNumber()),
                    String.valueOf(WTransactionConstants.Type.USERUSER.getValue()));
        }

        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        operationIdempotencyVO.setIdempotencyString(activityId.toString());
        reversePaymentRequest.setOperationIdempotencyData(operationIdempotencyVO);
        reversePaymentRequest
                .setPaymentTransactionId(new BigInteger(txnId.get(0).toString()));
        reversePaymentRequest.getBusinessContext()
                .setActorId(new BigInteger(accountNumber));
        return reversePaymentRequest;
    }

    @Override
    public ReversePaymentResponse executePostPay(final ReversePaymentRequest reversePaymentRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(reversePaymentRequest);

        return this.postPaymentService.reversePaymentService(reversePaymentRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final ReversePaymentResponse reversePaymentResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        final ReversePaymentResponse response = reversePaymentResponse;
        Assert.assertNotNull(response,
                "post payment response should not be null");
        Assert.assertEquals(response.getResult().toString(), postPayRequest.getReturnCode(),
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");
        context.addReportingAttribute(ReportingAttributes.ENCRYPTED_REVERSAL_TXN_ID,
                reversePaymentResponse.getEncryptedReversalTransactionId());
        LOGGER.info("Reverse Payment - Encrypted Txn Id - {}",
                reversePaymentResponse.getEncryptedReversalTransactionId());
        LOGGER.info("Reverse Payment Operation Passed");
    }
}
