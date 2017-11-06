package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.money.PaymentControlVO;
import com.jptest.financialinstrument.WalletPreferenceDetailsVO;
import com.jptest.money.DelayedDisbursementScheduleVO;
import com.jptest.money.DisbursementDetailsVO;
import com.jptest.money.ExternalFeeDetailsArrayVO;
import com.jptest.money.ExternalFeeDetailsVO;
import com.jptest.money.ExternalTxnType;
import com.jptest.money.MultilegPaymentsDataVO;

import java.math.BigInteger;

public class PaymentControlVOBuilder implements VOBuilder<PaymentControlVO> {

	private Boolean bypassShipping = Boolean.FALSE;
	private Boolean confirmedEmailNotRequired = Boolean.FALSE;
	private Boolean bypassAcceptserv = Boolean.FALSE;
	private Boolean forceBalancePayment = Boolean.FALSE;
	private Boolean useExistingAuthForAlwaysCapture = Boolean.FALSE;
	private Boolean allowAsyncCredit = Boolean.FALSE;
	private Long applyFeeTo;
	private Boolean bypassRecipientHighRestrictedCheck = Boolean.FALSE;
	private Boolean requiresInstantPayment = Boolean.FALSE;
	private Boolean bypassRiskModels = Boolean.FALSE;
	private Boolean bypassReceivingLimitsCheck = Boolean.FALSE;
	private Boolean ignoreEcheckPreference = Boolean.FALSE;
	private Boolean overrideDeclineIfAccountRestricted = Boolean.FALSE;
	private Boolean avoidUsingBalance = Boolean.FALSE;
	private Boolean doNotRepairNegativeBalance = Boolean.FALSE;
	private Boolean wwaxUserCountIncrement = Boolean.FALSE;
	private Boolean paymentPendingUserCommitment = Boolean.FALSE;
	private BigInteger pushFundExpirationTime;
	private Boolean enableCacheResponse = Boolean.FALSE;
	private Boolean isNotInstantFunding = Boolean.FALSE;
	private Boolean isForcedPostTransaction = Boolean.FALSE;
	private VOBuilder<ExternalFeeDetailsVO> externalFeeDetails = new NullVOBuilder<>();
	private VOBuilder<ExternalFeeDetailsArrayVO> externalFeeDetailsArray = new NullVOBuilder<>();
	private Boolean generateApprovalCode = Boolean.FALSE;
	private ListVOBuilder<WalletPreferenceDetailsVO> walletPreferenceDetails = new NullVOBuilder<>();
	private Boolean processThroughShadowSystem = Boolean.FALSE;
	private Boolean isjptestStandinForFundingSourceErrors = Boolean.FALSE;
	private Boolean isTransactionIcPlusEnabled = Boolean.FALSE;
	private ExternalTxnType externalTxnType;
	private String txnAcquirerCountryCode;
	private Boolean isZeroDollarAuth = Boolean.FALSE;
	private VOBuilder<DisbursementDetailsVO> disbursementInfo = new NullVOBuilder<>();
	private VOBuilder<DelayedDisbursementScheduleVO> delayedDisbursementSchedule = new NullVOBuilder<>();
	private Boolean isShortPayTransaction = Boolean.FALSE;
	private VOBuilder<MultilegPaymentsDataVO> multilegPaymentsDataVo = new NullVOBuilder<>();
	private Boolean delayedPostprocessing = Boolean.FALSE;

	public static PaymentControlVOBuilder newBuilder() {
		return new PaymentControlVOBuilder();
	}

	public PaymentControlVOBuilder bypassShipping(Boolean bypassShipping) {
		this.bypassShipping = bypassShipping;
		return this;

	}

	public PaymentControlVOBuilder confirmedEmailNotRequired(
			Boolean confirmedEmailNotRequired) {
		this.confirmedEmailNotRequired = confirmedEmailNotRequired;
		return this;

	}

	public PaymentControlVOBuilder bypassAcceptserv(Boolean bypassAcceptserv) {
		this.bypassAcceptserv = bypassAcceptserv;
		return this;

	}

	public PaymentControlVOBuilder forceBalancePayment(
			Boolean forceBalancePayment) {
		this.forceBalancePayment = forceBalancePayment;
		return this;

	}

	public PaymentControlVOBuilder useExistingAuthForAlwaysCapture(
			Boolean useExistingAuthForAlwaysCapture) {
		this.useExistingAuthForAlwaysCapture = useExistingAuthForAlwaysCapture;
		return this;

	}

	public PaymentControlVOBuilder allowAsyncCredit(Boolean allowAsyncCredit) {
		this.allowAsyncCredit = allowAsyncCredit;
		return this;

	}

	public PaymentControlVOBuilder applyFeeTo(Long applyFeeTo) {
		this.applyFeeTo = applyFeeTo;
		return this;

	}

	public PaymentControlVOBuilder bypassRecipientHighRestrictedCheck(
			Boolean bypassRecipientHighRestrictedCheck) {
		this.bypassRecipientHighRestrictedCheck = bypassRecipientHighRestrictedCheck;
		return this;

	}

	public PaymentControlVOBuilder requiresInstantPayment(
			Boolean requiresInstantPayment) {
		this.requiresInstantPayment = requiresInstantPayment;
		return this;

	}

	public PaymentControlVOBuilder bypassRiskModels(Boolean bypassRiskModels) {
		this.bypassRiskModels = bypassRiskModels;
		return this;

	}

	public PaymentControlVOBuilder bypassReceivingLimitsCheck(
			Boolean bypassReceivingLimitsCheck) {
		this.bypassReceivingLimitsCheck = bypassReceivingLimitsCheck;
		return this;

	}

	public PaymentControlVOBuilder ignoreEcheckPreference(
			Boolean ignoreEcheckPreference) {
		this.ignoreEcheckPreference = ignoreEcheckPreference;
		return this;

	}

	public PaymentControlVOBuilder overrideDeclineIfAccountRestricted(
			Boolean overrideDeclineIfAccountRestricted) {
		this.overrideDeclineIfAccountRestricted = overrideDeclineIfAccountRestricted;
		return this;

	}

	public PaymentControlVOBuilder avoidUsingBalance(Boolean avoidUsingBalance) {
		this.avoidUsingBalance = avoidUsingBalance;
		return this;

	}

	public PaymentControlVOBuilder doNotRepairNegativeBalance(
			Boolean doNotRepairNegativeBalance) {
		this.doNotRepairNegativeBalance = doNotRepairNegativeBalance;
		return this;

	}

	public PaymentControlVOBuilder wwaxUserCountIncrement(
			Boolean wwaxUserCountIncrement) {
		this.wwaxUserCountIncrement = wwaxUserCountIncrement;
		return this;

	}

	public PaymentControlVOBuilder paymentPendingUserCommitment(
			Boolean paymentPendingUserCommitment) {
		this.paymentPendingUserCommitment = paymentPendingUserCommitment;
		return this;

	}

	public PaymentControlVOBuilder pushFundExpirationTime(
			BigInteger pushFundExpirationTime) {
		this.pushFundExpirationTime = pushFundExpirationTime;
		return this;

	}

	public PaymentControlVOBuilder enableCacheResponse(
			Boolean enableCacheResponse) {
		this.enableCacheResponse = enableCacheResponse;
		return this;

	}

	public PaymentControlVOBuilder isNotInstantFunding(
			Boolean isNotInstantFunding) {
		this.isNotInstantFunding = isNotInstantFunding;
		return this;

	}

	public PaymentControlVOBuilder isForcedPostTransaction(
			Boolean isForcedPostTransaction) {
		this.isForcedPostTransaction = isForcedPostTransaction;
		return this;

	}

	public PaymentControlVOBuilder externalFeeDetails(
			VOBuilder<ExternalFeeDetailsVO> externalFeeDetails) {
		this.externalFeeDetails = externalFeeDetails;
		return this;

	}

	public PaymentControlVOBuilder externalFeeDetailsArray(
			VOBuilder<ExternalFeeDetailsArrayVO> externalFeeDetailsArray) {
		this.externalFeeDetailsArray = externalFeeDetailsArray;
		return this;

	}

	public PaymentControlVOBuilder generateApprovalCode(
			Boolean generateApprovalCode) {
		this.generateApprovalCode = generateApprovalCode;
		return this;

	}

	public PaymentControlVOBuilder walletPreferenceDetails(
			ListVOBuilder<WalletPreferenceDetailsVO> walletPreferenceDetails) {
		this.walletPreferenceDetails = walletPreferenceDetails;
		return this;

	}

	public PaymentControlVOBuilder processThroughShadowSystem(
			Boolean processThroughShadowSystem) {
		this.processThroughShadowSystem = processThroughShadowSystem;
		return this;

	}

	public PaymentControlVOBuilder isjptestStandinForFundingSourceErrors(
			Boolean isjptestStandinForFundingSourceErrors) {
		this.isjptestStandinForFundingSourceErrors = isjptestStandinForFundingSourceErrors;
		return this;

	}

	public PaymentControlVOBuilder isTransactionIcPlusEnabled(
			Boolean isTransactionIcPlusEnabled) {
		this.isTransactionIcPlusEnabled = isTransactionIcPlusEnabled;
		return this;

	}

	public PaymentControlVOBuilder externalTxnType(
			ExternalTxnType externalTxnType) {
		this.externalTxnType = externalTxnType;
		return this;

	}

	public PaymentControlVOBuilder txnAcquirerCountryCode(
			String txnAcquirerCountryCode) {
		this.txnAcquirerCountryCode = txnAcquirerCountryCode;
		return this;

	}

	public PaymentControlVOBuilder isZeroDollarAuth(Boolean isZeroDollarAuth) {
		this.isZeroDollarAuth = isZeroDollarAuth;
		return this;

	}

	public PaymentControlVOBuilder disbursementInfo(
			VOBuilder<DisbursementDetailsVO> disbursementInfo) {
		this.disbursementInfo = disbursementInfo;
		return this;

	}

	public PaymentControlVOBuilder delayedDisbursementSchedule(
			VOBuilder<DelayedDisbursementScheduleVO> delayedDisbursementSchedule) {
		this.delayedDisbursementSchedule = delayedDisbursementSchedule;
		return this;

	}

	public PaymentControlVOBuilder isShortPayTransaction(
			Boolean isShortPayTransaction) {
		this.isShortPayTransaction = isShortPayTransaction;
		return this;

	}

	public PaymentControlVOBuilder multilegPaymentsDataVo(
			VOBuilder<MultilegPaymentsDataVO> multilegPaymentsDataVo) {
		this.multilegPaymentsDataVo = multilegPaymentsDataVo;
		return this;

	}

	public PaymentControlVOBuilder delayedPostprocessing(
			Boolean delayedPostprocessing) {
		this.delayedPostprocessing = delayedPostprocessing;
		return this;

	}

	@Override
	public PaymentControlVO build() {
		PaymentControlVO vo = new PaymentControlVO();
		vo.setBypassShipping(bypassShipping);
		vo.setConfirmedEmailNotRequired(confirmedEmailNotRequired);
		vo.setBypassAcceptserv(bypassAcceptserv);
		vo.setForceBalancePayment(forceBalancePayment);
		vo.setUseExistingAuthForAlwaysCapture(useExistingAuthForAlwaysCapture);
		vo.setAllowAsyncCredit(allowAsyncCredit);
		vo.setApplyFeeTo(applyFeeTo);
		vo.setBypassRecipientHighRestrictedCheck(bypassRecipientHighRestrictedCheck);
		vo.setRequiresInstantPayment(requiresInstantPayment);
		vo.setBypassRiskModels(bypassRiskModels);
		vo.setBypassReceivingLimitsCheck(bypassReceivingLimitsCheck);
		vo.setIgnoreEcheckPreference(ignoreEcheckPreference);
		vo.setOverrideDeclineIfAccountRestricted(overrideDeclineIfAccountRestricted);
		vo.setAvoidUsingBalance(avoidUsingBalance);
		vo.setDoNotRepairNegativeBalance(doNotRepairNegativeBalance);
		vo.setWwaxUserCountIncrement(wwaxUserCountIncrement);
		vo.setPaymentPendingUserCommitment(paymentPendingUserCommitment);
		vo.setPushFundExpirationTime(pushFundExpirationTime);
		vo.setEnableCacheResponse(enableCacheResponse);
		vo.setIsNotInstantFunding(isNotInstantFunding);
		vo.setIsForcedPostTransaction(isForcedPostTransaction);
		vo.setExternalFeeDetails(externalFeeDetails.build());
		vo.setExternalFeeDetailsArray(externalFeeDetailsArray.build());
		vo.setGenerateApprovalCode(generateApprovalCode);
		vo.setWalletPreferenceDetails(walletPreferenceDetails.buildList());
		vo.setProcessThroughShadowSystem(processThroughShadowSystem);
		vo.setIsjptestStandinForFundingSourceErrors(isjptestStandinForFundingSourceErrors);
		vo.setIsTransactionIcPlusEnabled(isTransactionIcPlusEnabled);
		vo.setExternalTxnType(externalTxnType);
		vo.setTxnAcquirerCountryCode(txnAcquirerCountryCode);
		vo.setIsZeroDollarAuth(isZeroDollarAuth);
		vo.setDisbursementInfo(disbursementInfo.build());
		vo.setDelayedDisbursementSchedule(delayedDisbursementSchedule.build());
		vo.setIsShortPayTransaction(isShortPayTransaction);
		vo.setMultilegPaymentsDataVo(multilegPaymentsDataVo.build());
		vo.setDelayedPostprocessing(delayedPostprocessing);
		return vo;
	}
}
