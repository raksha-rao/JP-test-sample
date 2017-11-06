package com.jptest.payments.fulfillment.testonia.business.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.testng.Assert;

import com.jptest.api.platform.wallet.FinancialInstrumentsSpecification.v2.Wallet;
import com.jptest.payments.common.CommonPartyPb;
import com.jptest.payments.common.CommonPartyPb.Party;
import com.jptest.payments.common.CommonPartyPb.PersonalDetails;
import com.jptest.payments.common.jptestAccountDetailsPb.jptestAccountDetails;
import com.jptest.payments.fulfillment.testonia.bridge.FiManagementServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.funding_selection.DisbursementTargetPb.DisbursementTarget;
import com.jptest.payments.funding_selection.InstructionTargetPb.InstructionTarget;
import com.jptest.payments.funding_selection.LedgerDetailsPb.LedgerDetails;
import com.jptest.payments.funding_selection.LedgerProcessingModePb.LedgerProcessingMode;
import com.jptest.payments.payment_message.DisbursementModePb.DisbursementMode;
import com.jptest.payments.payment_message.FrontingInstrumentPb;
import com.jptest.payments.payment_message.FundingMethodPb.FundingMethod;
import com.jptest.payments.payment_message.MoneyMovementTypePb.MoneyMovementType;
import com.jptest.payments.payment_message.PartyPb.FundingIdentifier;
import com.jptest.payments.payment_message.PaymentInstructionPb.PaymentInstruction;
import com.jptest.payments.payment_message.PaymentInstructionPb.PaymentInstruction.Builder;
import com.jptest.payments.payment_message.PaymentMessageContextPb.PaymentContext;
import com.jptest.payments.payment_message.PaymentMessagePb;
import com.jptest.payments.payment_message.PaymentMessagePb.AmqpHeader;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;
import com.jptest.payments.payment_message.PaymentMessagePb.Transfer;
import com.jptest.payments.payment_message.TransactionPb.InternalIdempotencyData;
import com.jptest.payments.payment_message.TransactionPb.Transaction;
import com.jptest.payments.payment_store.UserPb;
import com.jptest.payments.payment_store.UserPb.Account;
import com.jptest.payments.payments.BusinessDetailsPb;
import com.jptest.payments.payments.PaymentsCommonPb;
import com.jptest.payments.payments.StatusPb;
import com.jptest.payments.payments.ValueTypePb;
import com.jptest.payments.product.BusinessApplicationPb;
import com.jptest.payments.product.ChannelPartnerPb;
import com.jptest.payments.product.ConsumerAuthCredentialPb;
import com.jptest.payments.product.PaymentServicesCategoryPb;
import com.jptest.payments.product.PaymentsAcceptanceSolutionCategoryPb;
import com.jptest.payments.product.PaymentsAcceptanceSolutionPb;
import com.jptest.payments.product.PaymentsFlowApprovalTypePb;
import com.jptest.payments.product.PaymentsFlowChannelPb;
import com.jptest.payments.product.PaymentsFlowPb;
import com.jptest.payments.product.PaymentsSchedulePb;
import com.jptest.payments.product.ProductConfigurationPb;
import com.jptest.payments.product.SenderTypePb;
import com.jptest.payments.product.ServiceSubtypeCategoryPb;
import com.jptest.payments.product.TenantPb;
import com.jptest.payments.proto.common.LegalEntityPb.LegalEntity;
import com.jptest.payments.user.AccountStatusPb;
import com.jptest.payments.user.AccountTagsPb;
import com.jptest.payments.user.TypeOfAccountPb;
import com.jptest.payments.wallet.FundingInstrumentPb;
import com.jptest.payments.wallet.FundingInstrumentPb.LegacyBalanceConstructs;
import com.jptest.payments.wallet.enums.InstrumentTypePb.InstrumentType;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;

/**
 * This service will execute Release_Funds PaymentMessage request and will give
 * Release_Funds PaymentMessge response.
 */
@Singleton
public class ReleaseFundsPaymentMessageCreatorService implements PaymentMessageCreatorService {

	private static final String LEGAL_ENTITY_INC = "I";

	@Inject
	private FiManagementServBridge fiManagementServBridge;
	@Inject
	private FFXHelper ffxHelper;

	@Override
	public PaymentMessage buildPaymentMessage(final FulfillPaymentPlanOptions input, final Context context) {
		final User sender = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
		final Currency usdAmount = ffxHelper.getUSDEquivalent(input.getTxnAmount().getCurrencyCode(),
				Long.parseLong(input.getTxnAmount().getAmount()));
		final List<PaymentInstruction.Builder> paymentInstructionBuilderList = new ArrayList<>();

		final Builder debitInstruction = this.getDebitInstruction(input, usdAmount.getAmount());
		paymentInstructionBuilderList.add(debitInstruction);

		final Builder creditInstruction = this.getCreditInstruction(input, sender, usdAmount.getAmount());

		paymentInstructionBuilderList.add(creditInstruction);

		final PaymentMessagePb.PaymentMessage.Builder paymentMessageBuilder = constructPaymentMessage(
				input.getTxnAmount().getCurrencyCode(), Long.parseLong(input.getTxnAmount().getAmount()),
				paymentInstructionBuilderList, input, sender);

		return paymentMessageBuilder.build();

	}

	protected PaymentInstruction.Builder getDebitInstruction(final FulfillPaymentPlanOptions input, long usdAmount) {

		Assert.assertNotNull(input.getLedgerAmount());
		final long amount = Long.parseLong(input.getTxnAmount().getAmount());

		final com.jptest.payments.funding_selection.LedgerPb.Ledger ledger = com.jptest.payments.funding_selection.LedgerPb.Ledger.jptest_PAYABLE_GL;
		final com.jptest.payments.funding_selection.LedgerTypePb.LedgerType ledgerType = com.jptest.payments.funding_selection.LedgerTypePb.LedgerType.jptest_PAYABLE_GENERAL_LEDGER;

		final LedgerDetails ledgerDetails = LedgerDetails.newBuilder().setLedger(ledger).setLedgerType(ledgerType)

				.setLedgerProcessingMode(LedgerProcessingMode.DELAYED).build();
		final PaymentInstruction.Builder ledgerPaymentInstruction = PaymentInstruction.newBuilder()
				.setAmount(legacyValueBuilder(input.getLedgerAmount().getCurrencyCode(), amount, -2))
				.setUsdAmount(legacyValueBuilder(input.getLedgerAmount().getCurrencyCode(), usdAmount, -2))
				.setMoneyMovementType(MoneyMovementType.FUNDING)
				// .setDisbursementReason(DisbursementReasonType.PAYMENT_RECEIVED)

				.setTarget(InstructionTarget.LEDGER).setLedgerDetails(ledgerDetails)

				.setFundingMethod(FundingMethod.PAYABLE).setInstructionId("456");

		return ledgerPaymentInstruction;
	}

	protected PaymentInstruction.Builder getCreditInstruction(final FulfillPaymentPlanOptions input, User sender,
			long usdAmount) {

		Assert.assertNotNull(input.getTxnAmount());

		final long amount = Long.parseLong(input.getTxnAmount().getAmount());
		final PaymentInstruction.Builder debit = PaymentInstruction.newBuilder()
				.setAmount(legacyValueBuilder(input.getTxnAmount().getCurrencyCode(), amount, -2))
				.setUsdAmount(legacyValueBuilder(input.getTxnAmount().getCurrencyCode(), usdAmount, -2))
				.setMoneyMovementType(MoneyMovementType.DISBURSEMENT).setDisbursementTarget(DisbursementTarget.WALLET);

		final Wallet wallet = this.fiManagementServBridge.getWallet(sender.getAccountNumber());

		final Long availableBalance = (long) Double
				.parseDouble(wallet.getBalanceAccounts().get(0).getSubBalances().get(0).getAmount().getValue());

		final FundingInstrumentPb.StoredValueAccount sv_account = FundingInstrumentPb.StoredValueAccount.newBuilder()
				.setBalanceAccountId(wallet.getBalanceAccounts().get(0).getId()) // encrypted
				.setCurrencyCode(wallet.getBalanceAccounts().get(0).getCurrencyCode())
				.setStoredValueId(wallet.getBalanceAccounts().get(0).getStoredValueId()).setIsActive(true) // TODO
																											// -
																											// Remove
																											// once
																											// confirmed

				.setAvailableBalance(
						legacyValueBuilder(wallet.getBalanceAccounts().get(0).getCurrencyCode(), availableBalance, -2))
				.setUnencryptedBalanceAccountId(
						wallet.getBalanceAccounts().get(0).getBalanceAuxiliaryData().getIdUnencrypted())
				// TODO - Move this to Instrument Holder and remove the
				// legacyBalanceConstructs
				.setLegacyBalanceConstructs(LegacyBalanceConstructs.newBuilder().setLogNegativeBalance(true)
						.setAffectsAggregate(true).setLegalEntity(LEGAL_ENTITY_INC)
						.setAllowNegativeBalance(input.isAllowNegativeBalance()) // This
						// is
						// needed
						// else
						// INTERNAL_DECLINE_BUSINESS
						// for
						// negative
						// holding
						// in
						// PAS_CHARGE
						.setUserAccountNumber(sender.getAccountNumber()).build())
				.build();

		final FundingInstrumentPb.BackingInstrument fundingInstrument = FundingInstrumentPb.BackingInstrument
				.newBuilder().setType(InstrumentType.STORED_VALUE).setStoredValue(sv_account)
				.setBackingInstrumentId(sv_account.getStoredValueId()).build();
		debit.setBackingInstrument(fundingInstrument);
		debit.setInstructionId("123");
		return debit;
	}

	private static PaymentMessagePb.PaymentMessage.Builder constructPaymentMessage(final String currency,
			final long effectiveTransactionAmount, final List<PaymentInstruction.Builder> paymentInstructionBuilderList,
			final FulfillPaymentPlanOptions input, User sender) {

		final PaymentMessagePb.PaymentMessage.Builder paymentMessageBuilder = PaymentMessagePb.PaymentMessage
				.newBuilder();

		// Set the AmqpHeader
		final InternalIdempotencyData idempotency = InternalIdempotencyData.newBuilder()
				.setInternalIdempotencyId(input.getIdempotencyToken()).build();

		final AmqpHeader amqHeader = PaymentMessagePb.AmqpHeader.newBuilder()
				.setIdempotencyKey(idempotency.getInternalIdempotencyId()).build();

		// FLEX should not care about this
		final FrontingInstrumentPb.FrontingInstrument frontingInstrument = FrontingInstrumentPb.FrontingInstrument
				.newBuilder().setFundingIdentifier(FundingIdentifier.newBuilder().setFundingOptionId("1234")
						.setTransferOptionId("1385135413403092652").build())
				.build();

		PaymentContext paymentContext = null;
		paymentContext = PaymentContext.newBuilder().setProductConfiguration(getMassPayPCE())
				.setPaymentTrackingId("55555555555") // Batch id from MASSPAY
														// client
				.build();

		final Account senderAccount = UserPb.Account.newBuilder().setSubprime("0")
				.setPrimaryCurrency(UserPb.Account.getDefaultInstance().getPrimaryCurrency()).setFraudScore("100000")
				.setDisposition("OPEN").addTags(AccountTagsPb.AccountTags.IS_VERIFIED)
				// .setLegalEntity("INC")
				.setLegalCountry("US").setAccountStatus(AccountStatusPb.AccountStatus.OPEN)
				.setTypeOfAccount(TypeOfAccountPb.TypeOfAccount.PERSONAL).setAccountNumber(sender.getAccountNumber())
				.setIsRestricted(false).build();

		// final com.jptest.qi.rest.domain.pojo.User sender = input.getSender();
		final CommonPartyPb.Name senderName = CommonPartyPb.Name.newBuilder().setFirstName(sender.getFirstName())
				.setLastName(sender.getLastName()).build();

		final com.jptest.payments.payments.PaymentsCommonPb.Address.Builder senderAddress = PaymentsCommonPb.Address
				.newBuilder().setLine1(sender.getHomeAddress1()).setLine2(sender.getHomeAddress2())
				.setCity(sender.getHomeCity()).setStateProvince(sender.getHomeState())
				.setCountryCode(sender.getHomeCountry()).setPostalCode(sender.getHomeZip())
				.setCounty(sender.getCountry());

		final PersonalDetails personalDetails = CommonPartyPb.PersonalDetails.newBuilder().setName(senderName)
				.setAddress(senderAddress).build();

		final Party transferParty = CommonPartyPb.Party.newBuilder()
				.setjptestAccountDetails(jptestAccountDetails.newBuilder().setAccountDetails(senderAccount).build())
				.setPersonalDetails(personalDetails)
				.setBusinessDetails(
						BusinessDetailsPb.BusinessDetails.newBuilder().setLegalEntity(LegalEntity.INC).build())
				.setUserId(CommonPartyPb.UserIdentifier.newBuilder().setAccountNumber(sender.getAccountNumber())
						.setEmailAddress("abc@jptest.com").build())
				.build();

		final Transaction.Builder transaction = Transaction.newBuilder().setInternalIdempotencyData(idempotency)
				.setDisbursementMode(DisbursementMode.INSTANT)
				.setTransactionAmount(legacyValueBuilder(currency, effectiveTransactionAmount, -2))
				.setEffectiveTransactionAmount(legacyValueBuilder(currency, effectiveTransactionAmount, -2))
				.setStatus(StatusPb.Status.UNPROCCESSED).setTransferParty(transferParty);
		// .setTransactionContext(transactionContext)

		for (final PaymentInstruction.Builder paymentInstructionBuilder : paymentInstructionBuilderList) {
			transaction.addPaymentInstructions(paymentInstructionBuilder);
		}
		paymentMessageBuilder
				.setTransfer(Transfer.newBuilder().setFrontingInstrument(frontingInstrument)
						.setPaymentContext(paymentContext).addTransactions(transaction))
				.setStatus(StatusPb.Status.UNPROCCESSED).setAmqpHeader(amqHeader).build();

		return paymentMessageBuilder;

	}

	private static ProductConfigurationPb.ProductConfiguration getMassPayPCE() {
		final ProductConfigurationPb.ProductConfiguration productConfiguration = ProductConfigurationPb.ProductConfiguration
				.newBuilder().setTenant(TenantPb.Tenant.jptest)
				.setChannelPartner(ChannelPartnerPb.ChannelPartner.UNKNOWN)
				.setBusinessApplication(BusinessApplicationPb.BusinessApplication.FUND_TRANSFERS)
				.setPaymentServicesCategory(PaymentServicesCategoryPb.PaymentServicesCategory.RELEASE_FUNDS)
				.setPaymentsAcceptanceSolution(PaymentsAcceptanceSolutionPb.PaymentsAcceptanceSolution.PAYOUTS)
				.setPaymentsAcceptanceSolutionCategory(
						PaymentsAcceptanceSolutionCategoryPb.PaymentsAcceptanceSolutionCategory.MASSPAY)
				.setSenderCountry("*").setReceiverCountry("*")
				.setServiceSubtypeCategory(ServiceSubtypeCategoryPb.ServiceSubtypeCategory.UNKNOWN)
				.setSenderType(SenderTypePb.SenderType.MEMBER)
				.setPaymentsFlowChannel(PaymentsFlowChannelPb.PaymentsFlowChannel.UNKNOWN)
				.setPaymentsSchedule(PaymentsSchedulePb.PaymentsSchedule.ONE_TIME_PAYMENT)
				.setPaymentsFlow(PaymentsFlowPb.PaymentsFlow.UNKNOWN)
				.setPaymentsFlowApprovalType(PaymentsFlowApprovalTypePb.PaymentsFlowApprovalType.UNKNOWN)
				.setConsumerAuthCredential(ConsumerAuthCredentialPb.ConsumerAuthCredential.UNKNOWN).build();

		return productConfiguration;
	}

	protected static PaymentsCommonPb.Value.Builder legacyValueBuilder(final String currency, final long amount,
			final int exponent) {

		return PaymentsCommonPb.Value.newBuilder().setType(ValueTypePb.ValueType.CURRENCY).setCode(currency)
				.setCoefficient(amount).setExponent(exponent);
	}

}
