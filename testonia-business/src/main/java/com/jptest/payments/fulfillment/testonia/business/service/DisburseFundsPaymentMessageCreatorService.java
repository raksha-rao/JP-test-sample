package com.jptest.payments.fulfillment.testonia.business.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.jptest.fee.AuditTrailVO;
import com.jptest.fee.FeeSummaryVO;
import com.jptest.payments.common.CommonPartyPb;
import com.jptest.payments.common.CommonPb;
import com.jptest.payments.common.jptestAccountDetailsPb.jptestAccountDetails;
import com.jptest.payments.fulfillment.testonia.business.util.UserUtility;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.funding_selection.DisbursementContingencyReasonPb.DisbursementContingencyReason;
import com.jptest.payments.funding_selection.DisbursementContingencyTypePb.DisbursementContingencyType;
import com.jptest.payments.payment_message.DisbursementModePb.DisbursementMode;
import com.jptest.payments.payment_message.FrontingInstrumentPb.FrontingInstrument;
import com.jptest.payments.payment_message.MemoPb.Memo;
import com.jptest.payments.payment_message.MoneyMovementTypePb.MoneyMovementType;
import com.jptest.payments.payment_message.PartyPb.Receiver;
import com.jptest.payments.payment_message.PartyPb.Sender;
import com.jptest.payments.payment_message.PaymentInstructionPb.PaymentInstruction;
import com.jptest.payments.payment_message.PaymentInstructionPb.PaymentInstruction.Builder;
import com.jptest.payments.payment_message.PaymentMessageContextPb.PaymentContext;
import com.jptest.payments.payment_message.PaymentMessageContextPb.PurchaseContext;
import com.jptest.payments.payment_message.PaymentMessagePb;
import com.jptest.payments.payment_message.PaymentMessagePb.FinancialAuthorization;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;
import com.jptest.payments.payment_message.PaymentProcessingModePb;
import com.jptest.payments.payment_message.TransactionPb.DisbursementContingency;
import com.jptest.payments.payment_message.TransactionPb.InternalIdempotencyData;
import com.jptest.payments.payment_message.TransactionPb.Transaction;
import com.jptest.payments.payment_message.TransactionPb.TransactionContext;
import com.jptest.payments.payment_store.UserPb;
import com.jptest.payments.payment_store.UserPb.Account;
import com.jptest.payments.payments.PaymentsCommonPb.PhoneNumber;
import com.jptest.payments.user.AccountStatusPb;
import com.jptest.payments.user.AccountTagsPb;
import com.jptest.payments.user.TypeOfAccountPb;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;

/**
 * This service will execute Disburse_Funds PaymentMessage request and will give
 * Disburse_Funds PaymentMessge response.
 */
@Singleton
public class DisburseFundsPaymentMessageCreatorService extends BaseDisburseFundsPaymentMessageCreatorService
        implements PaymentMessageCreatorService {

	@Inject
	private FFXHelper ffxHelper;

	public PaymentMessage buildPaymentMessage(final FulfillPaymentPlanOptions input, Context context) {

		final User sender = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
		final User receiver = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
		final Currency usdAmount = ffxHelper.getUSDEquivalent(input.getTxnAmount().getCurrencyCode(),
				Long.parseLong(input.getTxnAmount().getAmount()));
		AuditTrailVO auditTrail = new AuditTrailVO();
		auditTrail.setOriginalTxnAmount(
				new Currency(input.getTxnAmount().getCurrencyCode(), Long.parseLong(input.getTxnAmount().getAmount())));
		auditTrail.setOriginalTxnFee(new Currency(input.getTxnAmount().getCurrencyCode(), 100));
		auditTrail.setFeeItems(Arrays.asList(new FeeSummaryVO()));
		auditTrail.getFeeItems().get(0).setActualFixedFee(auditTrail.getOriginalTxnFee());
		auditTrail.getFeeItems().get(0).setBaseFeeId(new BigInteger("1001"));
		auditTrail.getFeeItems().get(0).setTotalFee(auditTrail.getOriginalTxnFee());
		auditTrail.getFeeItems().get(0).setActualPercentFee(0.0);
		final Currency usdFeeAmount = ffxHelper.getUSDEquivalent(input.getTxnAmount().getCurrencyCode(),
				auditTrail.getOriginalTxnFee().getAmount());
		final List<PaymentInstruction.Builder> paymentInstructionBuilderList = new ArrayList<>();
		final Builder debitInstruction = this.getDebitInstruction(input, context, sender, auditTrail,
				usdAmount.getAmount(), usdFeeAmount.getAmount());
		paymentInstructionBuilderList.add(debitInstruction);
		final Builder creditInstruction = this.getCreditInstruction(input, context, sender, receiver,
				usdAmount.getAmount(), usdFeeAmount.getAmount());
		paymentInstructionBuilderList.add(creditInstruction);
		final Builder feeInstruction = this.getFeeInstruction(input, context, auditTrail, usdFeeAmount.getAmount());
		paymentInstructionBuilderList.add(feeInstruction);

		PaymentMessagePb.PaymentMessage.Builder paymentMessageBuilder = constructPaymentMessage(
				input.getTxnAmount().getCurrencyCode(), Long.parseLong(input.getTxnAmount().getAmount()),
				paymentInstructionBuilderList, input, sender, receiver);
		return paymentMessageBuilder.build();

	}

	public PaymentMessagePb.PaymentMessage.Builder constructPaymentMessage(final String currency,
			final long effectiveTransactionAmount, final List<PaymentInstruction.Builder> paymentInstructionBuilderList,
			final FulfillPaymentPlanOptions input, User sender, User receiver) {

		final PaymentMessagePb.PaymentMessage.Builder paymentMessageBuilder = PaymentMessagePb.PaymentMessage
				.newBuilder();
		final Receiver.Builder receiverBuilder = Receiver.newBuilder();
		if (receiver != null && receiver.getAccountNumber() != null && !receiver.isUnConfirmedPhone()) {
			// final Receiver receiver = Receiver.newBuilder()
			receiverBuilder
					.setUserIdentifier(
							CommonPartyPb.UserIdentifier.newBuilder().setAccountNumber(receiver.getAccountNumber())
									.setEmailAddress(receiver.getEmailAddress()).build())
					.setName(receiver.getFirstName()).build();
		} else if (receiver != null && receiver.getEmailAddress() != null && !receiver.isUnConfirmedPhone()) {
			receiverBuilder.setUserIdentifier(
					CommonPartyPb.UserIdentifier.newBuilder().setEmailAddress(receiver.getEmailAddress()).build());
		} else if (receiver != null && receiver.isUnConfirmedPhone()) {
			String phoneNo = UserUtility.createPhoneNumber();
			receiverBuilder.setUserIdentifier(
					CommonPartyPb.UserIdentifier.newBuilder()
							.setPhoneInfo(PhoneNumber.newBuilder().setNationalNumber(phoneNo).build()));
		}

		final Sender senderBuilder = Sender
				.newBuilder().setUserIdentifier(CommonPartyPb.UserIdentifier.newBuilder()
						.setEmailAddress(sender.getEmailAddress()).setAccountNumber(sender.getAccountNumber()).build())
				.build();
		final InternalIdempotencyData idempotency = InternalIdempotencyData.newBuilder()
				.setInternalIdempotencyId(input.getIdempotencyToken()).build();
		final TransactionContext transactionContext = TransactionContext.newBuilder()
				.setContextIdentifier("PAY-ID-LINK") // This will be used to
				// link Subsequent
				// Payment Deposit
				.setCreationTime(CommonPb.DateTime.newBuilder().setTimeInMillis(System.currentTimeMillis()))
				.setExpirationTime(CommonPb.DateTime.newBuilder().setTimeInMillis(System.currentTimeMillis() + 1000))
				.setRequestDescription(" This is for FulfillPayment").build();
		final PaymentContext paymentContext = PaymentContext.newBuilder().setProductConfiguration(getMassPayPCE())
				.setProcessingMode(PaymentProcessingModePb.PaymentProcessingMode.MANUAL).build();
		final PurchaseContext purchaseContext = PurchaseContext.newBuilder().build();
		final jptestAccountDetails.Builder receiverAccountDetails = jptestAccountDetails.newBuilder();
		Transaction.Builder transaction = Transaction.newBuilder();

		if (receiver != null && receiver.getAccountNumber() != null) {
			receiverAccountDetails.setAccountDetails(Account.newBuilder().setAccountNumber(receiver.getAccountNumber())
					.setSubprime("0").setPrimaryCurrency(UserPb.Account.getDefaultInstance().getPrimaryCurrency())
					.setFraudScore("100000").setDisposition("OPEN").addTags(AccountTagsPb.AccountTags.IS_VERIFIED)
					.setLegalEntity(LEGAL_ENTITY_INC).setLegalCountry(LEGAL_COUNTRY_US)
					.setAccountStatus(AccountStatusPb.AccountStatus.OPEN)
					.setTypeOfAccount(TypeOfAccountPb.TypeOfAccount.PERSONAL)
					.addUserFlags(UserPb.UserFlags.newBuilder().setFlagGroupId(0).setFlagGroupValue(0))
					.setIsRestricted(false).build()).build();

			transaction.setInternalIdempotencyData(idempotency)
					.setReceiverAccount(Account.newBuilder().setAccountNumber(receiver.getAccountNumber()))
					.setDisbursementMode(DisbursementMode.INSTANT).setReceiver(receiverBuilder.build())
					.setReceiverParty(CommonPartyPb.Party.newBuilder().setUserId(receiverBuilder.getUserIdentifier())
							.setjptestAccountDetails(receiverAccountDetails).build())
					.setReceiverAccount(receiverAccountDetails.getAccountDetails())
					.setTransactionAmount(legacyValueBuilder(currency, effectiveTransactionAmount))
					.setEffectiveTransactionAmount(legacyValueBuilder(currency, effectiveTransactionAmount))
					.setUsdEffectiveTransactionAmount(ffxHelper
							.getUSDEquivalentValue(legacyValueBuilder(currency, effectiveTransactionAmount).build()))
					.setPurchaseContext(purchaseContext).setTransactionContext(transactionContext);
			transaction.setMemo(Memo.newBuilder().setShortDescription(input.getMemo()).setMessageId("1000").build());
			if (Objects.nonNull(input.getContingency())
					&& input.getContingency().equals(RECIPIENT_NON_HOLDING_CURRENCY)) {
				transaction.addDisbursementContingency(DisbursementContingency.newBuilder()
						.setDisbursementContingencyType(DisbursementContingencyType.RECIPIENT_ACCEPT_DENY)
						.setDisbursementContingencyReason(
								DisbursementContingencyReason.RECIPIENT_NON_HOLDING_CURRENCY_PAYMENT_PREFERENCE)
						.build());
			} else if (Objects.nonNull(input.getContingency())
					&& input.getContingency().equals(RECIPIENT_NO_PENDING_PAYMENTS)) {
				transaction.addDisbursementContingency(DisbursementContingency.newBuilder()
						.setDisbursementContingencyType(DisbursementContingencyType.RECIPIENT_ACCEPT_DENY)
						.setDisbursementContingencyReason(DisbursementContingencyReason.RECIPIENT_NO_PENDING_PAYMENTS)
						.build());
			} else if (Objects.nonNull(input.getContingency())
                    && input.getContingency().equals(YOUTH_ACCOUNT_RECIPIENT)) {
                transaction.addDisbursementContingency(DisbursementContingency.newBuilder()
                        .setDisbursementContingencyType(DisbursementContingencyType.PARENT_ACCEPT_DENY)
                        .setDisbursementContingencyReason(DisbursementContingencyReason.YOUTH_ACCOUNT_RECIPIENT)
                        .build());
            }

			for (final PaymentInstruction.Builder paymentInstructionBuilder : paymentInstructionBuilderList) {
				// Skipping credit PI for unilateral transactions
				if (!receiver.isConfirmEmail()
						&& MoneyMovementType.DISBURSEMENT.equals(paymentInstructionBuilder.getMoneyMovementType())) {
					continue;
				} else if (receiver.isUnConfirmedPhone()
						&& MoneyMovementType.DISBURSEMENT.equals(paymentInstructionBuilder.getMoneyMovementType())) {
					continue;
				}
				transaction.addPaymentInstructions(paymentInstructionBuilder);
			}
		} else {
			transaction.setReceiverParty(
					CommonPartyPb.Party.newBuilder().setUserId(receiverBuilder.getUserIdentifier()).build());
		}
		// Adding contingencies for unilateral transaction
		if (receiver != null && !receiver.isConfirmEmail() || receiver != null && receiver.isUnConfirmedPhone()) {
			transaction.addDisbursementContingency(DisbursementContingency.newBuilder()
					.setDisbursementContingencyReason(DisbursementContingencyReason.UNILATERAL)
					.setDisbursementContingencyType(DisbursementContingencyType.RECIPIENT_CONFIRM_ALIAS).build());
		}
		paymentMessageBuilder.setFinancialAuthorization(FinancialAuthorization.newBuilder()
				.setSenderParty(CommonPartyPb.Party.newBuilder().setUserId(senderBuilder.getUserIdentifier())
						.setjptestAccountDetails(jptestAccountDetails.newBuilder().setAccountDetails(Account
								.newBuilder().setAccountNumber(sender.getAccountNumber()).setSubprime("0")
								.setPrimaryCurrency(UserPb.Account.getDefaultInstance().getPrimaryCurrency())
								.setFraudScore("100000").setDisposition("OPEN")
								.addTags(AccountTagsPb.AccountTags.IS_VERIFIED).setLegalEntity(LEGAL_ENTITY_INC)
								.setLegalCountry(LEGAL_COUNTRY_US).setAccountStatus(AccountStatusPb.AccountStatus.OPEN)
								.setTypeOfAccount(TypeOfAccountPb.TypeOfAccount.PERSONAL)
								.setAccountNumber(sender.getAccountNumber()).setIsRestricted(false).build()))
						.build())
				.setFrontingInstrument(
						FrontingInstrument.newBuilder().setUserIdentifier(senderBuilder.getUserIdentifier()).build())
				.setPaymentContext(paymentContext).addTransactions(transaction)).build();

		return paymentMessageBuilder;
	}
}
