package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.merchant.RecoupmentElementVO;
import com.jptest.money.DisputePayoutRecoupResponse;
import com.jptest.money.LegacyTable;
import com.jptest.money.RecoupmentOperationStatusType;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.OperationStatus;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.ReadDepth;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.gmf.WThirdPartyRecoupDAO;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.gmf.WThirdPartyRecoupDTO;
import com.jptest.payments.fulfillment.testonia.model.money.RecoupRequest;
import com.jptest.payments.fulfillment.testonia.model.util.RecoupConstants.RecoupAction;
import com.jptest.payments.fulfillment.testonia.model.util.RecoupConstants.RecoupStatus;
import com.jptest.payments.fulfillment.testonia.model.util.RecoupConstants.WaiveFeePolicy;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants.Subtype;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants.Type;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;

/**
 * DisputePayoutRecoupmentOperationTask takes care of constructing the
 * PayoutRecoupment request , executes and validates the response
 *
 * @JP Inc.
 */
public class DisputePayoutRecoupOperationTask
		extends BasePostPaymentOperationTask<RecoupRequest, DisputePayoutRecoupResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DisputePayoutRecoupOperationTask.class);

	@Inject
	private PostPaymentExecService postPaymentService;
	@Inject
	private WThirdPartyRecoupDAO wThirdPartyRecoupDAO;
	@Inject
	private PaymentReadServBridge plsBridge;

	@Override
	public RecoupRequest constructPostPayRequest(final RecoupRequest recoupRequest, final Context context) {

		LOGGER.info("***************Building dispute_payout_recoup request***************");
		final String payoutTransactionId = (String) this.getDataFromContext(context,
				ContextKeys.ENCRYPTED_PAYOUT_TRANSACTION_ID.getName());
		Assert.assertNotNull(payoutTransactionId, "Validate payoutTransactionId is not null");
		final User thirdParty = (User) this.getDataFromContext(context, ContextKeys.THIRD_PARTY_VO_KEY.getName());
		final User seller = (User) this.getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());
		final BigInteger thirdPartyAccountNumber = new BigInteger(thirdParty.getAccountNumber());
		final BigInteger sellerAccountNumber = new BigInteger(seller.getAccountNumber());
		recoupRequest.getDisputePayoutRecoupRequest().setSellerAccount(sellerAccountNumber);

		WThirdPartyRecoupDTO recoupData = new WThirdPartyRecoupDTO();
		recoupData.setPayoutTransactionId(payoutTransactionId);
		recoupData.setInvoiceId(recoupRequest.getInvoiceId());
		recoupData.setSellerAccountNumber(sellerAccountNumber);
		recoupData.setPayoutFundingAccountNumber(thirdPartyAccountNumber);
		recoupData.setWaiveFeePolicy(recoupRequest.getWaiveFeePolicy().getByte());
		if (recoupRequest.getRecoupAction() == RecoupAction.SCHEDULE_RECOUP) {
			scheduleRecoup(recoupData);
		}
		
        recoupRequest.getDisputePayoutRecoupRequest()
                .setRecoupmentElements(populateRecoupList(recoupData, recoupRequest));
		
		return recoupRequest;
	}

	@Override
	public DisputePayoutRecoupResponse executePostPay(final RecoupRequest recoupRequest, final boolean call2PEX) {
		Assert.assertNotNull(recoupRequest);

		return this.postPaymentService.disputePayoutRecoupService(recoupRequest.getDisputePayoutRecoupRequest(),
				call2PEX);
	}

	@Override
	public void assertPostPayResponse(final DisputePayoutRecoupResponse disputePayoutRecoupResponse,
			final PostPaymentRequest postPayRequest, final Context context) {
		final DisputePayoutRecoupResponse response = disputePayoutRecoupResponse;
		Assert.assertNotNull(response, "post payment response should not be null");

		LOGGER.info("dispute_payout_recoup Operation Passed");

	}

	private GetLegacyEquivalentByPaymentReferenceResponse loadTransactionDetails(final String referenceValue) {
		final GetLegacyEquivalentByPaymentReferenceRequest request = new GetLegacyEquivalentByPaymentReferenceRequest();
		final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
		paymentSideReferenceVO.setReferenceValue(referenceValue);
		paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
		request.setPaymentReference(paymentSideReferenceVO);
		final List<LegacyTable> legacyTable = new ArrayList<LegacyTable>();
		legacyTable.add(LegacyTable.WTRANSACTION);
		legacyTable.add(LegacyTable.FEE_HISTORY);
		request.setRequestedLegacyTablesAsEnum(legacyTable);
		request.setMinimumReadDepth(ReadDepth.WHOLE_PAYMENT_TREE);
		request.setRequireRiskDecision(false);
		return this.plsBridge.getLegacyEquivalentByPaymentReference(request);
	}

	@Override
	protected void populateActivityId(final Context context, final DisputePayoutRecoupResponse response) {
		if (response.getResultAsEnum() == RecoupmentOperationStatusType.RESULT_OK) {
			final BigInteger activityId = BigInteger.ZERO;
			Assert.assertNotNull(activityId);
			context.setData(this.getActivityIdKey(), activityId);
			LOGGER.info("DISPUTE_PAYOUT: Activity ID - {}", activityId);
		}
	}
	
	private void scheduleRecoup(WThirdPartyRecoupDTO recoupData) {
		List<WTransactionVO> wtransactionList = null;
		WTransactionVO feeRecord = null;
		WTransactionVO payoutRecord = null;
		final GetLegacyEquivalentByPaymentReferenceResponse plsResponse = this
				.loadTransactionDetails(recoupData.getPayoutTransactionId());
		Assert.assertTrue(plsResponse.getStatusAsEnum() == OperationStatus.COMPLETED_OK,
				"Validate PLS call is successful while loading dispute_payout details");
		wtransactionList = plsResponse.getLegacyEquivalent().getWtransactions();
		Assert.assertNotNull(wtransactionList, "Verify wtransaction list is not empty");
		long currentTime = System.currentTimeMillis() / 1000;
		for (WTransactionVO txnRecord : wtransactionList) {
			if (txnRecord.getType() == Type.FEE.getByte()) {
				feeRecord = txnRecord;
			} else if (txnRecord.getAccountNumber().compareTo(recoupData.getPayoutFundingAccountNumber()) == 0
					&& txnRecord.getType() == Type.USERUSER.getByte()
					&& txnRecord.getSubtype() == Subtype.USERUSER_DISPUTED_PAYMENT.getByte()) {
				payoutRecord = txnRecord;
			}
		}
		Assert.assertNotNull(payoutRecord, "Verify Payout Transaction Details are loaded by PLS call");
		long payoutAmount = -payoutRecord.getAmount().getAmount();
		String payoutCurrency = payoutRecord.getAmount().getCurrencyCode();
		long minRecoupAmount = payoutAmount;
		recoupData.setRecoupScheduledDate(currentTime);
		recoupData.setTimeCreated(currentTime);
		recoupData.setRecoupAskAmount(payoutAmount);
		recoupData.setRecoupAskAmountCurr(payoutCurrency);
		if (recoupData.getWaiveFeePolicy() == WaiveFeePolicy.RECOUP_WAIVE_PP_ALL_FEE.getByte()) {
			Assert.assertNotNull(feeRecord, "Verify Fee Transaction details are not null");
			minRecoupAmount = payoutAmount + feeRecord.getAmount().getAmount();
		}
		recoupData.setMinRecoupAmount(minRecoupAmount);
		recoupData.setMinRecoupAmountCurr(payoutCurrency);
		recoupData.setRecoupStatus(RecoupStatus.RECOUP_STATUS_SCHEDULED.getByte());
		recoupData.setPyplTimeTouched(currentTime);
		Boolean status = wThirdPartyRecoupDAO.createRecoupRecord(recoupData) == 1;
		Assert.assertTrue(status, "Verify WthirdPartyRecoup record insertion is success");
		LOGGER.info("Recoup schedule status : {}", status);
	}
	
    private List<RecoupmentElementVO> populateRecoupList(WThirdPartyRecoupDTO recoupData,
            final RecoupRequest recoupRequest) {
		List<RecoupmentElementVO> recoupList = new ArrayList<RecoupmentElementVO>();
		RecoupmentElementVO recoupElement = new RecoupmentElementVO();
		recoupElement.setPayoutFundingAccount(recoupData.getPayoutFundingAccountNumber());
		recoupElement.setSellerAccount(recoupData.getSellerAccountNumber());
		recoupElement.setPayoutTransactionId(recoupData.getPayoutTransactionId());
		recoupElement.setRecoupmentScheduledDate(BigInteger.valueOf(recoupData.getRecoupScheduledDate()));
		recoupElement.setClientInvoiceId(recoupData.getInvoiceId());
		recoupElement.setForceFundsRecovery(false);

        if (recoupRequest.getDisputePayoutRecoupRequest().getRecoupmentElements() != null) {
            recoupElement.setForceFundsRecovery(recoupRequest.getDisputePayoutRecoupRequest().getRecoupmentElements()
                    .get(0).getForceFundsRecovery());
            recoupElement.setRecoupmentMinAmt(
                    recoupRequest.getDisputePayoutRecoupRequest().getRecoupmentElements().get(0).getRecoupmentMinAmt());
            recoupElement.setRecoupmentMaxAmt(
                    recoupRequest.getDisputePayoutRecoupRequest().getRecoupmentElements().get(0).getRecoupmentMaxAmt());
        }
        else {
            recoupElement.setRecoupmentMinAmt(
                    new Currency(recoupData.getMinRecoupAmountCurr(), recoupData.getMinRecoupAmount()));
            recoupElement.setRecoupmentMaxAmt(
                    new Currency(recoupData.getRecoupAskAmountCurr(), recoupData.getRecoupAskAmount()));
        }
		recoupList.add(recoupElement);
		return recoupList;
	}

}
