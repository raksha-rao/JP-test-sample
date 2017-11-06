package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.money.FulfillPaymentRequest;
import com.jptest.money.LegacyTable;
import com.jptest.money.WTransactionBUFSVO;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.OperationStatus;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.ReadDepth;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableBaseTask;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * Task to retrieve transaction details from Paymentreadserv Paymentreadserv returns results from MayFly if its
 * available. Otherwise fetches from SOR.
 */
public class PaymentReadServRetrieverTask extends RetriableBaseTask<GetLegacyEquivalentByPaymentReferenceResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentReadServRetrieverTask.class);

    @Inject
    private PaymentReadServBridge plsBridge;

    private Function<Context, GetLegacyEquivalentByPaymentReferenceRequest> requestBuilder;

    private PaymentReferenceTypeCode refTypeCode = PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE;

    public PaymentReadServRetrieverTask() {
        // max wait 1 min with intervals of 10 seconds
        super(1 * 60 * 1000, 10 * 1000);
    }

    public PaymentReadServRetrieverTask(final PaymentReferenceTypeCode refType) {
        this.refTypeCode = refType;
    }

    /*
     * Method reference to construct the PRS request
     */
    public PaymentReadServRetrieverTask(
            final Function<Context, GetLegacyEquivalentByPaymentReferenceRequest> requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @Override
    public GetLegacyEquivalentByPaymentReferenceResponse process(final Context context) {
        if (config.getBoolean(BizConfigKeys.PRS_RETRY_MODE.getName(), false)) {
            return super.process(context);
        } else {
            return processPRS(context);
        }
    }

    private GetLegacyEquivalentByPaymentReferenceResponse processPRS(Context context) {
        GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse = null;

        GetLegacyEquivalentByPaymentReferenceRequest getLegacyEquivalentByPaymentReferenceRequest = null;

        if (this.requestBuilder != null)

        {
            getLegacyEquivalentByPaymentReferenceRequest = this.requestBuilder.apply(context);
        } else

        {
            getLegacyEquivalentByPaymentReferenceRequest = this
                    .getLegacyEquivalentByPaymentReferenceRequest(context);
        }
        try {
            getLegacyEquivalentByPaymentReferenceResponse = this.plsBridge
                    .getLegacyEquivalentByPaymentReference(getLegacyEquivalentByPaymentReferenceRequest);
            if (getLegacyEquivalentByPaymentReferenceResponse == null
                    || getLegacyEquivalentByPaymentReferenceResponse
                            .getStatusAsEnum() != OperationStatus.COMPLETED_OK) {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("PRS call failed with: it will be retried if prs.retry.mode is set to true");
            return null;
        }
        LOGGER.info("PLS call response status: {}", getLegacyEquivalentByPaymentReferenceResponse.getStatusAsEnum());
        final List<WTransactionVO> wTransactionList = getLegacyEquivalentByPaymentReferenceResponse
                .getLegacyEquivalent()
                .getWtransactions();
        // add wtransactions to context
        if (wTransactionList != null)

        {
            LOGGER.info("Retrived {} transactions for encryptedTransactionId {}", wTransactionList.size(),
                    getLegacyEquivalentByPaymentReferenceRequest.getPaymentReference().getReferenceValue());
            context.setData(ContextKeys.WTRANSACTION_LIST_KEY.getName(), wTransactionList);
            for (final WTransactionVO wTransactionVO : wTransactionList) {
                LOGGER.info("Transaction ID: {}", wTransactionVO.getId());
            }
        }

        // add bufs to context
        final List<WTransactionBUFSVO> wTransactionBufsList = getLegacyEquivalentByPaymentReferenceResponse
                .getLegacyEquivalent().getWtransactionBufs();
        if (wTransactionBufsList != null)

        {
            context.setData(ContextKeys.WTRANSACTION_BUFS_LIST_KEY.getName(), wTransactionBufsList);
        }

        addEngineActivityIdToContext(getLegacyEquivalentByPaymentReferenceResponse, context);
        return getLegacyEquivalentByPaymentReferenceResponse;
    }

    /**
     * Retrieves the engine activity id and stores it in context
     * using {@link ContextKeys.ENGINE_ACTIVITY_ID_KEY}
     * @param getLegacyEquivalentByPaymentReferenceResponse
     * @param context
     */
    private void addEngineActivityIdToContext(
            GetLegacyEquivalentByPaymentReferenceResponse response,
            Context context) {
        List<PaymentSideReferenceVO> relatedPaymentReferences = response.getRelatedPaymentReferences();
        if (CollectionUtils.isEmpty(relatedPaymentReferences)) {
            return;
        }

        for (PaymentSideReferenceVO vo : relatedPaymentReferences) {
            if (vo.getReferenceTypeAsEnum() == PaymentReferenceTypeCode.FULFILLMENT_ACTIVITY_ID) {
                context.setData(ContextKeys.ENGINE_ACTIVITY_ID_KEY.getName(), new BigInteger(vo.getReferenceValue()));
                return;
            }
        }

    }

    protected GetLegacyEquivalentByPaymentReferenceRequest getLegacyEquivalentByPaymentReferenceRequest(
            final Context context) {
        String refId = null;
        if (this.refTypeCode == PaymentReferenceTypeCode.IDEMPOTENCY_ID) {
            final FulfillPaymentRequest request = (FulfillPaymentRequest) this.getDataFromContext(context,
                    ContextKeys.FULFILL_PAYMENT_REQUEST_KEY.getName());
            refId = request.getIdempotenceId();
        } else {
            refId = this.getReferenceValue(context);
        }
        final GetLegacyEquivalentByPaymentReferenceRequest request = new GetLegacyEquivalentByPaymentReferenceRequest();
        final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
        paymentSideReferenceVO.setReferenceType(this.refTypeCode);
        paymentSideReferenceVO.setReferenceValue(refId);
        request.setPaymentReference(paymentSideReferenceVO);
        request.setRequestedLegacyTablesAsEnum(this.getLegacyTables());
        request.setMinimumReadDepth(ReadDepth.WHOLE_PAYMENT_TREE);
        request.setRequireRiskDecision(false);
        return request;

    }

    protected List<LegacyTable> getLegacyTables() {
        final List<LegacyTable> legacyTables = new ArrayList<LegacyTable>();
        legacyTables.add(LegacyTable.WTRANSACTION);
        legacyTables.add(LegacyTable.WTRANSACTION_AUTH);
        legacyTables.add(LegacyTable.WSUBBALANCE_TRANSACTION);
        legacyTables.add(LegacyTable.PAYMENT_EXTENSIONS);
        return legacyTables;
    }

    protected String getReferenceValue(final Context context) {
        return this.getEncryptedTxnId(context);
    }

    private String getEncryptedTxnId(final Context context) {
        Object txnHandleObject = this.getDataFromContext(context, ContextKeys.WTRANSACTION_ID_KEY.getName());
        if (txnHandleObject == null) {
            throw new TestExecutionException("TransactionId is not available to process in WTransactionRetrieverTask");
        }
        final String txnHandle = txnHandleObject.toString();
        if (StringUtils.isNumeric(txnHandle)) {
            this.refTypeCode = PaymentReferenceTypeCode.LEGACY_BASE_ID;
        }
        return txnHandle;
    }

    @Override
    protected boolean isDesiredOutput(GetLegacyEquivalentByPaymentReferenceResponse output) {
        return output != null;
    }

    @Override
    protected GetLegacyEquivalentByPaymentReferenceResponse retriableExecute(Context context) {
        LOGGER.info("Running PRS call in a retry mode");
        return this.processPRS(context);
    }

    @Override
    protected GetLegacyEquivalentByPaymentReferenceResponse onSuccess(Context context,
            GetLegacyEquivalentByPaymentReferenceResponse output) {
        return output;
    }

    @Override
    protected GetLegacyEquivalentByPaymentReferenceResponse onFailure(Context context,
            GetLegacyEquivalentByPaymentReferenceResponse output) {
        LOGGER.error("Ran out of time to get the PRS data");
        throw new TestExecutionException("PRS didn't return data in given time");
    }

}
