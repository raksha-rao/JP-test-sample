package com.jptest.payments.fulfillment.testonia.business.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.jptest.fee.AuditTrailVO;
import com.jptest.fee.FeeSummaryVO;
import com.jptest.payments.common.CommonPartyPb;
import com.jptest.payments.common.CommonPartyPb.UserIdentifier;
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
 * This service creates Disburse_Funds PaymentMessage request for a unilateral transaction with Phone No
 */
@Singleton
public class DisburseFundsUnilateralPhoneNoPaymentMessageCreatorService
        extends BaseDisburseFundsPaymentMessageCreatorService
        implements PaymentMessageCreatorService {

    @Inject
    private FFXHelper ffxHelper;

    public PaymentMessage buildPaymentMessage(final FulfillPaymentPlanOptions input, Context context) {
        final User sender = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
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
        final Builder debitInstruction = getDebitInstruction(input, context, sender, auditTrail,
                usdAmount.getAmount(), usdFeeAmount.getAmount());
        paymentInstructionBuilderList.add(debitInstruction);
        final Builder feeInstruction = getFeeInstruction(input, context, auditTrail, usdFeeAmount.getAmount());
        paymentInstructionBuilderList.add(feeInstruction);
        PaymentMessagePb.PaymentMessage.Builder paymentMessageBuilder = constructPaymentMessage(
                input.getTxnAmount().getCurrencyCode(), Long.parseLong(input.getTxnAmount().getAmount()),
                paymentInstructionBuilderList, input, sender);
        return paymentMessageBuilder.build();

    }

    public PaymentMessagePb.PaymentMessage.Builder constructPaymentMessage(final String currency,
            final long effectiveTransactionAmount, final List<PaymentInstruction.Builder> paymentInstructionBuilderList,
            final FulfillPaymentPlanOptions input, User sender) {

        final PaymentMessagePb.PaymentMessage.Builder paymentMessageBuilder = PaymentMessagePb.PaymentMessage
                .newBuilder();
        final String phoneNo = UserUtility.createPhoneNumber();
        final Receiver.Builder receiverBuilder = Receiver.newBuilder()
                .setUserIdentifier(UserIdentifier.newBuilder()
                        .setPhoneInfo(PhoneNumber.newBuilder().setNationalNumber(phoneNo).build()).build());

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

        final Transaction.Builder transaction = Transaction.newBuilder();

        transaction.setInternalIdempotencyData(idempotency)
                .setDisbursementMode(DisbursementMode.INSTANT).setReceiver(receiverBuilder.build())
                .setReceiverParty(CommonPartyPb.Party.newBuilder().setUserId(receiverBuilder.getUserIdentifier())
                        .build())
                .setTransactionAmount(legacyValueBuilder(currency, effectiveTransactionAmount))
                .setEffectiveTransactionAmount(legacyValueBuilder(currency, effectiveTransactionAmount))
                .setUsdEffectiveTransactionAmount(ffxHelper
                        .getUSDEquivalentValue(legacyValueBuilder(currency, effectiveTransactionAmount).build()))
                .setPurchaseContext(purchaseContext).setTransactionContext(transactionContext);
        transaction.setMemo(Memo.newBuilder().setShortDescription(input.getMemo()).setMessageId("1000").build());
        transaction
                .addDisbursementContingency(DisbursementContingency.newBuilder()
                        .setDisbursementContingencyType(DisbursementContingencyType.RECIPIENT_ACCEPT_DENY)
                        .setDisbursementContingencyReason(
                                DisbursementContingencyReason.RECIPIENT_NON_HOLDING_CURRENCY_PAYMENT_PREFERENCE)
                        .build());

        for (final PaymentInstruction.Builder paymentInstructionBuilder : paymentInstructionBuilderList) {
            transaction.addPaymentInstructions(paymentInstructionBuilder);
        }

        // Adding Unilateral contingencies
        transaction.addDisbursementContingency(DisbursementContingency.newBuilder()
                .setDisbursementContingencyReason(DisbursementContingencyReason.UNILATERAL)
                .setDisbursementContingencyType(DisbursementContingencyType.RECIPIENT_ENROLLMENT)
                .build());

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
