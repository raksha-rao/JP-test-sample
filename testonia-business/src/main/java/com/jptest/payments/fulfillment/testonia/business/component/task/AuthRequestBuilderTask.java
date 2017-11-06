package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import javax.inject.Inject;
import com.jptest.money.AuthRequest;
import com.jptest.money.AuthRequestVO;
import com.jptest.money.BusinessContextVO;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.service.FiManagementHelper;
import com.jptest.payments.fulfillment.testonia.business.vo.money.AuthCaptureAnalyzeVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.AuthInfoVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.AuthRequestVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.BusinessContextVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.CounterPartyVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.OperationIdempotencyVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.PaymentClientInfoVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.PaymentControlVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.PaymentFundingSelectionVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.PaymentInfoVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.PaymentMessageVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.SenderVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.TransactionAuthVOBuilder;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;


/**
 * Builds AuthRequest to be consumed by auth() and fulfill_auth() APIs
 * 
 * @JP Inc.
 */
public class AuthRequestBuilderTask extends BaseTask<AuthRequest> {

    private static final Currency ZERO_USD_CURRENCY = new Currency("USD", 0);
    private static final Byte WTRANSACTION_TYPE_AUTHORIZATION = (byte) '@';
    private static final long WTRANSACTION_FLAG4_WALLET = 0x100L;

    // These can be made enums once other flows are introduced
    private static final Byte COUNTERPARTY_ALIAS_TYPE_EMAIL = 69;
    private static final Byte FUNDING_SOURCE_TYPE_BALANCE = 66;

    private User buyer;
    private User seller;
    private final FulfillPaymentPlanOptions fulfillPaymentPlanOptions;

    @Inject
    private FiManagementHelper fiManagementHelper;

    @Inject
    private PaymentServBridge paymentservBridge;

    public AuthRequestBuilderTask(FulfillPaymentPlanOptions fulfillPaymentPlanOptions) {
        this.fulfillPaymentPlanOptions = fulfillPaymentPlanOptions;
    }

    @Override
    public AuthRequest process(final Context context) {

        buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
        seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());

        final Currency authAmount = fulfillPaymentPlanOptions.getAuthOptions().getAuthAmount().getCurrency();
        final Currency negativeAuthAmount = new Currency(authAmount.getCurrencyCode(), -authAmount.getAmount());

        SenderVOBuilder senderVOBuilder = new SenderVOBuilder()
                .senderAccountNumber(new BigInteger(buyer.getAccountNumber()));
        CounterPartyVOBuilder counterPartyVOBuilder = new CounterPartyVOBuilder()
                .recipientAccountNumber(new BigInteger(seller.getAccountNumber()))
                .counterpartyAlias(seller.getEmailAddress()).counterpartyAliasType(COUNTERPARTY_ALIAS_TYPE_EMAIL);

        PaymentInfoVOBuilder paymentInfoVOBuilder = new PaymentInfoVOBuilder().usdPaymentAmount(ZERO_USD_CURRENCY)
                .paymentAmount(negativeAuthAmount).type(WTRANSACTION_TYPE_AUTHORIZATION)
                .subtype((byte) 0).transactionFlags1(0L)
                .transactionFlags2(0L).transactionFlags3(0L)
                .transactionFlags4(WTRANSACTION_FLAG4_WALLET)
                .transactionFlags5(0L);

        PaymentClientInfoVOBuilder paymentClientInfoVOBuilder = new PaymentClientInfoVOBuilder();

        PaymentControlVOBuilder paymentControlVOBuilder = new PaymentControlVOBuilder().forceBalancePayment(false)
                .avoidUsingBalance(false);

        PaymentMessageVOBuilder paymentMessageVOBuilder = new PaymentMessageVOBuilder()
                .transactionMemo(fulfillPaymentPlanOptions.getAuthOptions().getMemo());

        AuthInfoVOBuilder authInfoVOBuilder = new AuthInfoVOBuilder();

        TransactionAuthVOBuilder transactionAuthVOBuilder = new TransactionAuthVOBuilder()
                .accountNumber(new BigInteger(buyer.getAccountNumber()));

        AuthCaptureAnalyzeVOBuilder authCaptureAnalyzeVOBuilder = new AuthCaptureAnalyzeVOBuilder()
                .amountOverAuth(authAmount)
                .orderId(BigInteger.ZERO).isOrderRelated(false).hasOrderChild(true)
                .tempPHoldTxnId(BigInteger.ZERO)
                .amountReservedFromPBalance(new Currency(authAmount.getCurrencyCode(), 0L))
                .multiCaptureAuth(false).isNegBalanceCaptureAllowed(false).origAuth(transactionAuthVOBuilder)
                .activeAuth(transactionAuthVOBuilder);

        BigInteger newActivityId = paymentservBridge.createActivityId();
        String newIdempotencyString = "IDEM-AUTH-" + paymentservBridge.createActivityId().toString();

        OperationIdempotencyVOBuilder operationIdempotencyVOBuilder = new OperationIdempotencyVOBuilder()
                .activityId(newActivityId).idempotencyString(newIdempotencyString);

        AuthRequestVO authRequestVO = new AuthRequestVOBuilder().isChildAuth(false).orderId(BigInteger.ZERO)
                .sender(senderVOBuilder).counterparty(counterPartyVOBuilder).paymentInfo(paymentInfoVOBuilder)
                .paymentClientInfo(paymentClientInfoVOBuilder)
                .paymentControl(paymentControlVOBuilder)
                .paymentMessage(paymentMessageVOBuilder)
                .fundingSelection(getPaymentFundingSelectionVOBuilder()).authInfo(authInfoVOBuilder)
                .authCaptureAnalyze(authCaptureAnalyzeVOBuilder).operationIdempotencyData(operationIdempotencyVOBuilder)
                .build();

        BusinessContextVO businessContextVO = new BusinessContextVOBuilder()
                .actorAccountNumber(new BigInteger(buyer.getAccountNumber())).actorType(5L).build();

        AuthRequest authRequest = new AuthRequest();
        authRequest.setAuthRequest(authRequestVO);
        authRequest.setBusinessContext(businessContextVO);

        return authRequest;
    }

    public PaymentFundingSelectionVOBuilder getPaymentFundingSelectionVOBuilder() {

        PaymentFundingSelectionVOBuilder paymentFundingSelectionVOBuilder = new PaymentFundingSelectionVOBuilder();

        switch (fulfillPaymentPlanOptions.getFundingSource()) {
            case "HOLDING":
                String holdingId = fiManagementHelper.getHoldingIdForCurrencyAndAccount(buyer.getAccountNumber(),
                        fulfillPaymentPlanOptions.getAuthOptions().getAuthAmount().getCurrency().getCurrencyCode());
                paymentFundingSelectionVOBuilder.fundingSourceType(FUNDING_SOURCE_TYPE_BALANCE)
                        .fundingHoldingId(new BigInteger(holdingId));
                break;
            default:
                return paymentFundingSelectionVOBuilder;
        }

        return paymentFundingSelectionVOBuilder;

    }

}
