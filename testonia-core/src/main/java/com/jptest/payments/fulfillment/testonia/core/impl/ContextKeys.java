package com.jptest.payments.fulfillment.testonia.core.impl;

public enum ContextKeys {
	
	CONTEXT_KEY("context_key"),

    CORRELATION_ID("correlationId"),

    WTRANSACTION_LIST_KEY("fulfillment_response_wtransactions"),

    WTRANSACTION_BUFS_LIST_KEY("wtransaction_bufs_list"),
    
    USER_CREATION("create_users"),

    SELLER_VO_KEY("fulfillment_seller"),

    BUYER_VO_KEY("fulfillment_buyer"),

    FUNDER_VO_KEY("fulfillment_funder"),

    THIRD_PARTY_VO_KEY("fulfillment_thirdParty"),

    WTRANSACTION_ACTIVITYID_SET("fulfillment_response_activityid_set"),

    WTRANSACTION_LIST_ACTIVITY_DETAILS_KEY("fulfillment_response_wtransactions_activity_details"),

    WTRANSACTION_PENDING_ID_KEY("fulfillment_response_pending_id"),

    WTRANSACTION_ID_KEY("wtransaction_id"),

    TXN_HOLD_RELEASE_TASK_KEY("txn_hold_release_task"),

    FULFILL_PLAN_RESPONSE_KEY("fulfillPlanResponse"),

    FULFILL_PLAN_IDEMPOTENT_RESPONSE_KEY("fulfillPlanIdempotentResponse"),

    CURRENT_STAGE_NAME_KEY("current_stage_name"),

    GOLDEN_FILE_OUTPUT_LOCATION("golden.file.output.location"),

    GOLDEN_FILE_INPUT_LOCATION("golden.file.input.location"),

    FULFILL_PAYMENT_REQUEST_KEY("fulfillPaymentRequest"),

    PAYMENT_MESSAGE_REQUEST_KEY("paymentMessageRequest"),

    PAYMENT_MESSAGE_RESPONSE_KEY("paymentMessageResponse"),

    PAYMENT_MESSAGE_VALIDATION_KEY("paymentMessageValidation"),

    RESERVE_FUND_ACTIVITY_ID("reserve_fund_activity_id"),

    POST_PAYMENT_REQUEST_KEY("PostPaymentRequest"),

    POST_PAYMENT_RESPONSE_KEY("PostPaymentResponse"),

    IPN_URL("ipn_url"),

    PRS_RESPONSE_KEY("prs_response"),

    PRS_LIST_RESPONSE_KEY("prs_list_response"),

    POST_PAYMENT_TOGGLES_KEY("post_payment_toggles"),

    HOLDING_TRANSACTION_EXISTS_KEY("holding_transaction_record_exists"),

    IPN_VALIDATION("ipn_validation"),

    IPN_ENCRYPTED_ID_KEY("ipn_encrypted_id"),

    ENGINE_ACTIVITY_ID_KEY("engine_activity_id"),

    PAY_RESPONSE_KEY("PAY_RESPONSE"),

    CREATE_PENDING_REVERSAL_ACTIVITY_ID("create_pending_payment_reversal_activity_id"),

    COMPLETE_PENDING_REVERSAL_ACTIVITY_ID("complete_pending_payment_reversal_activity_id"),

    CANCEL_PENDING_REVERSAL_ACTIVITY_ID("cancel_pending_payment_reversal_activity_id"),

    REVERSAL_ACTIVITY_ID("reverse_payment_activity_id"),

    REFUND_ACTIVITY_ID("refund_payment_activity_id"),

    PRECREATED_USECASES("precreated_usecases"),

    TESTCASE_UNIQUE_ID("testcase_unique_id"),
    DISPUTE_PAYOUT_RECOUP_ACTIVITY_ID("dispute_payout_recoup_activity_id"),

    CREATE_HOLD_ACTIVITY_ID("create_hold_activity_id"),
    RELEASE_HOLD_ACTIVITY_ID("release_hold_activity_id"),
    TRANSITION_TO_HOLD_ACTIVITY_ID("transition_to_hold_activity_id"),
    CREATE_RESERVE_ACTIVITY_ID("create_reserve_activity_id"),
    RELEASE_RESERVE_ACTIVITY_ID("release_reserve_activity_id"),
    TRANSITION_TO_RESERVE_ACTIVITY_ID("transition_to_reserve_activity_id"),

    RECOVER_FUNDS_ACTIVITY_ID("recover_disputed_funds_activity_id"),

    DISPUTE_PAYOUT("dispute_payout"),

    ENCRYPTED_PAYOUT_TRANSACTION_ID("encrypted_payout_transaction_id"),

    FULFILL_PLAN_DUPLICATE_INVOICE_RESPONSE_KEY("fulfillPlanDuplicateInvoiceResponse"),
    FULFILL_PAYMENT_FLAGS("fulfill_payment_flags"),
    RECOUP_INTENT("recoup_intent"),

    TESTCASE_INPUT_DATA("testcase_input_data"),

    jptest_PAYOUT("jptest_payout"),

    WITHDRAW("withdraw"),

    REVERSE_jptest_PAYOUT("reverse_jptest_payout"),

    POST_BANK_RETURN("post_bank_return"),

    PAYMENT("payment"),

    PSS_RESPONSE_KEY("pss_response"),

    IGNORABLE_FLAGS_LOCATION("ignorable_flags_location"),

    GOAL_ACCOUNT_CREATION_TASK_KEY("goal_account_creation_task"),

    GOAL_ACCOUNT_CREDIT_TASK_KEY("goal_account_credit_task"),

    PLAN_AGREEMENT_RESPONSE_KEY("planAgreementResponse"),
    
    FULFILL_BULKORDER_REQUEST_KEY("fulfillBulkOrderRequest"),

    FULFILL_BULKORDER_RESPONSE_KEY("fulfillBulkOrderResponse"),
    
    BULKORDER_IDEMPOTENT_RESPONSE_KEY("BulkOrderIdempotentResponse"),

    FULFILL_PAYMENT_RESPONSE_KEY("fulfillPaymentResponse"),

    FULFILL_DISBURSEMENT_RESPONSE_KEY("fulfillDisbursementResponse"),

    PRIMARY_PARTNER("primaryPartner"),

    SECONDARY_PARTNER("secondaryPartner"),

    PARTNER_DISBURSEMENT("partnerDisbursement"),
    
    ORDER_ID("orderId"),
    
    ORDER_ACTIVITY_ID("orderActivityId"),
    
    SELLER_ALIAS("sellerAlias"),
    
	IDEMPOTENCY_ID("idempotencyId"),
	
	INVOICE_ID("invoiceId"),
	IDEMPOTENT_FULFILL_PAYMENT_REQUEST_KEY("idempotentFulfillPaymentRequest"),
	
	IDEMPOTENCY_RESPONSE("idempotencyResponse"),

	GENERATE_CREDIT_CARD("generateCreditCard"),

	ANALYZE_ADD_FI_RESPONSE_KEY("analyzeAddFiRsponse"),

	COMNNERCIAL_ENTITY_REG("insert_commercial_entity_reg"),

	PAY_V2_RESPONSE("PayV2_reponse"),

	PURCHASE_CONTEXT("purchaseContext"),

    INCENTIVE_INSTRUMENT_KEY("incentiveInstrument"),

    OFFER_KEY("offerKey"),

    OFFER_PROGRAM_KEY("offerProgramKey"),

    DECRYPTED_OFFER_ID_KEY("decryptedOfferId"),

    DECRYPTED_OFFER_PROGRAM_ID_KEY("decryptedOfferProgramId"),

    FUNDER_ACCOUNT_LIST("funder_list"),
	
	AUTH_REQUEST("auth_request"),
	
	AUTH_RESPONSE("auth_response"),
	
	SERVICE_MARK_DOWN("service_markdown"),

	TEP_REQUEST_KEY("tepRequestKey"),

    TEP_RESPONSE_KEY("tepResponseKey"),

    PRS_IDMAP_KEY("prsIDMapKey"),

    CLEAR_MAYFLY_PAYMENT_EVENTS("clearMayflyPaymentEvents"),

    WRITE_DATA_IN_MAYFLY_KEY("writePaymentEventsInMayfly"),

    UNIQUE_VO_KEYS("uniqueVOKeys"),

    PRS_VALIDATE_RESPONSE("prsValidateResponse"),

    PRS_REF_TYPES("prsRefTypes"),
    
    FFX_INFO("ffxInfo");

    private String name;

    private ContextKeys(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
