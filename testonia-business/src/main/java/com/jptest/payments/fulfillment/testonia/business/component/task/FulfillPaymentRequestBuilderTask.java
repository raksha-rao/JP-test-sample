package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.financialinstrument.GetFisByAccountResponse;
import com.jptest.financialinstrument.WalletInstrumentVO;
import com.jptest.money.FulfillPaymentRequest;
import com.jptest.money.FulfillmentContextVO;
import com.jptest.money.FulfillmentMemoVO;
import com.jptest.money.FulfillmentPlanVO;
import com.jptest.money.FulfillmentTransactionUnitVO;
import com.jptest.money.FulfillmentUniqueIdConstraintVO;
import com.jptest.money.FundingMethodType;
import com.jptest.money.MoneyPlanning;
import com.jptest.money.PaymentMemoVO;
import com.jptest.money.UniqueIdType;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableBaseTask;
import com.jptest.payments.fulfillment.testonia.business.component.task.exception.PlanCreationFailedException;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions.CurrencyData;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.test.asg.common.utils.planningserv.PaymentPlanningUtils;
import com.jptest.test.money.constants.PaymentFlowType;
import com.jptest.test.money.util.config.ConfigHelper;
import com.jptest.test.money.util.db.user.UserDAO;
import com.jptest.test.money.util.request.fulfillment.PlanPaymentRequestUtility;
import com.jptest.test.money.util.servicehelper.payment.PaymentOptions;
import com.jptest.test.money.util.servicehelper.payment.PaymentServHelper;
import com.jptest.test.money.util.servicehelper.user.UserInfo;
import com.jptest.test.money.util.servicehelper.user.UserInfoHelper;
import com.jptest.test.money.util.voutil.money.PaymentActionsVOFiller;
import com.jptest.test.money.util.voutil.money.PaymentVOFillerHelper;
import com.jptest.types.Currency;

public class FulfillPaymentRequestBuilderTask extends RetriableBaseTask<FulfillPaymentRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FulfillPaymentRequestBuilderTask.class);

    @Inject
    private UserInfoHelper userInfoHelper;

    @Inject
    protected PaymentVOFillerHelper paymentVOFillerHelper;

    @Inject
    private PaymentServHelper paymentServHelper;

    @Inject
    protected UserDAO userDAO;

    @Inject
    protected PaymentPlanningUtils paymentPlanningUtils;

    @Inject
    protected ConfigHelper configHelper;

    @Inject
    @Named("moneyplanningserv_ca")
    protected MoneyPlanning moneyPlanningClient;

    private PaymentActionsVOFiller paymentActionsVOFiller;
    private PlanPaymentRequestUtility planPaymentRequestUtility;

    private PaymentOptions input;
    
    private String buyerKey;
    private String sellerKey;

    public FulfillPaymentRequestBuilderTask(PaymentOptions input) {
        this.paymentActionsVOFiller = new PaymentActionsVOFiller(this.paymentServHelper, this.configHelper,
                this.paymentVOFillerHelper,
                this.userDAO);
        this.planPaymentRequestUtility = new PlanPaymentRequestUtility(this.moneyPlanningClient,
                this.paymentActionsVOFiller,
                this.paymentVOFillerHelper, this.paymentPlanningUtils);
        this.input = input;
        this.buyerKey = ContextKeys.BUYER_VO_KEY.getName();
        this.sellerKey = ContextKeys.SELLER_VO_KEY.getName();
    }
    
    public FulfillPaymentRequestBuilderTask(FulfillPaymentPlanOptions fulfillPaymentOptions, String buyerVOKey, String sellerVOKey) {
    	this(fulfillPaymentOptions);
    	this.buyerKey = buyerVOKey;
        this.sellerKey = sellerVOKey;
    }

    public FulfillPaymentRequestBuilderTask(FulfillPaymentPlanOptions fulfillPaymentOptions) {
        this(buildPaymentOptions(fulfillPaymentOptions));
    }

    private static PaymentOptions buildPaymentOptions(FulfillPaymentPlanOptions fulfillPaymentOptions) {
        final PaymentOptions options = new PaymentOptions();
        if (fulfillPaymentOptions.getPaymentFlowType() != null) {
            options.setPaymentFlowType(
                    PaymentFlowType.getPaymentFlowType(fulfillPaymentOptions.getPaymentFlowType().getType()));
        }
        if (fulfillPaymentOptions.getMemo() != null) {
            options.setMemo(fulfillPaymentOptions.getMemo());
        }
        final CurrencyData txnAmount = fulfillPaymentOptions.getTxnAmount();
        options.setTxnAmount(new Currency(txnAmount.getCurrencyCode(), Long.parseLong(txnAmount.getAmount())));
        if (fulfillPaymentOptions.getFundingSource() != null) {
            options.setFundingSource(FundingMethodType.getEnumByName(fulfillPaymentOptions.getFundingSource()));
        }
        options.setBackupFundingType(fulfillPaymentOptions.getBackupFundingType());
        options.setUseBalance(fulfillPaymentOptions.isUseBalance());
        options.setForceUserSpecifiedFundingInstrument(fulfillPaymentOptions.isForceUserSpecifiedFundingInstrument());
        options.setActionId(fulfillPaymentOptions.getActionId());

        if (fulfillPaymentOptions.getFulfillmentType() != null) {
            options.setFulfillmentType(fulfillPaymentOptions.getFulfillmentType());
        }
        options.setIdempotencyToken(fulfillPaymentOptions.getIdempotencyToken());
        options.setIsS2F(fulfillPaymentOptions.isS2F());
        options.setIsMobile(fulfillPaymentOptions.isMobile());
        return options;
    }

    private FulfillPaymentRequest buildFulfillPaymentRequest(final Context context) {
        try {
            final User buyer = (User) this.getDataFromContext(context, buyerKey);
            final User seller = (User) this.getDataFromContext(context, sellerKey);
            input.setSender(buyer);
            input.setRecipient(seller);
            return this.buildRequest(buyer, seller, input);
        } catch (final Throwable e) {
            // Ideally shouldn't be catching throwable but the CQES util is ready only 
            // and that is where the assertion error is coming from so no other option
            return null;
        }

    }

    private FulfillPaymentRequest buildRequest(final User buyer, final User seller, final PaymentOptions paymentOptions)
            throws IOException {

        final UserInfo buyerInfo = this.userInfoHelper.getUserInfo(buyer.getAccountNumber());
        this.appendFundingInstruments(buyerInfo.getGetFisByAccountResponse(), paymentOptions);
        final UserInfo sellerInfo = this.userInfoHelper.getUserInfo(seller.getAccountNumber());

        this.paymentActionsVOFiller.init(buyerInfo, sellerInfo, paymentOptions);

        final FulfillPaymentRequest fulfillPayRequest = new FulfillPaymentRequest();

        final FulfillmentContextVO fulfillmentContextVO = this.paymentVOFillerHelper
                .buildFulfillmentContextVO(paymentOptions);

        fulfillPayRequest.setActorInfo(this.paymentActionsVOFiller.fillActorInfoVO());

        if (paymentOptions.getIdempotencyToken() != null) {
            fulfillPayRequest.setIdempotenceId(paymentOptions.getIdempotencyToken());
        } else {
            fulfillPayRequest.setIdempotenceId(this.paymentServHelper.createActivityId().toString());
        }

        this.buildPlanVO(buyerInfo, sellerInfo, paymentOptions, fulfillPayRequest, fulfillmentContextVO);

        /*
         * fulfillPayRequest.setFulfillmentPlan(paymentActionsVOFiller. fillFulfillmentPlanVO(getProtectionVOList()));
         * fulfillPayRequest.getFulfillmentPlan().setPlanVersion( paymentActionsVOFiller.getPlanVersion());
         */

        final FulfillmentUniqueIdConstraintVO fulfillmentUniqueIdConstraintVO = new FulfillmentUniqueIdConstraintVO();
        fulfillmentUniqueIdConstraintVO.setIdType(UniqueIdType.LEGACY_DATA_SET);
        /*
         * If invoiceId is not passed in paymentOptions use system time.
         */
        String invoiceId = paymentOptions.getInvoiceId();
        if (invoiceId == null || invoiceId.isEmpty()) {
            invoiceId = String.valueOf(System.currentTimeMillis());
        }
        fulfillmentUniqueIdConstraintVO.setUniqueId(seller.getAccountNumber() + "_" + invoiceId);
        for (int index = 0; index < fulfillmentContextVO.getTransactionUnits().size(); index++) {
            fulfillmentContextVO.getTransactionUnits().get(index)
                    .setUniqueIdConstraint(fulfillmentUniqueIdConstraintVO);
        }
        fulfillPayRequest.setFulfillmentContext(fulfillmentContextVO);

        return fulfillPayRequest;
    }

    private void buildPlanVO(final UserInfo buyerInfo, final UserInfo sellerInfo, final PaymentOptions paymentOptions,
            final FulfillPaymentRequest fulfillPayRequest, final FulfillmentContextVO fulfillmentContextVO)
                    throws IOException {
        final FulfillmentPlanVO fulfillmentPlanVO = this.planPaymentRequestUtility.getFulfillmentPlanVO(paymentOptions,
                buyerInfo,
                sellerInfo);

        if (fulfillmentContextVO.getTransactionUnitMemos() == null) {
            final List<FulfillmentMemoVO> transactionUnitMemos = new ArrayList<FulfillmentMemoVO>();
            final FulfillmentMemoVO fulfillmentMemoVO = new FulfillmentMemoVO();
            final PaymentMemoVO paymentMemoVO = new PaymentMemoVO();
            paymentMemoVO.setDetailedDescription(paymentOptions.getMemo());
            fulfillmentMemoVO.setPaymentPlanStrategyId(fulfillmentPlanVO.getPaymentPlans().get(0).getStrategyId());
            fulfillmentMemoVO.setTransactionUnitId(
                    fulfillmentPlanVO.getPaymentPlans().get(0).getTransactionUnitPlans().get(0).getTransactionUnitId());
            fulfillmentMemoVO.setMessage(paymentMemoVO);
            transactionUnitMemos.add(fulfillmentMemoVO);
            fulfillmentContextVO.setTransactionUnitMemos(transactionUnitMemos);
        }

        if (fulfillmentContextVO.getTransactionUnits() == null) {
            final List<FulfillmentTransactionUnitVO> transactionUnits = new ArrayList<FulfillmentTransactionUnitVO>();
            final FulfillmentTransactionUnitVO fulfillmentTransactionUnitVO = new FulfillmentTransactionUnitVO();
            fulfillmentTransactionUnitVO.setTransactionUnitId("1");
            transactionUnits.add(fulfillmentTransactionUnitVO);
            fulfillmentContextVO.setTransactionUnits(transactionUnits);
        }

        fulfillPayRequest.setFulfillmentPlan(fulfillmentPlanVO);
    }

    /**
     * This method adds list of WalletInstruments which can't be loaded using get_fis_by_account to the instruments
     * returned by get_fis_by_account
     *
     * @param {@link
     *            UserInfo}
     * @param list
     *            of {@link WalletInstrumentVO}
     */
    private void appendFundingInstruments(final GetFisByAccountResponse getFisByAccountResponse,
            final PaymentOptions paymentOptions) {

        if (paymentOptions.getFundingInstrumentList() != null && paymentOptions.getFundingInstrumentList().size() > 0) {
            // getFisByAccountResponse().getInstruments().getInstrument().addAll(walletInstrumentList);
            for (final WalletInstrumentVO walletInstrumentVO : paymentOptions.getFundingInstrumentList()) {
                getFisByAccountResponse.getInstruments().getInstrument().add(walletInstrumentVO);
            }
        }

    }

    @Override
    protected boolean isDesiredOutput(FulfillPaymentRequest output) {
        return output != null;
    }

    @Override
    protected FulfillPaymentRequest retriableExecute(Context context) {
        return buildFulfillPaymentRequest(context);
    }

    @Override
    protected FulfillPaymentRequest onSuccess(Context context, FulfillPaymentRequest output) {
        LOGGER.info("constructed the FulfillPaymentRequest successfully");
        return output;
    }

    @Override
    protected FulfillPaymentRequest onFailure(Context context, FulfillPaymentRequest output) {
        throw new PlanCreationFailedException("Plan creation failed");
    }

}
