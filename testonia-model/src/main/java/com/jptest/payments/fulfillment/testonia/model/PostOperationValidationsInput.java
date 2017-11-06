package com.jptest.payments.fulfillment.testonia.model;

public class PostOperationValidationsInput {

    private GoldenFileComparisonTaskInput ipnValidation;
    private GoldenFileComparisonTaskInput financialJournalValidation;
    private GoldenFileComparisonTaskInput payloadValidation;
    private GoldenFileComparisonTaskInput paymentSenderValidation;
    private GoldenFileComparisonTaskInput paymentRecipientValidation;
    private GoldenFileComparisonTaskInput topUpDataValidation;
    private GoldenFileComparisonTaskInput prsDataValidation;
    private GoldenFileComparisonTaskInput activityLogValidation;
    private GoldenFileComparisonTaskInput paymentRecipientSubBalanceValidation;
    private GoldenFileComparisonTaskInput settlementRequestValidation;
    private GoldenFileComparisonTaskInput postPaymentResponseValidation;
    private GoldenFileComparisonTaskInput paymentSideValidation;


    public GoldenFileComparisonTaskInput getPaymentSideValidation() {
        return paymentSideValidation;
    }

    public void setPaymentSideValidation(GoldenFileComparisonTaskInput paymentSideValidation) {
        this.paymentSideValidation = paymentSideValidation;
    }

    public GoldenFileComparisonTaskInput getActivityLogValidation() {
        return this.activityLogValidation;
    }

    public void setActivityLogValidation(final GoldenFileComparisonTaskInput activityLogValidation) {
        this.activityLogValidation = activityLogValidation;
    }

    public GoldenFileComparisonTaskInput getPrsDataValidation() {
        return this.prsDataValidation;
    }

    public void setPrsDataValidation(final GoldenFileComparisonTaskInput prsDataValidation) {
        this.prsDataValidation = prsDataValidation;
    }

    public GoldenFileComparisonTaskInput getIpnValidation() {
        return this.ipnValidation;
    }

    public void setIpnValidation(final GoldenFileComparisonTaskInput ipnValidation) {
        this.ipnValidation = ipnValidation;
    }

    public GoldenFileComparisonTaskInput getFinancialJournalValidation() {
        return this.financialJournalValidation;
    }

    public void setFinancialJournalValidation(final GoldenFileComparisonTaskInput financialJournalValidation) {
        this.financialJournalValidation = financialJournalValidation;
    }

    public GoldenFileComparisonTaskInput getPayloadValidation() {
        return this.payloadValidation;
    }

    public void setPayloadValidation(final GoldenFileComparisonTaskInput payloadValidation) {
        this.payloadValidation = payloadValidation;
    }

    public GoldenFileComparisonTaskInput getPaymentSenderValidation() {
        return this.paymentSenderValidation;
    }

    public void setPaymentSenderValidation(final GoldenFileComparisonTaskInput paymentSenderValidation) {
        this.paymentSenderValidation = paymentSenderValidation;
    }

    public GoldenFileComparisonTaskInput getPaymentRecipientValidation() {
        return this.paymentRecipientValidation;
    }

    public void setPaymentRecipientValidation(final GoldenFileComparisonTaskInput paymentRecipientValidation) {
        this.paymentRecipientValidation = paymentRecipientValidation;
    }

    public GoldenFileComparisonTaskInput getTopUpDataValidation() {
        return this.topUpDataValidation;
    }

    public void setTopUpDataValidation(final GoldenFileComparisonTaskInput topUpDataValidation) {
        this.topUpDataValidation = topUpDataValidation;
    }

    public GoldenFileComparisonTaskInput getPaymentRecipientSubBalanceValidation() {
        return this.paymentRecipientSubBalanceValidation;
    }

    public void setPaymentRecipientSubBalanceValidation(
            final GoldenFileComparisonTaskInput paymentRecipientSubBalanceValidation) {
        this.paymentRecipientSubBalanceValidation = paymentRecipientSubBalanceValidation;
    }

    public GoldenFileComparisonTaskInput getSettlementRequestValidation() {
        return this.settlementRequestValidation;
    }

    public void setSettlementRequestValidation(final GoldenFileComparisonTaskInput settlementRequestValidation) {
        this.settlementRequestValidation = settlementRequestValidation;
    }

    public GoldenFileComparisonTaskInput getPostPaymentResponseValidation() {
        return this.postPaymentResponseValidation;
    }

    public void setPostPaymentResponseValidation(final GoldenFileComparisonTaskInput postPaymentResponseValidation) {
        this.postPaymentResponseValidation = postPaymentResponseValidation;
    }
}
