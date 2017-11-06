package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.UpdateTransactionPropertyRequest;
import com.jptest.money.UpdateTransactionPropertyResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * UpdateTransactionPropertyTask constructs the UpdateTransactionPropertyRequest , executes update_transaction_property
 * and validates the response
 *
 * @JP Inc.
 */
public class UpdateTransactionPropertyTask
        extends BasePostPaymentOperationTask<UpdateTransactionPropertyRequest, UpdateTransactionPropertyResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateTransactionPropertyTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private WTransactionP20DaoImpl wTxnDAO;

    @Inject
    private CryptoUtil cryptoUtil;
    @Inject
    private PostPaymentExecService postPaymentService;

    @Override
    public UpdateTransactionPropertyRequest constructPostPayRequest(
            final UpdateTransactionPropertyRequest updateTransactionPropertyRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final User buyerData = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);

        try {
            final String buyerAccountNumber = buyerData.getAccountNumber();
            final List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndTypeAndStatus(
                    new BigInteger(buyerAccountNumber),
                    String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
                    String.valueOf(WTransactionConstants.Status.SUCCESS.getValue()));
            updateTransactionPropertyRequest
                    .setTransactionHandle(
                            this.cryptoUtil.encryptTxnId(Long.parseLong(txnId.get(0).toString()),
                                    Long.parseLong(buyerAccountNumber)));
            return updateTransactionPropertyRequest;

        }
        catch (final NumberFormatException e) {
            e.printStackTrace();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }

        return updateTransactionPropertyRequest;
    }

    @Override
    public UpdateTransactionPropertyResponse executePostPay(
            final UpdateTransactionPropertyRequest updateTransactionPropertyRequest, final boolean call2PEX) {
        Assert.assertNotNull(updateTransactionPropertyRequest);

        return this.postPaymentService.updateTransactionPropertyService(updateTransactionPropertyRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final UpdateTransactionPropertyResponse updateTransactionPropertyResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(updateTransactionPropertyResponse,
                "update transaction property should not be null");
        Assert.assertEquals(updateTransactionPropertyResponse.getPropertyResponseAsEnum().getValue().toString(), 
        		postPayRequest.getReturnCode(), this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");
        LOGGER.info("update transaction property Passed");

    }
}
