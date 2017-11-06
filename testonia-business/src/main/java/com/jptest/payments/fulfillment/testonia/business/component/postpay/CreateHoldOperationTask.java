package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.CreateHoldRequest;
import com.jptest.money.CreateHoldResponse;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentSideReferenceExistsValidation;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * CreateHoldOperationTask takes care of constructing the createHoldRequest and executing create_hold and validates
 * response
 *
 * @JP Inc.
 */
public class CreateHoldOperationTask
        extends
        BasePostPaymentOperationTask<CreateHoldRequest, CreateHoldResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateHoldOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private PopulateActivityIdHelper populateActivityIdHelper;

    private static final String ACTIVITY_TYPE = "CreateHoldActivity";

    @Override
    public CreateHoldRequest constructPostPayRequest(
            final CreateHoldRequest createHoldRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String accountNumber = seller.getAccountNumber();
        final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());

        createHoldRequest.setIdempotencyString(activityId.toString());
        createHoldRequest.setAccountNumber(new BigInteger(accountNumber));
        createHoldRequest.setTransactionHandle(fulfillResponse.getFulfillmentHandle());

        return createHoldRequest;
    }

    @Override
    public CreateHoldResponse executePostPay(
            final CreateHoldRequest createHoldRequest, final boolean call2PEX) {
        Assert.assertNotNull(createHoldRequest);
        final CreateHoldResponse createHoldResponse = this.paymentServBridge
                .createHold(createHoldRequest);
        return createHoldResponse;
    }

    @Override
    public void assertPostPayResponse(final CreateHoldResponse createHoldResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(createHoldResponse,
        		this.getClass().getSimpleName() 
        		+ ". assertPostPayResponse() createHoldResponse should not be null");

        Assert.assertEquals(createHoldResponse.getSuccess().booleanValue(), true,
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed:");

        if (createHoldResponse.getHoldHandle() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), createHoldResponse.getHoldHandle());

        LOGGER.info("Create Hold - Encrypted Txn Id - {}", createHoldResponse.getHoldHandle());
        LOGGER.info("Create Hold Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final CreateHoldResponse response) {
        if (response.getSuccess()) {
            final String decryptedSubBalanceHandle = this.populateActivityIdHelper
                    .getdecryptedSubBalanceHandle(response.getHoldHandle());
            final PaymentSideReferenceExistsValidation paymentSideReferenceExistsValidation = new PaymentSideReferenceExistsValidation(
                    decryptedSubBalanceHandle);
            paymentSideReferenceExistsValidation.validate(context);
            final BigInteger activityId = this.populateActivityIdHelper
                    .getActivityIdFromSubBalanceHandle(decryptedSubBalanceHandle, ACTIVITY_TYPE);
            Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
            		+ ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(), activityId);
            LOGGER.info("CREATE_HOLD_ACTIVITY_ID: Activity ID - {}, Holds Handle - {}", activityId,
                    decryptedSubBalanceHandle);
        }

    }
}
