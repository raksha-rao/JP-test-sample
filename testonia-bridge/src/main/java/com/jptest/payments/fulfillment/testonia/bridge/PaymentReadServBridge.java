package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.common.ActorInfoVO;
import com.jptest.money.GetCustomerTransactionByAlternateIdRequest;
import com.jptest.money.GetCustomerTransactionByAlternateIdResponse;
import com.jptest.money.GetCustomerTransactionLegacyEquivalentByAlternateIdRequest;
import com.jptest.money.GetCustomerTransactionLegacyEquivalentByAlternateIdResponse;
import com.jptest.money.GetFinancialTransactionsWithPropertiesByAlternateIdRequest;
import com.jptest.money.GetFinancialTransactionsWithPropertiesByAlternateIdResponse;
import com.jptest.money.GetPaymentAgreementByAlternateIdRequest;
import com.jptest.money.GetPaymentAgreementByAlternateIdResponse;
import com.jptest.money.LegacyTable;
import com.jptest.money.PaymentRead;
import com.jptest.payments.GetInternalValueObjectByPaymentReferenceRequest;
import com.jptest.payments.GetInternalValueObjectByPaymentReferenceResponse;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceListRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceListResponse;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.LegacyEquivalentInputDetailsVO;
import com.jptest.payments.PaymentLookup;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.ReadDepth;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;

/**
 * Class that helps making paymentreadserv service call
 */
@Singleton
public class PaymentReadServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentReadServBridge.class);

    private PaymentLookup paymentLookup;
    private PaymentRead paymentRead;

    @Inject
    public PaymentReadServBridge(@Named("paymentreadserv") final PaymentLookup paymentLookup,
            @Named("paymentreadserv") final PaymentRead paymentRead) {
        this.paymentLookup = paymentLookup;
        this.paymentRead = paymentRead;
    }

    /**
     * Returns transaction details for input encryptedTransactionId
     *
     * @param encryptedTransactionId
     * @return
     * @throws IOException
     */
    public GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReference(
            final String encryptedTransactionId) throws TestExecutionException {
        final GetLegacyEquivalentByPaymentReferenceRequest request = new GetLegacyEquivalentByPaymentReferenceRequest();
        final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
        paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
        paymentSideReferenceVO.setReferenceValue(encryptedTransactionId);
        request.setPaymentReference(paymentSideReferenceVO);
        final List<LegacyTable> legacyTables = new ArrayList<LegacyTable>();
        legacyTables.add(LegacyTable.WTRANSACTION);
        legacyTables.add(LegacyTable.WTRANSACTION_AUTH);
        legacyTables.add(LegacyTable.WTRANSACTION_BUFS);
        request.setRequestedLegacyTablesAsEnum(legacyTables);
        request.setMinimumReadDepth(ReadDepth.WHOLE_PAYMENT_TREE);
        request.setRequireRiskDecision(false);

        return this.getLegacyEquivalentByPaymentReference(request);
    }

    /**
     * Returns transaction details for input encryptedTransactionId
     *
     * @param encryptedTransactionId
     * @return
     * @throws IOException
     */
    public GetLegacyEquivalentByPaymentReferenceListResponse getLegacyEquivalentByPaymentReferenceList(
            final String orderId) throws TestExecutionException {
        final GetLegacyEquivalentByPaymentReferenceListRequest request = new GetLegacyEquivalentByPaymentReferenceListRequest();
        final List<LegacyEquivalentInputDetailsVO> inputList = new ArrayList<LegacyEquivalentInputDetailsVO>();
        final LegacyEquivalentInputDetailsVO input = new LegacyEquivalentInputDetailsVO();
        final ActorInfoVO actor = new ActorInfoVO();
        request.setActor(actor);
        final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
        paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
        paymentSideReferenceVO.setReferenceValue(orderId);
        input.setPaymentReference(paymentSideReferenceVO);
        input.setRequireRiskDecision(false);
        inputList.add(input);
        request.setInputDetails(inputList);

        return this.getLegacyEquivalentByPaymentReferenceListRequest(request);
    }

    public GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReference(
            final GetLegacyEquivalentByPaymentReferenceRequest prsRequest) throws TestExecutionException {
        GetLegacyEquivalentByPaymentReferenceResponse prsResponse = null;
        try {
            LOGGER.debug("PLS GetLegacyEquivalentByPaymentReference request: {}", printValueObject(prsRequest));
            prsResponse = this.paymentLookup.get_legacy_equivalent_by_payment_reference(prsRequest);
            LOGGER.debug("PLS GetLegacyEquivalentByPaymentReference response: {}", printValueObject(prsResponse));

        } catch (final Exception e) {
            LOGGER.error("Encountered exception while calling PaymentReadService", e);
            throw new TestExecutionException("Encountered exception while calling PaymentReadService");
        }

        if (prsResponse == null || prsResponse.getLegacyEquivalent() == null) {
            throw new TestExecutionException("Invalid/Empty Response from PRS for "
                    + prsRequest.getPaymentReference().getReferenceTypeAsEnum().getName() + " "
                    + prsRequest.getPaymentReference().getReferenceValue());
        }
        return prsResponse;
    }

    public GetLegacyEquivalentByPaymentReferenceListResponse getLegacyEquivalentByPaymentReferenceListRequest(
            final GetLegacyEquivalentByPaymentReferenceListRequest prsRequest) throws TestExecutionException {
        GetLegacyEquivalentByPaymentReferenceListResponse prsResponse = null;
        try {
            LOGGER.debug("PLS GetLegacyEquivalentByPaymentReferenceList request: {}", printValueObject(prsRequest));
            prsResponse = this.paymentLookup.get_legacy_equivalent_by_payment_reference_list(prsRequest);
            LOGGER.debug("PLS GetLegacyEquivalentByPaymentReferenceList response: {}", printValueObject(prsResponse));

        } catch (final Exception e) {
            LOGGER.error("Encountered exception while calling PaymentReadService", e);
            throw new TestExecutionException("Encountered exception while calling PaymentReadService");
        }

        if (prsResponse == null || prsResponse.getLegacyEquivalentResponses() == null) {
            throw new TestExecutionException("Invalid/Empty Response from PRS for list TRANSACTION_UNIT_HANDLE "
                    + prsRequest.getInputDetails().get(0).getPaymentReference().getReferenceValue());
        }
        return prsResponse;
    }

    /**
     * Returns transaction details for input encryptedTransactionId
     *
     * @param encryptedTransactionId
     * @return
     * @throws IOException
     */
    public GetCustomerTransactionByAlternateIdResponse getCustomerTransactionByAlternateIdRequest(
            final GetCustomerTransactionByAlternateIdRequest prsRequest) throws TestExecutionException {
        GetCustomerTransactionByAlternateIdResponse prsResponse = null;
        try {
            LOGGER.debug("PRS GetCustomerTransactionByAlternateId request: {}", printValueObject(prsRequest));
            prsResponse = this.paymentRead.get_customer_transaction_by_alternate_id(prsRequest);
            LOGGER.debug("PRS GetCustomerTransactionByAlternateId response: {}", printValueObject(prsResponse));

        } catch (final Exception e) {
            LOGGER.error("Encountered exception while calling PaymentReadService", e);
            throw new TestExecutionException("Encountered exception while calling PaymentReadService");
        }

        return prsResponse;
    }

    /**
     * Returns payment agreement recorded in legacy P1.0 payment system by the fulfillment service
     *
     * @param encryptedTransactionId
     * @return
     * @throws IOException
     */
    public GetPaymentAgreementByAlternateIdResponse getPaymentAgreementByAlternateIdRequest(
            final GetPaymentAgreementByAlternateIdRequest prsRequest) throws TestExecutionException {
        GetPaymentAgreementByAlternateIdResponse prsResponse = null;
        try {
            LOGGER.debug("PRS GetPaymentAgreementByAlternateId request: {}", printValueObject(prsRequest));
            prsResponse = this.paymentRead.get_payment_agreement_by_alternate_id(prsRequest);
            LOGGER.debug("PRS GetPaymentAgreementByAlternateId response: {}", printValueObject(prsResponse));

        } catch (final Exception e) {
            LOGGER.error("Encountered exception while calling PaymentReadService", e);
            throw new TestExecutionException("Encountered exception while calling PaymentReadService");
        }
        return prsResponse;
    }

    /**
     * Returns financial transaction details for the externally allocated Id
     *
     * @param encryptedTransactionId
     * @return
     * @throws IOException
     */
    public GetFinancialTransactionsWithPropertiesByAlternateIdResponse getFinancialTransactionsWithPropertiesByAlternateIdRequest(
            final GetFinancialTransactionsWithPropertiesByAlternateIdRequest prsRequest) throws TestExecutionException {
        GetFinancialTransactionsWithPropertiesByAlternateIdResponse prsResponse = null;
        try {
            LOGGER.debug("PRS GetFinancialTransactionsWithPropertiesByAlternateId request: {}",
                    printValueObject(prsRequest));
            prsResponse = this.paymentRead.get_financial_transactions_with_properties_by_alternate_id(prsRequest);
            LOGGER.debug("PRS GetFinancialTransactionsWithPropertiesByAlternateId response: {}",
                    printValueObject(prsResponse));

        } catch (final Exception e) {
            LOGGER.error("Encountered exception while calling PaymentReadService", e);
            throw new TestExecutionException("Encountered exception while calling PaymentReadService");
        }
        return prsResponse;
    }

    /**
     * Returns 1.0 Equivalent records by the externally allocated Id
     *
     * @param encryptedTransactionId
     * @return
     * @throws IOException
     */
    public GetCustomerTransactionLegacyEquivalentByAlternateIdResponse getCustomerTransactionLegacyEquivalentByAlternateIdRequest(
            final GetCustomerTransactionLegacyEquivalentByAlternateIdRequest prsRequest) throws TestExecutionException {
        GetCustomerTransactionLegacyEquivalentByAlternateIdResponse prsResponse = null;
        try {
            LOGGER.debug("PRS GetCustomerTransactionLegacyEquivalentByAlternateId request: {}",
                    printValueObject(prsRequest));
            prsResponse = this.paymentRead.get_customer_transaction_legacy_equivalent_by_alternate_id(prsRequest);
            LOGGER.debug("PRS GetCustomerTransactionLegacyEquivalentByAlternateId response: {}",
                    printValueObject(prsResponse));

        } catch (final Exception e) {
            LOGGER.error("Encountered exception while calling PaymentReadService", e);
            throw new TestExecutionException("Encountered exception while calling PaymentReadService");
        }
        return prsResponse;
    }

    /**
     * Returns Internal Value Objects
     */
    public GetInternalValueObjectByPaymentReferenceResponse getInternalValueObjectByPaymentReference(
            final GetInternalValueObjectByPaymentReferenceRequest prsRequest) {
        GetInternalValueObjectByPaymentReferenceResponse prsResponse = null;
        try {
            LOGGER.debug("PRS getInternalValueObjectByPaymentReference request: {}", printValueObject(prsRequest));
            prsResponse = this.paymentLookup.get_internal_value_object_by_payment_reference(prsRequest);
            LOGGER.debug("PRS getInternalValueObjectByPaymentReference response: {}", printValueObject(prsResponse));
        } catch (final Exception e) {
            LOGGER.error("Encountered exception while calling PaymentReadService", e);
            throw new TestExecutionException("Encountered exception while calling PaymentReadService");
        }
        return prsResponse;
    }
}
