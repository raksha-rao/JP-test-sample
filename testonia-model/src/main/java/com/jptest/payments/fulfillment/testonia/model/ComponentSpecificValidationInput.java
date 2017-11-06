package com.jptest.payments.fulfillment.testonia.model;

public class ComponentSpecificValidationInput {

    private GoldenFileComparisonTaskInput activityLogSORValidation;
    private GoldenFileComparisonTaskInput activityLogLegacyEventValidation;
    private GoldenFileComparisonTaskInput activityLogIPNValidation;
    private GoldenFileComparisonTaskInput activityLogFJValidation;
    private GoldenFileComparisonTaskInput fulfillmentActivityValidation;
    private GoldenFileComparisonTaskInput activityLogSettlementValidation;
    private GoldenFileComparisonTaskInput activityLogVoidRequestValidation;
    private GoldenFileComparisonTaskInput activityLogPaymentMessageValidation;

    public GoldenFileComparisonTaskInput getActivityLogSORValidation() {
        return this.activityLogSORValidation;
    }

    public void setActivityLogSORValidation(final GoldenFileComparisonTaskInput activityLogSORValidation) {
        this.activityLogSORValidation = activityLogSORValidation;
    }

    public GoldenFileComparisonTaskInput getActivityLogLegacyEventValidation() {
        return this.activityLogLegacyEventValidation;
    }

    public void setActivityLogLegacyEventValidation(
            final GoldenFileComparisonTaskInput activityLogLegacyEventValidation) {
        this.activityLogLegacyEventValidation = activityLogLegacyEventValidation;
    }

    public GoldenFileComparisonTaskInput getActivityLogIPNValidation() {
        return this.activityLogIPNValidation;
    }

    public void setActivityLogIPNValidation(final GoldenFileComparisonTaskInput activityLogIPNValidation) {
        this.activityLogIPNValidation = activityLogIPNValidation;
    }

    public GoldenFileComparisonTaskInput getActivityLogFJValidation() {
        return this.activityLogFJValidation;
    }

    public void setActivityLogFJValidation(final GoldenFileComparisonTaskInput activityLogFJValidation) {
        this.activityLogFJValidation = activityLogFJValidation;
    }

    public GoldenFileComparisonTaskInput getFulfillmentActivityValidation() {
        return this.fulfillmentActivityValidation;
    }

    public void setFulfillmentActivityValidation(final GoldenFileComparisonTaskInput fulfillmentActivityValidation) {
        this.fulfillmentActivityValidation = fulfillmentActivityValidation;
    }

    public GoldenFileComparisonTaskInput getActivityLogSettlementValidation() {
        return this.activityLogSettlementValidation;
    }

    public void setActivityLogSettlementValidation(
            final GoldenFileComparisonTaskInput activityLogSettlementValidation) {
        this.activityLogSettlementValidation = activityLogSettlementValidation;
    }

    public GoldenFileComparisonTaskInput getActivityLogVoidRequestValidation() {
        return this.activityLogVoidRequestValidation;
    }

    public void setActivityLogVoidRequestValidation(
            final GoldenFileComparisonTaskInput activityLogVoidRequestValidation) {
        this.activityLogVoidRequestValidation = activityLogVoidRequestValidation;
    }

    public GoldenFileComparisonTaskInput getActivityLogPaymentMessageValidation() {
        return this.activityLogPaymentMessageValidation;
    }

    public void setActivityLogPaymentMessageValidation(
            final GoldenFileComparisonTaskInput activityLogPaymentMessageValidation) {
        this.activityLogPaymentMessageValidation = activityLogPaymentMessageValidation;
    }

}
