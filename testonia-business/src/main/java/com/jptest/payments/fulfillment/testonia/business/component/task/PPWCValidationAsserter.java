package com.jptest.payments.fulfillment.testonia.business.component.task;

import static com.jptest.payments.OperationStatus.COMPLETED_OK;
import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.WRelatedTransactionDao;
import com.jptest.payments.fulfillment.testonia.model.money.WRelatedTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


/* (non-Javadoc)
* PPWCValidationAsserter validates the WRelated_Transaction table. For auto loan repayment we would have a related transaction id with the
* parent transaction id(repayment transaction id) of type L. For manual loan repayment there would be no entry in WRelated_Transaction table.
*/
public class PPWCValidationAsserter extends BaseAsserter {
    private static final String AUTO_LOAN_REPAYMENT = "AUTO_LOAN_REPAYMENT";
    private static final String PPWC_MANUAL_LOAN_REPAYMENT = "PPWC_MANUAL_LOAN_REPAYMENT";
    private static final Logger LOGGER = LoggerFactory.getLogger(PPWCValidationAsserter.class);
    @Inject
    private PaymentReadServBridge paymentReadServBridge;
    @Inject
    private WRelatedTransactionDao wRelatedTransactionDao;
    private String loanType;

    public PPWCValidationAsserter(String loanType) {
        this.loanType = loanType;
    }

    @Override public void validate(Context context) {
        final User sender = (User) this.getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
        FulfillPaymentResponse fulfillPaymentResponse = (FulfillPaymentResponse) getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());
        String txnHandle = fulfillPaymentResponse.getFulfillmentHandle();
        GetLegacyEquivalentByPaymentReferenceResponse plsResponse = loadPersistedDataUsingPls(txnHandle);
        String accountNumber = sender.getAccountNumber();
        String repaymentTxnId = getFirstLegTxnId(accountNumber, plsResponse);
        //kibana will have repaymentTxnId data, even if assertion fails
        context.addReportingAttribute(ReportingAttributes.REPAYMENT_TXN_ID, repaymentTxnId);
        validateWRelatedTransaction(repaymentTxnId);
    }

    protected void validateWRelatedTransaction(String repaymentTxnId) {
        LOGGER.info("Validating wRelated_transaction table");
        List<WRelatedTransactionDTO> wRelatedTransactionDTOs = wRelatedTransactionDao
                .getWRelatedTransactionDetails(repaymentTxnId);
        if (wRelatedTransactionDTOs.size() > 0 &&
                wRelatedTransactionDTOs.stream().filter(x -> x.getAssociationType().equalsIgnoreCase("L")).count()
                        > 0 && loanType.equalsIgnoreCase(AUTO_LOAN_REPAYMENT)) {
            LOGGER.info("Valid record found in WRelated_Transaction table for repaymentTxnId {}", repaymentTxnId);
        }
        else if (wRelatedTransactionDTOs.size() == 0 && loanType.equalsIgnoreCase(PPWC_MANUAL_LOAN_REPAYMENT)) {
            LOGGER.info(
                    "No record exists in WRelated_Transaction table for PPWC_MANUAL_LOAN_REPAYMENT with repaymentTxnId {}",
                    repaymentTxnId);
        }
        else {
            Assert.fail(this.getClass().getSimpleName()
                    + ".validateWRelatedTransaction() failed because no valid record found in WRelated_Transaction table for repaymentTxnId: {}"
                    + repaymentTxnId);
        }
    }

    private GetLegacyEquivalentByPaymentReferenceResponse loadPersistedDataUsingPls(String transactionHandle) {

        GetLegacyEquivalentByPaymentReferenceResponse plsResponse = null;
        GetLegacyEquivalentByPaymentReferenceRequest request = new GetLegacyEquivalentByPaymentReferenceRequest();
        request.setRequestedLegacyTablesAsEnum(new ArrayList<>());
        PaymentSideReferenceVO reference = new PaymentSideReferenceVO();
        reference.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
        reference.setReferenceValue(transactionHandle);
        request.setPaymentReference(reference);
        try {
            plsResponse = paymentReadServBridge
                    .getLegacyEquivalentByPaymentReference(request);
            LOGGER.info(printValueObject(plsResponse));
            if (plsResponse.getStatusAsEnum() == COMPLETED_OK)
                return plsResponse;
            else {
                LOGGER.error("ERROR in pls response {}", plsResponse.getStatusAsEnum().getName());
            }
        }
        catch (Exception ex) {
            LOGGER.error("Could not load PLS data {}", ex);
        }
        return plsResponse;
    }

    private String getFirstLegTxnId(String senderAccountNumber,
            GetLegacyEquivalentByPaymentReferenceResponse plsResponse) {
        List<WTransactionVO> wtxns = plsResponse.getLegacyEquivalent().getWtransactions();
        for (WTransactionVO wtxn : wtxns) {
            if (wtxn.getType() == WTransactionConstants.Type.USERUSER.getByte()
                    && wtxn.getAccountNumber().toString().equalsIgnoreCase(senderAccountNumber)) {
                return wtxn.getId().toString();
            }
        }
        return null;
    }
}
