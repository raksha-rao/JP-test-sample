package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.CreateReserveRequest;
import com.jptest.money.CreateReserveResponse;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.ReserveType;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentSideReferenceExistsValidation;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * CreateReserveOperationTask takes care of constructing the createReserveRequest and executing create_reserve and
 * validates response
 *
 * @JP Inc.
 */

public class CreateReserveOperationTask extends
        BasePostPaymentOperationTask<CreateReserveRequest, CreateReserveResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReserveOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private PopulateActivityIdHelper populateActivityIdHelper;

    private static final String ACTIVITY_TYPE = "CreateHoldActivity";

    @Override
    public CreateReserveRequest constructPostPayRequest(final CreateReserveRequest createReserveRequest,
            final Context context) {
        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String accountNumber = seller.getAccountNumber();
        final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());

        createReserveRequest.setIdempotencyString(activityId.toString());
        createReserveRequest.setAccountNumber(new BigInteger(accountNumber));
        createReserveRequest.setTransactionHandle(fulfillResponse.getFulfillmentHandle());
        if (createReserveRequest.getReserveTypeAsEnum() == ReserveType.LIMITATION_RESERVE
                || createReserveRequest.getReserveTypeAsEnum() == ReserveType.JUMP_START_RESERVES) {
            createReserveRequest.setTransactionHandle(null);
            createReserveRequest.setReleaseTime(null);
        }
        return createReserveRequest;
    }

    @Override
    public CreateReserveResponse executePostPay(final CreateReserveRequest createReserveRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(createReserveRequest);
        final CreateReserveResponse createReserveResponse = this.paymentServBridge
                .createReserve(createReserveRequest);
        return createReserveResponse;
    }

    @Override
    public void assertPostPayResponse(final CreateReserveResponse createReserveResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(createReserveResponse,
                "createReserveResponse should not be null");

        Assert.assertEquals(createReserveResponse.getSuccess().booleanValue(), true,
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed:");

        // TODO: PRS cannot find the txn using this encrypted txn id
        // if (createReserveResponse.getReserveHandle() != null)
        // context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), createReserveResponse.getReserveHandle());

        LOGGER.info("Create Reserve - Encrypted Txn Id - {}", createReserveResponse.getReserveHandle());
        LOGGER.info("Create Reserve Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final CreateReserveResponse response) {
        if (response.getSuccess()) {
            final String decryptedSubBalanceHandle = this.populateActivityIdHelper
                    .getdecryptedSubBalanceHandle(response.getReserveHandle());
            final PaymentSideReferenceExistsValidation paymentSideReferenceExistsValidation = new PaymentSideReferenceExistsValidation(
                    decryptedSubBalanceHandle);
            paymentSideReferenceExistsValidation.validate(context);
            final BigInteger activityId = this.populateActivityIdHelper
                    .getActivityIdFromSubBalanceHandle(decryptedSubBalanceHandle, ACTIVITY_TYPE);

            // Account Level Reserves activity details will not be found since it will not mapped to the particular
            // transaction. Hence activityId would be empty. Empty will not be set in context hence activityId of those
            // kind of reserves set as BigInteger.ZERO
            if (activityId == null) {
                context.setData(this.getActivityIdKey(), BigInteger.ZERO);
            }
            else {
                context.setData(this.getActivityIdKey(), activityId);
            }
            LOGGER.info("CREATE_RESERVE_ACTIVITY_ID: Activity ID - {}, Reserve Handle - {}", activityId,
                    decryptedSubBalanceHandle);
        }

    }

}
