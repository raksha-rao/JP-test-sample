package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
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
import com.jptest.payments.fulfillment.testonia.business.component.validation.EngineActivityIdCheckTask;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.business.vo.money.OpaqueDataElementVOBuilder;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.PaymentServConstants.Disposition;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.types.Currency;

/**
 * PostBankReturnOperationTask takes care of constructing the post bank Request and executing post bank and validates
 * response
 *
 * @JP Inc.
 */
public class PostBankReturnOperationTask
        extends
        BasePostPaymentOperationTask<PostBankReturnRequest, PostBankReturnResponse> {

    private static final int RETRY_INTERVAL = 15000;

    private static final int MAX_WAIT_TIME = 240000;

    private static final Logger LOGGER = LoggerFactory.getLogger(PostBankReturnOperationTask.class);

    private WTransactionVO wTransactionVO;

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private PaymentReadServBridge plsBridge;

    @Inject
    private CryptoUtil cryptoUtil;

    @Override
    public PostBankReturnRequest constructPostPayRequest(
            final PostBankReturnRequest postBankReturnRequest,
            final Context context) {
        String encryptedTransactionId = null;

        final GetLegacyEquivalentByPaymentReferenceRequest getLegacyEquivalentByPaymentReferenceRequest = new GetLegacyEquivalentByPaymentReferenceRequest();
        final GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse = this
                .getLegacyEquivalentByPaymentReference(
                        context, getLegacyEquivalentByPaymentReferenceRequest);

        final List<WTransactionVO> wTransactionList = getLegacyEquivalentByPaymentReferenceResponse
                .getLegacyEquivalent()
                .getWtransactions();

        Currency returnedAmount = null;

		wTransactionVO = getBankFundingWTransactionVO(wTransactionList);
		encryptedTransactionId = wTransactionVO.getEncryptedId();
		returnedAmount = wTransactionVO.getAmount();

        postBankReturnRequest.setTransactionId(encryptedTransactionId);
        context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), encryptedTransactionId);

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

	protected WTransactionVO getBankFundingWTransactionVO(final List<WTransactionVO> wTransactionList) {

		WTransactionVO bankFundingVO = null;
		for (final WTransactionVO wTxn : wTransactionList) {
			if (wTxn.getType() == WTransactionConstants.Type.ACHDEPOSIT.getValue() ||
			        wTxn.getType() == WTransactionConstants.Type.EXT_INIT_DEPOSIT.getValue()) {
				bankFundingVO = wTxn;
				break;
			}
		}
		Assert.assertNotNull(bankFundingVO, this.getClass().getSimpleName() + ".getBankFundingWTransactionVO() Failed to find ACHDEPOSIT row.");
		return bankFundingVO;
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
        String txnId = (String) this.getDataFromContext(context, ContextKeys.WTRANSACTION_ID_KEY.getName());
        paymentSideReferenceVO.setReferenceValue(txnId);
        if (StringUtils.isNumeric(txnId)) {
            paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.LEGACY_BASE_ID);
        }
        else{
            paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);     
        }
        request.setPaymentReference(paymentSideReferenceVO);
        final List<LegacyTable> legacyTable = new ArrayList<LegacyTable>();
        legacyTable.add(LegacyTable.WTRANSACTION);
        request.setRequestedLegacyTablesAsEnum(legacyTable);
        request.setMinimumReadDepth(ReadDepth.WHOLE_PAYMENT_TREE);
        request.setRequireRiskDecision(false);
        return this.plsBridge.getLegacyEquivalentByPaymentReference(request);
    }

    @Override
    protected void populateActivityId(final Context context, final PostBankReturnResponse response) {
    	final String activityId = response.getActivityId();
    	if(activityId == null || activityId.isEmpty() || activityId.equals("")) {
    		context.setData(getActivityIdKey(), BigInteger.ZERO);
    	} else {	 
	        final EngineActivityIdCheckTask activityIdCheck = new EngineActivityIdCheckTask(MAX_WAIT_TIME, RETRY_INTERVAL,
	                new BigInteger(activityId));
	
	        final BigInteger activityIdPersisted = activityIdCheck.process(context);
	        context.setData(getActivityIdKey(), activityIdPersisted);
    	}
    }

    @Override
    protected void populateIpnEncryptedId(Context context, PostBankReturnResponse response) {
        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        if(wTransactionVO == null) return;

        try {
            String encryptedParentId = this.cryptoUtil.encryptTxnId(
                    wTransactionVO.getParentId().longValue(),
                    wTransactionVO.getAccountNumber().longValue());
            context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(), encryptedParentId);
        } catch (Exception e) {
            throw new TestExecutionException("Encryption of Parent ID Failed", e);
        }
    }
}
