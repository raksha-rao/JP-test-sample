package com.jptest.payments.fulfillment.testonia.business.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.api.platform.wallet.FinancialInstrumentsSpecification.v2.Wallet;
import com.jptest.fee.AuditTrailVO;
import com.jptest.payments.common.CommonPartyPb;
import com.jptest.payments.fulfillment.testonia.bridge.FiManagementServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.funding_selection.FeeDirectionPb.FeeDirection;
import com.jptest.payments.funding_selection.FeePayerPb.FeePayer;
import com.jptest.payments.funding_selection.FundingSelectionPb.FeePayerDetail;
import com.jptest.payments.funding_selection.FundingSelectionPb.FeeSupplementaryData;
import com.jptest.payments.funding_selection.InstructionTargetPb.InstructionTarget;
import com.jptest.payments.funding_selection.LedgerDetailsPb.LedgerDetails;
import com.jptest.payments.funding_selection.LedgerPb.Ledger;
import com.jptest.payments.funding_selection.LedgerProcessingModePb.LedgerProcessingMode;
import com.jptest.payments.funding_selection.LedgerTypePb.LedgerType;
import com.jptest.payments.payment_message.FundingMethodPb.FundingMethod;
import com.jptest.payments.payment_message.MoneyMovementTypePb.MoneyMovementType;
import com.jptest.payments.payment_message.PaymentInstructionPb.PaymentInstruction;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;
import com.jptest.payments.payment_message.PaymentTypePb.PaymentType;
import com.jptest.payments.payments.PaymentsCommonPb;
import com.jptest.payments.payments.ValueTypePb;
import com.jptest.payments.product.BusinessApplicationPb;
import com.jptest.payments.product.PaymentServicesCategoryPb;
import com.jptest.payments.product.PaymentsAcceptanceSolutionCategoryPb;
import com.jptest.payments.product.PaymentsAcceptanceSolutionPb;
import com.jptest.payments.product.PaymentsFlowApprovalTypePb;
import com.jptest.payments.product.PaymentsFlowPb;
import com.jptest.payments.product.PaymentsSchedulePb;
import com.jptest.payments.product.ProductConfigurationPb;
import com.jptest.payments.product.SenderTypePb;
import com.jptest.payments.product.ServiceSubtypeCategoryPb;
import com.jptest.payments.product.TenantPb.Tenant;
import com.jptest.payments.proto.common.ValueConversionPb.ValueConversion;
import com.jptest.payments.wallet.FundingInstrumentPb;
import com.jptest.payments.wallet.FundingInstrumentPb.BackingInstrument;
import com.jptest.payments.wallet.enums.InstrumentTypePb.InstrumentType;
import com.jptest.pricing.utilities.FeeSerializer;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * This service will has the common functions for creating a DisburseFundsPaymentMessage Request
 */
@Singleton
public abstract class BaseDisburseFundsPaymentMessageCreatorService implements PaymentMessageCreatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDisburseFundsPaymentMessageCreatorService.class);

    protected static final String LEGAL_ENTITY_INC = "I";
    protected static final String LEGAL_COUNTRY_US = "US";
    protected static final String RECIPIENT_NON_HOLDING_CURRENCY = "RNHCP";
    protected static final String RECIPIENT_NO_PENDING_PAYMENTS = "RNPP";
    protected static final String YOUTH_ACCOUNT_RECIPIENT = "YOARE";

    @Inject
    private FFXHelper ffxHelper;

    @Inject
    private FiManagementServBridge fiManagementServBridge;

    protected BackingInstrument buildSVAccountInstrument(Wallet wallet) {

        Long availableBalance = (long) Double
                .parseDouble(wallet.getBalanceAccounts().get(0).getSubBalances().get(0).getAmount().getValue());

        FundingInstrumentPb.StoredValueAccount storedValueAccount = FundingInstrumentPb.StoredValueAccount.newBuilder()
                .setStoredValueId(wallet.getBalanceAccounts().get(0).getStoredValueId())
                .setBalanceAccountId(wallet.getBalanceAccounts().get(0).getId())
                .setUnencryptedBalanceAccountId(
                        wallet.getBalanceAccounts().get(0).getBalanceAuxiliaryData().getIdUnencrypted())
                .setCurrencyCode(wallet.getBalanceAccounts().get(0).getCurrencyCode()).setIsActive(true)
                .setAvailableBalance(
                        legacyValueBuilder(wallet.getBalanceAccounts().get(0).getCurrencyCode(), availableBalance))
                .build();

        FundingInstrumentPb.BackingInstrument fundingInstrument = FundingInstrumentPb.BackingInstrument.newBuilder()
                .setType(InstrumentType.STORED_VALUE).setStoredValue(storedValueAccount).build();

        return fundingInstrument;

    }

    protected PaymentInstruction.Builder getDebitInstruction(final FulfillPaymentPlanOptions input,
            final Context context, User sender, AuditTrailVO auditTrail, long usdAmount, long usdFeeAmount) {

        PaymentInstruction.Builder debit = PaymentInstruction.newBuilder()
                .setAmount(legacyValueBuilder(input.getTxnAmount().getCurrencyCode(),
                        (Long.parseLong(input.getTxnAmount().getAmount())
                                + auditTrail.getOriginalTxnFee().getAmount())))
                .setUsdAmount(legacyValueBuilder("USD", (usdAmount + usdFeeAmount)))
                .setMoneyMovementType(MoneyMovementType.FUNDING).setInstructionId("5000000")
                .setPaymentType(PaymentType.PAYMENT).setMemo(input.getMemo())
                .setFundingMethod(FundingMethod.valueOf(input.getFundingSource())).setTarget(InstructionTarget.LEDGER)
                .setLedgerDetails(LedgerDetails.newBuilder().setLedgerProcessingMode(LedgerProcessingMode.DELAYED)
                        .setLedger(Ledger.valueOf(input.getLedger()))
                        .setLedgerType(LedgerType.valueOf(input.getLedgerType())));
        debit.setInstrumentHolder(CommonPartyPb.Party.newBuilder()
                .setUserId(CommonPartyPb.UserIdentifier.newBuilder().setAccountNumber(sender.getAccountNumber())))
                .build();

        return debit;

    }

    protected PaymentInstruction.Builder getCreditInstruction(final FulfillPaymentPlanOptions input,
            final Context context, User sender, User receiver, long usdAmount, long usdFeeAmount) {

        PaymentInstruction.Builder credit = PaymentInstruction.newBuilder()
                .setAmount(ffxHelper.getEquivalentValue(input.getTxnAmount().getCurrencyCode(),
                        Long.parseLong(input.getTxnAmount().getAmount()), receiver.getCurrency()))
                /*
                 * .setAmount(legacyValueBuilder(input.getTxnAmount(). getCurrencyCode(),
                 * Long.parseLong(input.getTxnAmount().getAmount())))
                 */

                .setUsdAmount(legacyValueBuilder("USD", (usdAmount + usdFeeAmount)))
                .setMoneyMovementType(MoneyMovementType.DISBURSEMENT).setInstructionId("5000001")
				.setPaymentType(PaymentType.PAYMENT).setMemo(input.getMemo()).setFundingMethod(FundingMethod.HOLDING);
                if (receiver.getAccountNumber() != null) {
			credit.setBackingInstrument(
                        this.buildSVAccountInstrument(fiManagementServBridge.getWallet(receiver.getAccountNumber())));}
        // Check if the Seller has holding in the TxnCurrency
        // If not add Disbursement CurrencyConversion
        if (!input.getTxnAmount().getCurrencyCode().equals(receiver.getCurrency())) {
            PaymentsCommonPb.Value amountFromValue = legacyValueBuilder(input.getTxnAmount().getCurrencyCode(),
                    Long.parseLong(input.getTxnAmount().getAmount())).build();
            PaymentsCommonPb.Value amountToValue = ffxHelper.getEquivalentValue(amountFromValue,
                    receiver.getCurrency());

            credit.setValueConversion(ValueConversion.newBuilder().setAmountFrom(amountFromValue)
                    .setSourceUsdEquivalent(ffxHelper.getUSDEquivalentValue(amountFromValue)).setAmountTo(amountToValue)
                    .setAmountOut(amountToValue).setTargetUsdEquivalent(ffxHelper.getUSDEquivalentValue(amountToValue))
                    .setRateIdTo(1000).setRateIdTo(1001).setRateIdUsd(1002).setFeeWaived(false)
					/*.setFxSupplementaryData(FundingSelectionPb.FxSupplementaryData.newBuilder()
							.setCurrencyConversionInfoOutData(
									"V:1:GBP:525:USD:1000:1.90576051396348000000;C:0:M:USD;O:19383:0.5109408792:0.5113000000:0.5116067800;")
							.build())*/
                    .setExchangeRateQuoteAmount(0).build());
        }
        credit.setInstrumentHolder(CommonPartyPb.Party.newBuilder()
                .setUserId(CommonPartyPb.UserIdentifier.newBuilder().setAccountNumber(receiver.getAccountNumber())))
                .build();
        return credit;
    }

    protected PaymentInstruction.Builder getFeeInstruction(final FulfillPaymentPlanOptions input, final Context context,
            AuditTrailVO auditTrail, long usdFeeAmount) {
        String accountNumber = ((User) context.getData(ContextKeys.BUYER_VO_KEY.getName())).getAccountNumber();
        String feeAuditTrailStr = "";

        try {
            feeAuditTrailStr = FeeSerializer.serializeAuditTrail(auditTrail);
        }
        catch (Exception e) {
            LOGGER.error("Exception serializing audit trail. Ignoring the exception -> ", e);
        }

        final com.jptest.payments.funding_selection.FundingSelectionPb.Fee.Builder fee = com.jptest.payments.funding_selection.FundingSelectionPb.Fee
                .newBuilder()
                .setFeeAmount(legacyValueBuilder(input.getTxnAmount().getCurrencyCode(),
                        auditTrail.getOriginalTxnFee().getAmount()))
                .setFeeAmountUsdEquivalent(legacyValueBuilder("USD", usdFeeAmount))
                .setFeePayerDetail(FeePayerDetail.newBuilder().setAccountNumber(accountNumber)
                        .setFeePayer(FeePayer.SENDER).build())
                .setFeeSupplementaryData(FeeSupplementaryData.newBuilder().setFeeAuditTrailData(feeAuditTrailStr)
                        .setFeeDirection(FeeDirection.FEE_CHARGED)
                        .setFeeReasonCode(
                                com.jptest.payments.funding_selection.FeeReasonCodePb.FeeReasonCode.FEE_MASSPAY)
                        .build());

        final PaymentInstruction.Builder feeInstruction = PaymentInstruction.newBuilder()
                .setAmount(legacyValueBuilder(input.getTxnAmount().getCurrencyCode(),
                        auditTrail.getOriginalTxnFee().getAmount()))
                .setMoneyMovementType(MoneyMovementType.FEE).setInstructionId("5000003").setFee(fee)
                .setPaymentType(PaymentType.PAYMENT);
        return feeInstruction;
    }

    protected static ProductConfigurationPb.ProductConfiguration getMassPayPCE() {

        // Note for any * value we are passing UNKNOWN in enums
        ProductConfigurationPb.ProductConfiguration productConfiguration = ProductConfigurationPb.ProductConfiguration
                .newBuilder().setTenant(Tenant.jptest).setSenderType(SenderTypePb.SenderType.MEMBER)
                .setBusinessApplication(BusinessApplicationPb.BusinessApplication.FUND_DISBURSEMENT)
                .setPaymentServicesCategory(PaymentServicesCategoryPb.PaymentServicesCategory.CHARGE)
                .setPaymentsAcceptanceSolution(PaymentsAcceptanceSolutionPb.PaymentsAcceptanceSolution.PAYOUTS)
                .setPaymentsAcceptanceSolutionCategory(
                        PaymentsAcceptanceSolutionCategoryPb.PaymentsAcceptanceSolutionCategory.MASSPAY)
                .setServiceSubtypeCategory(ServiceSubtypeCategoryPb.ServiceSubtypeCategory.FINANCIAL_AUTHORIZATION)
                .setPaymentsSchedule(PaymentsSchedulePb.PaymentsSchedule.UNKNOWN)
                .setPaymentsFlow(PaymentsFlowPb.PaymentsFlow.UNKNOWN)
                .setPaymentServicesCategory(PaymentServicesCategoryPb.PaymentServicesCategory.CHARGE)
                .setPaymentsFlowApprovalType(PaymentsFlowApprovalTypePb.PaymentsFlowApprovalType.UNKNOWN)
                .setIssuanceProductName("MASSPAY_DISBURSE_FUNDS").build();

        return productConfiguration;

    }

    protected static PaymentsCommonPb.Value.Builder legacyValueBuilder(final String currency, final long amount) {
        final int exponent = 0;

        return PaymentsCommonPb.Value.newBuilder().setType(ValueTypePb.ValueType.CURRENCY).setCode(currency)
                .setCoefficient(amount).setExponent(exponent);
    }


    @Override
    public abstract PaymentMessage buildPaymentMessage(FulfillPaymentPlanOptions input, Context context);

}
