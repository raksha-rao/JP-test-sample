package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.WithdrawRequest;
import com.jptest.money.WithdrawResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * WithdrawlsOperationTask constructs the withdrawls request, calls withdrawl operaiton and validates teh response
 *
 * @JP Inc.
 */
public class WithdrawlsOperationTask
        extends BasePostPaymentOperationTask<WithdrawRequest, WithdrawResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawlsOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Override
    public WithdrawRequest constructPostPayRequest(final WithdrawRequest withdrawRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final User sellerData = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String sellerAccountNumber = sellerData.getAccountNumber();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);

        withdrawRequest.setOperationIdempotencyData(operationIdempotencyVO);

        withdrawRequest.setOperationIdempotencyData(operationIdempotencyVO);
        withdrawRequest.setAccountNumber(new BigInteger(sellerAccountNumber));
        withdrawRequest.getActorInfo().setActorAuthType(Byte.parseByte("80"));
        withdrawRequest.getActorInfo()
                .setActorAccountNumber(new BigInteger(sellerAccountNumber));
        withdrawRequest.getWithdrawalMethodInfo().setWithdrawalInstrumentId(
                new BigInteger(sellerData.getBank().iterator().next().getBankId()));

        return withdrawRequest;
    }

    @Override
    public WithdrawResponse executePostPay(final WithdrawRequest withdrawRequest, final boolean call2PEX) {
        Assert.assertNotNull(withdrawRequest);
        final WithdrawResponse response = this.paymentServBridge
                .doWithdrawal(withdrawRequest);
        return response;
    }

    @Override
    public void assertPostPayResponse(final WithdrawResponse withdrawResponse, final PostPaymentRequest postPayRequest,
            final Context context) {
        final WithdrawResponse response = withdrawResponse;
        Assert.assertNotNull(response,
                "post payment response should not be null");
        Assert.assertEquals(response.getResult().toString(), postPayRequest.getReturnCode(),
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (withdrawResponse.getTransactionHandle() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), withdrawResponse.getTransactionHandle());

        LOGGER.info("Withdraw - Encrypted Txn Id - {}", withdrawResponse.getTransactionHandle());
        LOGGER.info("Withdraw Operation Passed");
    }
}
