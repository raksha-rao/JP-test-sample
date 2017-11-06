package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.money.AuthRequestVO;

import com.jptest.common.OpaqueDataElementVO;
import com.jptest.marketplaces.AuctionItemVO;
import com.jptest.merchant.AirlineItineraryDataVO;
import com.jptest.money.AdjustAuthInfoVO;
import com.jptest.money.AuthAdviceVO;
import com.jptest.money.AuthCaptureAnalyzeVO;
import com.jptest.money.AuthInfoVO;
import com.jptest.money.ClientCallBackVO;
import com.jptest.money.CounterPartyVO;
import com.jptest.money.EventDataVO;
import com.jptest.money.NotificationVO;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.PaymentClientInfoVO;
import com.jptest.money.PaymentControlVO;
import com.jptest.money.PaymentFlowVO;
import com.jptest.money.PaymentFundingSelectionVO;
import com.jptest.money.PaymentInfoVO;
import com.jptest.money.PaymentInvoiceParametersVO;
import com.jptest.money.PaymentMessageVO;
import com.jptest.money.PaymentParamsMobileVO;
import com.jptest.money.PaymentParamsSkypeVO;
import com.jptest.money.PlanVersionEnum;
import com.jptest.money.SIWaxUserDataVO;
import com.jptest.money.SenderVO;
import com.jptest.money.StandinContextVO;
import com.jptest.money.StoreInfoVO;
import com.jptest.money.TransactionAuthVO;
import com.jptest.money.UserHoldingVO;
import com.jptest.risk.AuctionRiskInfoVO;
import com.jptest.risk.RiskPaymentContextVO;
import com.jptest.risk.RiskVarsAdjustmentVO;
import java.math.BigInteger;


public class AuthRequestVOBuilder implements VOBuilder<AuthRequestVO> {

    private VOBuilder<RiskVarsAdjustmentVO> riskAdjustment = new NullVOBuilder<>();
    private VOBuilder<AirlineItineraryDataVO> airlineItineraryData = new NullVOBuilder<>();
    private VOBuilder<OperationIdempotencyVO> operationIdempotencyData = new NullVOBuilder<>();
    private VOBuilder<RiskVarsAdjustmentVO> extraRiskAdjustment = new NullVOBuilder<>();
    private VOBuilder<RiskPaymentContextVO> riskPaymentContext = new NullVOBuilder<>();
    private VOBuilder<SIWaxUserDataVO> standinWaxUserData = new NullVOBuilder<>();
    private VOBuilder<ClientCallBackVO> clientCallBack = new NullVOBuilder<>();
    private BigInteger dbOrderVersion;
    private Boolean merchantCloseAuth;
    private ListVOBuilder<OpaqueDataElementVO> paymentExtensions = new NullVOBuilder<>();
    private ListVOBuilder<UserHoldingVO> senderHoldingDetails = new NullVOBuilder<>();
    private String channelSettlementId;
    private VOBuilder<AdjustAuthInfoVO> adjustAuthInfo = new NullVOBuilder<>();
    private String planningActivityHandle;
    private Boolean ccChargeInTxnCurrency;
    private VOBuilder<StandinContextVO> standinContext = new NullVOBuilder<>();
    private VOBuilder<PaymentClientInfoVO> paymentClientInfo = new NullVOBuilder<>();
    private VOBuilder<PaymentControlVO> paymentControl = new NullVOBuilder<>();
    private VOBuilder<PaymentMessageVO> paymentMessage = new NullVOBuilder<>();
    private VOBuilder<AuctionRiskInfoVO> auctionRiskInfo = new NullVOBuilder<>();
    private VOBuilder<PaymentInvoiceParametersVO> invoiceParameters = new NullVOBuilder<>();
    private VOBuilder<PaymentFundingSelectionVO> fundingSelection = new NullVOBuilder<>();
    private VOBuilder<AuthCaptureAnalyzeVO> authCaptureAnalyze = new NullVOBuilder<>();
    private BigInteger orderId;
    private Boolean closeAuth;
    private String mayflyId;
    private Boolean closeBankAuth;
    private VOBuilder<StoreInfoVO> storeInfo = new NullVOBuilder<>();
    private VOBuilder<AuthAdviceVO> authAdvice = new NullVOBuilder<>();
    private Boolean isMozartFlow;
//    private Integer planVersion;
    private PlanVersionEnum planVersion;
    private String ccTxnRepaymentInstallmentNumber;
    private VOBuilder<SenderVO> sender = new NullVOBuilder<>();
    private VOBuilder<CounterPartyVO> counterparty = new NullVOBuilder<>();
    private VOBuilder<PaymentInfoVO> paymentInfo = new NullVOBuilder<>();
    private VOBuilder<PaymentParamsMobileVO> mobileParams = new NullVOBuilder<>();
    private VOBuilder<PaymentParamsSkypeVO> skypeParams = new NullVOBuilder<>();
    private ListVOBuilder<AuctionItemVO> auctionItems = new NullVOBuilder<>();
    private VOBuilder<EventDataVO> eventData = new NullVOBuilder<>();
    private VOBuilder<NotificationVO> notification = new NullVOBuilder<>();
    private VOBuilder<PaymentFlowVO> paymentFlow = new NullVOBuilder<>();
    private VOBuilder<TransactionAuthVO> existingAuth = new NullVOBuilder<>();
    private VOBuilder<AuthInfoVO> authInfo = new NullVOBuilder<>();
    private Boolean isChildAuth;

    public static AuthRequestVOBuilder newBuilder() {
        return new AuthRequestVOBuilder();
    }
    public AuthRequestVOBuilder riskAdjustment(VOBuilder<RiskVarsAdjustmentVO> riskAdjustment) {
        this.riskAdjustment = riskAdjustment;
        return this;

    }

    public AuthRequestVOBuilder airlineItineraryData(VOBuilder<AirlineItineraryDataVO> airlineItineraryData) {
        this.airlineItineraryData = airlineItineraryData;
        return this;

    }

    public AuthRequestVOBuilder operationIdempotencyData(VOBuilder<OperationIdempotencyVO> operationIdempotencyData) {
        this.operationIdempotencyData = operationIdempotencyData;
        return this;

    }

    public AuthRequestVOBuilder extraRiskAdjustment(VOBuilder<RiskVarsAdjustmentVO> extraRiskAdjustment) {
        this.extraRiskAdjustment = extraRiskAdjustment;
        return this;

    }

    public AuthRequestVOBuilder riskPaymentContext(VOBuilder<RiskPaymentContextVO> riskPaymentContext) {
        this.riskPaymentContext = riskPaymentContext;
        return this;

    }

    public AuthRequestVOBuilder standinWaxUserData(VOBuilder<SIWaxUserDataVO> standinWaxUserData) {
        this.standinWaxUserData = standinWaxUserData;
        return this;

    }

    public AuthRequestVOBuilder clientCallBack(VOBuilder<ClientCallBackVO> clientCallBack) {
        this.clientCallBack = clientCallBack;
        return this;

    }

    public AuthRequestVOBuilder dbOrderVersion(BigInteger dbOrderVersion) {
        this.dbOrderVersion = dbOrderVersion;
        return this;

    }

    public AuthRequestVOBuilder merchantCloseAuth(Boolean merchantCloseAuth) {
        this.merchantCloseAuth = merchantCloseAuth;
        return this;

    }

    public AuthRequestVOBuilder paymentExtensions(ListVOBuilder<OpaqueDataElementVO> paymentExtensions) {
        this.paymentExtensions = paymentExtensions;
        return this;

    }

    public AuthRequestVOBuilder senderHoldingDetails(ListVOBuilder<UserHoldingVO> senderHoldingDetails) {
        this.senderHoldingDetails = senderHoldingDetails;
        return this;

    }

    public AuthRequestVOBuilder channelSettlementId(String channelSettlementId) {
        this.channelSettlementId = channelSettlementId;
        return this;

    }

    public AuthRequestVOBuilder adjustAuthInfo(VOBuilder<AdjustAuthInfoVO> adjustAuthInfo) {
        this.adjustAuthInfo = adjustAuthInfo;
        return this;

    }

    public AuthRequestVOBuilder planningActivityHandle(String planningActivityHandle) {
        this.planningActivityHandle = planningActivityHandle;
        return this;

    }

    public AuthRequestVOBuilder ccChargeInTxnCurrency(Boolean ccChargeInTxnCurrency) {
        this.ccChargeInTxnCurrency = ccChargeInTxnCurrency;
        return this;

    }

    public AuthRequestVOBuilder standinContext(VOBuilder<StandinContextVO> standinContext) {
        this.standinContext = standinContext;
        return this;

    }

    public AuthRequestVOBuilder paymentClientInfo(VOBuilder<PaymentClientInfoVO> paymentClientInfo) {
        this.paymentClientInfo = paymentClientInfo;
        return this;

    }

    public AuthRequestVOBuilder paymentControl(VOBuilder<PaymentControlVO> paymentControl) {
        this.paymentControl = paymentControl;
        return this;

    }

    public AuthRequestVOBuilder paymentMessage(VOBuilder<PaymentMessageVO> paymentMessage) {
        this.paymentMessage = paymentMessage;
        return this;

    }

    public AuthRequestVOBuilder auctionRiskInfo(VOBuilder<AuctionRiskInfoVO> auctionRiskInfo) {
        this.auctionRiskInfo = auctionRiskInfo;
        return this;

    }

    public AuthRequestVOBuilder invoiceParameters(VOBuilder<PaymentInvoiceParametersVO> invoiceParameters) {
        this.invoiceParameters = invoiceParameters;
        return this;

    }

    public AuthRequestVOBuilder fundingSelection(VOBuilder<PaymentFundingSelectionVO> fundingSelection) {
        this.fundingSelection = fundingSelection;
        return this;

    }

    public AuthRequestVOBuilder authCaptureAnalyze(VOBuilder<AuthCaptureAnalyzeVO> authCaptureAnalyze) {
        this.authCaptureAnalyze = authCaptureAnalyze;
        return this;

    }

    public AuthRequestVOBuilder orderId(BigInteger orderId) {
        this.orderId = orderId;
        return this;

    }

    public AuthRequestVOBuilder closeAuth(Boolean closeAuth) {
        this.closeAuth = closeAuth;
        return this;

    }

    public AuthRequestVOBuilder mayflyId(String mayflyId) {
        this.mayflyId = mayflyId;
        return this;

    }

    public AuthRequestVOBuilder closeBankAuth(Boolean closeBankAuth) {
        this.closeBankAuth = closeBankAuth;
        return this;

    }

    public AuthRequestVOBuilder storeInfo(VOBuilder<StoreInfoVO> storeInfo) {
        this.storeInfo = storeInfo;
        return this;

    }

    public AuthRequestVOBuilder authAdvice(VOBuilder<AuthAdviceVO> authAdvice) {
        this.authAdvice = authAdvice;
        return this;

    }

    public AuthRequestVOBuilder isMozartFlow(Boolean isMozartFlow) {
        this.isMozartFlow = isMozartFlow;
        return this;

    }

//    public AuthRequestVOBuilder planVersion(Integer planVersion) {
//        this.planVersion = planVersion;
//        return this;
//
//    }

    public AuthRequestVOBuilder planVersion(PlanVersionEnum planVersion) {
        this.planVersion = planVersion;
        return this;

    }

    public AuthRequestVOBuilder ccTxnRepaymentInstallmentNumber(String ccTxnRepaymentInstallmentNumber) {
        this.ccTxnRepaymentInstallmentNumber = ccTxnRepaymentInstallmentNumber;
        return this;

    }

    public AuthRequestVOBuilder sender(VOBuilder<SenderVO> sender) {
        this.sender = sender;
        return this;

    }

    public AuthRequestVOBuilder counterparty(VOBuilder<CounterPartyVO> counterparty) {
        this.counterparty = counterparty;
        return this;

    }

    public AuthRequestVOBuilder paymentInfo(VOBuilder<PaymentInfoVO> paymentInfo) {
        this.paymentInfo = paymentInfo;
        return this;

    }

    public AuthRequestVOBuilder mobileParams(VOBuilder<PaymentParamsMobileVO> mobileParams) {
        this.mobileParams = mobileParams;
        return this;

    }

    public AuthRequestVOBuilder skypeParams(VOBuilder<PaymentParamsSkypeVO> skypeParams) {
        this.skypeParams = skypeParams;
        return this;

    }

    public AuthRequestVOBuilder auctionItems(ListVOBuilder<AuctionItemVO> auctionItems) {
        this.auctionItems = auctionItems;
        return this;

    }

    public AuthRequestVOBuilder eventData(VOBuilder<EventDataVO> eventData) {
        this.eventData = eventData;
        return this;

    }

    public AuthRequestVOBuilder notification(VOBuilder<NotificationVO> notification) {
        this.notification = notification;
        return this;

    }

    public AuthRequestVOBuilder paymentFlow(VOBuilder<PaymentFlowVO> paymentFlow) {
        this.paymentFlow = paymentFlow;
        return this;

    }

    public AuthRequestVOBuilder existingAuth(VOBuilder<TransactionAuthVO> existingAuth) {
        this.existingAuth = existingAuth;
        return this;

    }

    public AuthRequestVOBuilder authInfo(VOBuilder<AuthInfoVO> authInfo) {
        this.authInfo = authInfo;
        return this;

    }

    public AuthRequestVOBuilder isChildAuth(Boolean isChildAuth) {
        this.isChildAuth = isChildAuth;
        return this;

    }

    @Override
    public AuthRequestVO build() {
          AuthRequestVO vo = new AuthRequestVO();
          vo.setRiskAdjustment(riskAdjustment.build());
          vo.setAirlineItineraryData(airlineItineraryData.build());
          vo.setOperationIdempotencyData(operationIdempotencyData.build());
          vo.setExtraRiskAdjustment(extraRiskAdjustment.build());
          vo.setRiskPaymentContext(riskPaymentContext.build());
          vo.setStandinWaxUserData(standinWaxUserData.build());
          vo.setClientCallBack(clientCallBack.build());
          vo.setDbOrderVersion(dbOrderVersion);
          vo.setMerchantCloseAuth(merchantCloseAuth);
          vo.setPaymentExtensions(paymentExtensions.buildList());
          vo.setSenderHoldingDetails(senderHoldingDetails.buildList());
          vo.setChannelSettlementId(channelSettlementId);
          vo.setAdjustAuthInfo(adjustAuthInfo.build());
          vo.setPlanningActivityHandle(planningActivityHandle);
          vo.setCcChargeInTxnCurrency(ccChargeInTxnCurrency);
          vo.setStandinContext(standinContext.build());
          vo.setPaymentClientInfo(paymentClientInfo.build());
          vo.setPaymentControl(paymentControl.build());
          vo.setPaymentMessage(paymentMessage.build());
          vo.setAuctionRiskInfo(auctionRiskInfo.build());
          vo.setInvoiceParameters(invoiceParameters.build());
          vo.setFundingSelection(fundingSelection.build());
          vo.setAuthCaptureAnalyze(authCaptureAnalyze.build());
          vo.setOrderId(orderId);
          vo.setCloseAuth(closeAuth);
          vo.setMayflyId(mayflyId);
          vo.setCloseBankAuth(closeBankAuth);
          vo.setStoreInfo(storeInfo.build());
          vo.setAuthAdvice(authAdvice.build());
          vo.setIsMozartFlow(isMozartFlow);
          vo.setPlanVersion(planVersion);
//          vo.setPlanVersion(planVersion);
          vo.setCcTxnRepaymentInstallmentNumber(ccTxnRepaymentInstallmentNumber);
          vo.setSender(sender.build());
          vo.setCounterparty(counterparty.build());
          vo.setPaymentInfo(paymentInfo.build());
          vo.setMobileParams(mobileParams.build());
          vo.setSkypeParams(skypeParams.build());
          vo.setAuctionItems(auctionItems.buildList());
          vo.setEventData(eventData.build());
          vo.setNotification(notification.build());
          vo.setPaymentFlow(paymentFlow.build());
          vo.setExistingAuth(existingAuth.build());
          vo.setAuthInfo(authInfo.build());
          vo.setIsChildAuth(isChildAuth);
          return vo;
    }
}
