package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import java.util.List;

import com.jptest.financialinstrument.BuyerCreditTransContextVO;
import com.jptest.fundingmix.FundingAmountInfoVO;
import com.jptest.fundingmix.FundingPlanChargeVO;
import com.jptest.money.BillingDescriptorsVO;
import com.jptest.money.CCPreAuthVO;
import com.jptest.money.CCProcessorInfoVO;
import com.jptest.money.DelayedFundingDetailsVO;
import com.jptest.money.PaymentFundingSelectionVO;
import com.jptest.money.PaymentPlanIdentifier;
import com.jptest.paymentmethodchooser.FundingBalanceDivisionVO;
import com.jptest.paymentmethodchooser.PaymentBalanceDivisionVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.types.Currency;

public class PaymentFundingSelectionVOBuilder implements
		VOBuilder<PaymentFundingSelectionVO> {

	private BigInteger fundingHoldingId = BigInteger.ZERO;
	private BigInteger ccFundingCcId = BigInteger.ZERO;
	private BigInteger instantAchAchId = BigInteger.ZERO;
	private BigInteger echeckAchId = BigInteger.ZERO;
	private BigInteger chineseBankAchId = BigInteger.ZERO;
	private BigInteger manualEftAchId = BigInteger.ZERO;
	private BigInteger buyerCreditId = BigInteger.ZERO;
	private BigInteger postpaidTabId = BigInteger.ZERO;
	private BigInteger genericInstrumentId;
	private Byte fundingSourceType;
	private Boolean isBuyerCredit;
	private Boolean isBuyerCreditOnly;
	private Boolean isTransactionBuyerCredit;
	private Boolean isTransactionBuyerCreditOnly;
	private BigInteger backupFundingSourceId;
	private Byte backupFundingSourceType;
	private Boolean isPinlessEligible;
	private Byte networkPreference;
	private String transactionCvv2;
	private Currency incentiveAmount;
	private String buyerCreditPromoCode;
	private String merchantSoftDescriptorSuffix;
	private BigInteger shippingAddressId;
	private VOBuilder<BuyerCreditTransContextVO> bcTransContext = new NullVOBuilder<>();
	private VOBuilder<CCPreAuthVO> ccPreauthParams = new NullVOBuilder<>();
	private VOBuilder<CCProcessorInfoVO> ccProcessorInfo = new NullVOBuilder<>();
	private Boolean isIncentive = Boolean.FALSE;
	private Boolean overrideDeclineIfCcDisabled = Boolean.FALSE;
	private Boolean isChinaUnionpayFunding = Boolean.FALSE;
	private Boolean useDelayedFunding = Boolean.FALSE;
	private VOBuilder<DelayedFundingDetailsVO> delayedFundingDetails = new NullVOBuilder<>();
	private BigInteger numberOfInstallments;
	private VOBuilder<BillingDescriptorsVO> cardBillingDescriptors = new NullVOBuilder<>();
	private VOBuilder<PaymentBalanceDivisionVO> fundingDetailsInTxnCurrency = new NullVOBuilder<>();
	private VOBuilder<FundingBalanceDivisionVO> fundingDetailsInOtherCurrency = new NullVOBuilder<>();
	private ListVOBuilder<FundingPlanChargeVO> fundingPlanCharges = new NullVOBuilder<>();
	private List<PaymentPlanIdentifier> paymentPlanIdentifierAsEnum;
	private VOBuilder<FundingAmountInfoVO> fundingAmountInfo = new NullVOBuilder<>();

	public static PaymentFundingSelectionVOBuilder newBuilder() {
		return new PaymentFundingSelectionVOBuilder();
	}

	public PaymentFundingSelectionVOBuilder fundingHoldingId(
			BigInteger fundingHoldingId) {
		this.fundingHoldingId = fundingHoldingId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder ccFundingCcId(
			BigInteger ccFundingCcId) {
		this.ccFundingCcId = ccFundingCcId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder instantAchAchId(
			BigInteger instantAchAchId) {
		this.instantAchAchId = instantAchAchId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder echeckAchId(BigInteger echeckAchId) {
		this.echeckAchId = echeckAchId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder chineseBankAchId(
			BigInteger chineseBankAchId) {
		this.chineseBankAchId = chineseBankAchId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder manualEftAchId(
			BigInteger manualEftAchId) {
		this.manualEftAchId = manualEftAchId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder buyerCreditId(
			BigInteger buyerCreditId) {
		this.buyerCreditId = buyerCreditId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder postpaidTabId(
			BigInteger postpaidTabId) {
		this.postpaidTabId = postpaidTabId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder genericInstrumentId(
			BigInteger genericInstrumentId) {
		this.genericInstrumentId = genericInstrumentId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder fundingSourceType(
			Byte fundingSourceType) {
		this.fundingSourceType = fundingSourceType;
		return this;

	}

	public PaymentFundingSelectionVOBuilder isBuyerCredit(Boolean isBuyerCredit) {
		this.isBuyerCredit = isBuyerCredit;
		return this;

	}

	public PaymentFundingSelectionVOBuilder isBuyerCreditOnly(
			Boolean isBuyerCreditOnly) {
		this.isBuyerCreditOnly = isBuyerCreditOnly;
		return this;

	}

	public PaymentFundingSelectionVOBuilder isTransactionBuyerCredit(
			Boolean isTransactionBuyerCredit) {
		this.isTransactionBuyerCredit = isTransactionBuyerCredit;
		return this;

	}

	public PaymentFundingSelectionVOBuilder isTransactionBuyerCreditOnly(
			Boolean isTransactionBuyerCreditOnly) {
		this.isTransactionBuyerCreditOnly = isTransactionBuyerCreditOnly;
		return this;

	}

	public PaymentFundingSelectionVOBuilder backupFundingSourceId(
			BigInteger backupFundingSourceId) {
		this.backupFundingSourceId = backupFundingSourceId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder backupFundingSourceType(
			Byte backupFundingSourceType) {
		this.backupFundingSourceType = backupFundingSourceType;
		return this;

	}

	public PaymentFundingSelectionVOBuilder isPinlessEligible(
			Boolean isPinlessEligible) {
		this.isPinlessEligible = isPinlessEligible;
		return this;

	}

	public PaymentFundingSelectionVOBuilder networkPreference(
			Byte networkPreference) {
		this.networkPreference = networkPreference;
		return this;

	}

	public PaymentFundingSelectionVOBuilder transactionCvv2(
			String transactionCvv2) {
		this.transactionCvv2 = transactionCvv2;
		return this;

	}

	public PaymentFundingSelectionVOBuilder incentiveAmount(
			Currency incentiveAmount) {
		this.incentiveAmount = incentiveAmount;
		return this;

	}

	public PaymentFundingSelectionVOBuilder buyerCreditPromoCode(
			String buyerCreditPromoCode) {
		this.buyerCreditPromoCode = buyerCreditPromoCode;
		return this;

	}

	public PaymentFundingSelectionVOBuilder merchantSoftDescriptorSuffix(
			String merchantSoftDescriptorSuffix) {
		this.merchantSoftDescriptorSuffix = merchantSoftDescriptorSuffix;
		return this;

	}

	public PaymentFundingSelectionVOBuilder shippingAddressId(
			BigInteger shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
		return this;

	}

	public PaymentFundingSelectionVOBuilder bcTransContext(
			VOBuilder<BuyerCreditTransContextVO> bcTransContext) {
		this.bcTransContext = bcTransContext;
		return this;

	}

	public PaymentFundingSelectionVOBuilder ccPreauthParams(
			VOBuilder<CCPreAuthVO> ccPreauthParams) {
		this.ccPreauthParams = ccPreauthParams;
		return this;

	}

	public PaymentFundingSelectionVOBuilder ccProcessorInfo(
			VOBuilder<CCProcessorInfoVO> ccProcessorInfo) {
		this.ccProcessorInfo = ccProcessorInfo;
		return this;

	}

	public PaymentFundingSelectionVOBuilder isIncentive(Boolean isIncentive) {
		this.isIncentive = isIncentive;
		return this;

	}

	public PaymentFundingSelectionVOBuilder overrideDeclineIfCcDisabled(
			Boolean overrideDeclineIfCcDisabled) {
		this.overrideDeclineIfCcDisabled = overrideDeclineIfCcDisabled;
		return this;

	}

	public PaymentFundingSelectionVOBuilder isChinaUnionpayFunding(
			Boolean isChinaUnionpayFunding) {
		this.isChinaUnionpayFunding = isChinaUnionpayFunding;
		return this;

	}

	public PaymentFundingSelectionVOBuilder useDelayedFunding(
			Boolean useDelayedFunding) {
		this.useDelayedFunding = useDelayedFunding;
		return this;

	}

	public PaymentFundingSelectionVOBuilder delayedFundingDetails(
			VOBuilder<DelayedFundingDetailsVO> delayedFundingDetails) {
		this.delayedFundingDetails = delayedFundingDetails;
		return this;

	}

	public PaymentFundingSelectionVOBuilder numberOfInstallments(
			BigInteger numberOfInstallments) {
		this.numberOfInstallments = numberOfInstallments;
		return this;

	}

	public PaymentFundingSelectionVOBuilder cardBillingDescriptors(
			VOBuilder<BillingDescriptorsVO> cardBillingDescriptors) {
		this.cardBillingDescriptors = cardBillingDescriptors;
		return this;

	}

	public PaymentFundingSelectionVOBuilder fundingDetailsInTxnCurrency(
			VOBuilder<PaymentBalanceDivisionVO> fundingDetailsInTxnCurrency) {
		this.fundingDetailsInTxnCurrency = fundingDetailsInTxnCurrency;
		return this;

	}

	public PaymentFundingSelectionVOBuilder fundingDetailsInOtherCurrency(
			VOBuilder<FundingBalanceDivisionVO> fundingDetailsInOtherCurrency) {
		this.fundingDetailsInOtherCurrency = fundingDetailsInOtherCurrency;
		return this;

	}

	public PaymentFundingSelectionVOBuilder fundingPlanCharges(
			ListVOBuilder<FundingPlanChargeVO> fundingPlanCharges) {
		this.fundingPlanCharges = fundingPlanCharges;
		return this;

	}

	public PaymentFundingSelectionVOBuilder paymentPlanIdentifierAsEnum(
			List<PaymentPlanIdentifier> paymentPlanIdentifierAsEnum) {
		this.paymentPlanIdentifierAsEnum = paymentPlanIdentifierAsEnum;
		return this;

	}

	public PaymentFundingSelectionVOBuilder fundingAmountInfo(
			VOBuilder<FundingAmountInfoVO> fundingAmountInfo) {
		this.fundingAmountInfo = fundingAmountInfo;
		return this;

	}

	@Override
	public PaymentFundingSelectionVO build() {
		PaymentFundingSelectionVO vo = new PaymentFundingSelectionVO();
		vo.setFundingHoldingId(fundingHoldingId);
		vo.setCcFundingCcId(ccFundingCcId);
		vo.setInstantAchAchId(instantAchAchId);
		vo.setEcheckAchId(echeckAchId);
		vo.setChineseBankAchId(chineseBankAchId);
		vo.setManualEftAchId(manualEftAchId);
		vo.setBuyerCreditId(buyerCreditId);
		vo.setPostpaidTabId(postpaidTabId);
		vo.setGenericInstrumentId(genericInstrumentId);
		vo.setFundingSourceType(fundingSourceType);
		vo.setIsBuyerCredit(isBuyerCredit);
		vo.setIsBuyerCreditOnly(isBuyerCreditOnly);
		vo.setIsTransactionBuyerCredit(isTransactionBuyerCredit);
		vo.setIsTransactionBuyerCreditOnly(isTransactionBuyerCreditOnly);
		vo.setBackupFundingSourceId(backupFundingSourceId);
		vo.setBackupFundingSourceType(backupFundingSourceType);
		vo.setIsPinlessEligible(isPinlessEligible);
		vo.setNetworkPreference(networkPreference);
		vo.setTransactionCvv2(transactionCvv2);
		vo.setIncentiveAmount(incentiveAmount);
		vo.setBuyerCreditPromoCode(buyerCreditPromoCode);
		vo.setMerchantSoftDescriptorSuffix(merchantSoftDescriptorSuffix);
		vo.setShippingAddressId(shippingAddressId);
		vo.setBcTransContext(bcTransContext.build());
		vo.setCcPreauthParams(ccPreauthParams.build());
		vo.setCcProcessorInfo(ccProcessorInfo.build());
		vo.setIsIncentive(isIncentive);
		vo.setOverrideDeclineIfCcDisabled(overrideDeclineIfCcDisabled);
		vo.setIsChinaUnionpayFunding(isChinaUnionpayFunding);
		vo.setUseDelayedFunding(useDelayedFunding);
		vo.setDelayedFundingDetails(delayedFundingDetails.build());
		vo.setNumberOfInstallments(numberOfInstallments);
		vo.setCardBillingDescriptors(cardBillingDescriptors.build());
		vo.setFundingDetailsInTxnCurrency(fundingDetailsInTxnCurrency.build());
		vo.setFundingDetailsInOtherCurrency(fundingDetailsInOtherCurrency
				.build());
		vo.setFundingPlanCharges(fundingPlanCharges.buildList());
		vo.setPaymentPlanIdentifierAsEnum(paymentPlanIdentifierAsEnum);
		vo.setFundingAmountInfo(fundingAmountInfo.build());
		return vo;
	}
}
