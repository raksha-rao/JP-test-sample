package com.jptest.payments.fulfillment.testonia.business.component.postpay.p10;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.RefundPaymentRequest;
import com.jptest.money.RefundPaymentResponse;
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
 * RefundPaymentOperationTask takes care of constructing the refund request , executes and validates the response
 *
 * @JP Inc.
 */
public class RefundPaymentOperation10Task
        extends BasePostPayment10OperationTask<RefundPaymentRequest, RefundPaymentResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefundPaymentOperation10Task.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private PostPaymentExecService postPaymentService;
    @Inject
    private WTransactionP10DaoImpl wTxnDAO;
    
    @Override
    public RefundPaymentRequest constructPostPayRequest(final RefundPaymentRequest refundPaymentRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String accountNumber = seller.getAccountNumber();
        final List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndType(
                new BigInteger(accountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()));
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        operationIdempotencyVO.setIdempotencyString(activityId.toString());
        refundPaymentRequest.setOperationIdempotencyData(operationIdempotencyVO);
        refundPaymentRequest
                .setPaymentTransactionId(new BigInteger(txnId.get(0).toString()));
        refundPaymentRequest.getBusinessContext()
                .setActorId(new BigInteger(accountNumber));
        return refundPaymentRequest;
    }

    @Override
    public RefundPaymentResponse executePostPay(final RefundPaymentRequest refundPaymentRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(refundPaymentRequest);
        
        return this.postPaymentService.refundPaymentService(refundPaymentRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final RefundPaymentResponse refundPaymentResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        final RefundPaymentResponse response = refundPaymentResponse;
        Assert.assertNotNull(response,
                "post payment response should not be null");

        Assert.assertEquals(response.getResult().toString(), postPayRequest.getReturnCode(),
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (refundPaymentResponse.getEncryptedRefundTransactionId() != null) {
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    refundPaymentResponse.getEncryptedRefundTransactionId());
        }
        context.addReportingAttribute(ReportingAttributes.ENCRYPTED_REFUND_TXN_ID,
                refundPaymentResponse.getEncryptedRefundTransactionId());
        LOGGER.info("Refund - Encrypted Txn Id - {}", refundPaymentResponse.getEncryptedRefundTransactionId());
        LOGGER.info("Refund Payment Operation Passed");
    }

    
}
