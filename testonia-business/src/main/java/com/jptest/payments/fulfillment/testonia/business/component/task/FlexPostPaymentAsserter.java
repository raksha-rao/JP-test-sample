package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.LegacyTable;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.ReadDepth;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;


/**
 * Generic asserter that validates post payment which are migrated to FLEX by validating whether Flag6.IS_FLEX_PROCESSED
 * is set
 */
public class FlexPostPaymentAsserter extends BaseAsserter {

    @Inject
    private PaymentReadServBridge paymentReadServBridge;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlexPostPaymentAsserter.class);
    private boolean isFlexProcessed;

    public FlexPostPaymentAsserter(final boolean isFlexProcessed) {
        this.isFlexProcessed = isFlexProcessed;
    }

    /**
     * Basic validation of post payment which are migrated to FLEX is to validate whether Flag6.IS_FLEX_PROCESSED is
     * set.
     *
     * @see com.jptest.payments.fulfillment.testonia.core.Asserter#validate(com.jptest.payments.fulfillment.testonia.core.
     *      Context)
     * @param context
     */
    @Override
    public void validate(final Context context) {
        boolean flexProcessed = false;
        final GetLegacyEquivalentByPaymentReferenceRequest getLegacyEquivalentByPaymentReferenceRequest = new GetLegacyEquivalentByPaymentReferenceRequest();
        final GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse = this
                .getLegacyEquivalentByPaymentReference(context, getLegacyEquivalentByPaymentReferenceRequest);

        final List<WTransactionVO> wTransactionList = getLegacyEquivalentByPaymentReferenceResponse
                .getLegacyEquivalent().getWtransactions();

        for (final WTransactionVO wTransaction : wTransactionList) {
            if (wTransaction.getType() == WTransactionConstants.Type.REVERSAL.getByte()
                    && wTransaction.getStatus() == WTransactionConstants.Status.SUCCESS.getByte()
                    && wTransaction.getSubtype() == WTransactionConstants.Subtype.USERUSER_POSTAGE.getByte()) {
                if (wTransaction.getFlags6().and(WTransactionConstants.Flag6.IS_FLEX_PROCESSED.getValue())
                        .compareTo(WTransactionConstants.Flag6.IS_FLEX_PROCESSED.getValue()) == 0) {
                    flexProcessed = true;
                }
                Assert.assertEquals(flexProcessed, this.isFlexProcessed,
                        "FLAG6_FLEX_PROCESSED Flag Check in WTRANSACTION_P2 V Row ID " + wTransaction.getId());
                LOGGER.info("FLAG6_FLEX_PROCESSED Flag found in WTRANSACTION_P2 V Row ID {} : {}",
                        wTransaction.getId(), this.isFlexProcessed);
            }
        }

    }

    /**
     * Construct the PRS Request for getting the Transaction Model to verify the flag6 using WTRANSACTION_ID_KEY from
     * context.
     *
     * @param context
     * @param GetLegacyEquivalentByPaymentReferenceRequest
     * @return GetLegacyEquivalentByPaymentReferenceResponse
     */
    protected GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReference(final Context context,
            final GetLegacyEquivalentByPaymentReferenceRequest request) {

        final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
        final String txnId = (String) this.getDataFromContext(context, ContextKeys.WTRANSACTION_ID_KEY.getName());
        paymentSideReferenceVO.setReferenceValue(txnId);
        if (StringUtils.isNumeric(txnId)) {
            paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.LEGACY_BASE_ID);
        }
        else {
            paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
        }
        request.setPaymentReference(paymentSideReferenceVO);
        final List<LegacyTable> legacyTable = new ArrayList<LegacyTable>();
        legacyTable.add(LegacyTable.WTRANSACTION);
        request.setRequestedLegacyTablesAsEnum(legacyTable);
        request.setMinimumReadDepth(ReadDepth.WHOLE_PAYMENT_TREE);
        request.setRequireRiskDecision(false);
        return this.paymentReadServBridge.getLegacyEquivalentByPaymentReference(request);
    }

}
