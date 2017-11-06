package com.jptest.payments.fulfillment.testonia.business.component.postpay.p10;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.finsys.BankPITParticipantEventDataVO;
import com.jptest.money.BankCompletionReturnDataVO;
import com.jptest.money.BankReturnPropertiesVO;
import com.jptest.money.LegacyTable;
import com.jptest.money.PostBankReturnRequest;
import com.jptest.money.PostBankReturnResponse;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.ReadDepth;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.business.vo.money.OpaqueDataElementVOBuilder;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionP10DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.PaymentServConstants.Disposition;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;


/**
 * PostBankReturnOperationTask takes care of constructing the post bank Request and executing post bank and validates
 * response, it does not return any transaction ID.
 *
 * @JP Inc.
 */
public class PostBankReturnOperation10Task
        extends
        BasePostPayment10OperationTask<PostBankReturnRequest, PostBankReturnResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostBankReturnOperation10Task.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private PaymentReadServBridge plsBridge;

    @Inject
    private RetriableCheckIACHtoRStatusTask innerTask;

    @Override
    public PostBankReturnRequest constructPostPayRequest(
            final PostBankReturnRequest postBankReturnRequest,
            final Context context) {
        final User buyer = (User) this.getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
        final String accountNumber = buyer.getAccountNumber();
        this.innerTask.execute(accountNumber);

        String encryptedTransactionId = null;

        final GetLegacyEquivalentByPaymentReferenceRequest getLegacyEquivalentByPaymentReferenceRequest = new GetLegacyEquivalentByPaymentReferenceRequest();
        final GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse = this
                .getLegacyEquivalentByPaymentReference(
                        context, getLegacyEquivalentByPaymentReferenceRequest);

        final List<WTransactionVO> wTransactionList = getLegacyEquivalentByPaymentReferenceResponse
                .getLegacyEquivalent()
                .getWtransactions();

        Currency returnedAmount = null;

        for (final WTransactionVO wTxn : wTransactionList) {
            if (wTxn.getType() == WTransactionConstants.Type.ACHDEPOSIT.getValue()) {
                encryptedTransactionId = wTxn.getEncryptedId();
                returnedAmount = wTxn.getAmount();
                break;
            }
        }
        postBankReturnRequest.setTransactionId(encryptedTransactionId);

        /*
         * BankReturnPropertiesVO is needed when disposition is not COMPLETE
         */
        if (postBankReturnRequest.getDisposition().intValue() != Disposition.DISPOSITION_COMPLETED.getValue()) {
            BankReturnPropertiesVO bankReturnPropertiesVO = postBankReturnRequest.getBankReturnProperties();

            if (bankReturnPropertiesVO == null) {
                bankReturnPropertiesVO = new BankReturnPropertiesVO();
            }

            bankReturnPropertiesVO.setFundingHandle(encryptedTransactionId);
            bankReturnPropertiesVO.setAmountReturnedFromBank(returnedAmount);

            final BankPITParticipantEventDataVO bankPITParticipantEventDataVO = new BankPITParticipantEventDataVO();
            bankPITParticipantEventDataVO.setAmount(returnedAmount);
            postBankReturnRequest.setBankEvents(bankPITParticipantEventDataVO);

            final BankCompletionReturnDataVO bankCompletionReturnDataVO = new BankCompletionReturnDataVO();
            bankCompletionReturnDataVO.setReturnAmount(returnedAmount);

            final OpaqueDataElementVOBuilder opaqueReturnData = OpaqueDataElementVOBuilder.newBuilder()
                    .vo(bankCompletionReturnDataVO);

            postBankReturnRequest.setBankReturnPassthrough(opaqueReturnData.buildList().get(0));
            postBankReturnRequest.setBankReturnProperties(bankReturnPropertiesVO);
        }
        return postBankReturnRequest;
    }

    @Override
    public PostBankReturnResponse executePostPay(
            final PostBankReturnRequest postBankReturnRequest, final boolean call2PEX) {

        Assert.assertNotNull(postBankReturnRequest);
        final PostBankReturnResponse PostBankReturnResponse = this.paymentServBridge
                .postBankReturn(postBankReturnRequest);
        return PostBankReturnResponse;
    }

    @Override
    public void assertPostPayResponse(final PostBankReturnResponse postBankReturnResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(postBankReturnResponse,
                "post_bank_return response should not be null");

        Assert.assertEquals(postBankReturnResponse.getResult().toString(), postPayRequest.getReturnCode(),
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");
        LOGGER.info("post_bank_return Operation Passed");

    }

    protected GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReference(final Context context,
            final GetLegacyEquivalentByPaymentReferenceRequest request) {

        final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
        paymentSideReferenceVO
                .setReferenceValue(
                        (String) this.getDataFromContext(context, ContextKeys.WTRANSACTION_ID_KEY.getName()));
        paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
        request.setPaymentReference(paymentSideReferenceVO);
        final List<LegacyTable> legacyTable = new ArrayList<LegacyTable>();
        legacyTable.add(LegacyTable.WTRANSACTION);
        request.setRequestedLegacyTablesAsEnum(legacyTable);
        request.setMinimumReadDepth(ReadDepth.WHOLE_PAYMENT_TREE);
        request.setRequireRiskDecision(false);
        return this.plsBridge.getLegacyEquivalentByPaymentReference(request);
    }

    private static class RetriableCheckIACHtoRStatusTask extends RetriableTask<String, String> {

        private static final long MAX_WAIT_IN_MS = 125000;
        private static final long REPEAT_INTERVAL_IN_MS = 60 * 1000;

        @Inject
        private WTransactionP10DaoImpl wTxnDAO;

        @Override
        public boolean isDesiredOutput(final String output) {
            return output.equals("1");

        }

        @Override
        public String retriableExecute(final String input) {

            final int noOfCount = this.wTxnDAO
                    .getTransactionCountByAccountNumberAndTypeAndStatus(
                            new BigInteger(input),
                            String.valueOf(WTransactionConstants.Type.ACHDEPOSIT.getValue()),
                            String.valueOf(WTransactionConstants.Status.PROCESSING.getValue()));
            return String.valueOf(noOfCount);

        }

        @Override
        protected String onSuccess(final String input, final String output) {
            return output;
        }

        @Override
        protected String onFailure(final String input, final String output) {
            return output;
        }

        public RetriableCheckIACHtoRStatusTask() {

            super(MAX_WAIT_IN_MS, REPEAT_INTERVAL_IN_MS);

        }

    }

}
