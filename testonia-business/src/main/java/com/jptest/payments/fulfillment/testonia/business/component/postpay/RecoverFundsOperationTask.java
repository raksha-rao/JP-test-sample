package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.financialinstrument.GetFisByAccountResponse;
import com.jptest.money.FundingMethodType;
import com.jptest.money.FundingSourceVO;
import com.jptest.money.InstrumentIdentifierVO;
import com.jptest.money.LegacyTable;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.RecoverFundsRequest;
import com.jptest.money.RecoverFundsResponse;
import com.jptest.money.RecoverFundsResultType;
import com.jptest.money.UserIdentifierVO;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.OperationStatus;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.ReadDepth;
import com.jptest.payments.fulfillment.testonia.bridge.FinInstReadServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentReadServRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.business.vo.money.ActorInfoVOBuilder;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants.Subtype;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants.Type;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.test.money.constants.WTransactionConstants;


/**
 * RecoverFundsOperationTask takes care of constructing the recover_funds
 * request , executes and validates the response
 *
 * @JP Inc.
 */
public class RecoverFundsOperationTask extends BasePostPaymentOperationTask<RecoverFundsRequest, RecoverFundsResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecoverFundsOperationTask.class);

	@Inject
	private PaymentServBridge paymentServBridge;
	@Inject
	private PostPaymentExecService postPaymentService;
	@Inject
	private PaymentReadServBridge plsBridge;
	@Inject
	private TransactionHelper transactionHelper;
	@Inject
	private FinInstReadServBridge fiBridge;
	@Inject
	private PaymentReadServRetrieverTask paymentReadServRetrieverTask;
	@Inject
	private PopulateActivityIdHelper populateActivityIdHelper;

	@Override
	public RecoverFundsRequest constructPostPayRequest(final RecoverFundsRequest request, final Context context) {

		final BigInteger activityId = this.paymentServBridge.createActivityId();
		final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
		operationIdempotencyVO.setActivityId(activityId);

		LOGGER.info("***************Building FirstRepresentmentRequest request***************");
		Object object = this.getDataFromContext(context, ContextKeys.WTRANSACTION_ID_KEY.getName());
		Assert.assertNotNull(object, "Verify txn_id / TUH is available in context");
		final User buyer = (User) this.getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
		final BigInteger buyerAccountNumber = new BigInteger(buyer.getAccountNumber());
		final GetLegacyEquivalentByPaymentReferenceResponse plsResponse = this.loadTransactionDetails((String) object);
		Assert.assertTrue(plsResponse.getStatusAsEnum() == OperationStatus.COMPLETED_OK,
				"Validate PLS call is successful while loading dispute_payout details");
		List<WTransactionVO> wtransactionList = plsResponse.getLegacyEquivalent().getWtransactions();
		WTransactionVO depositRecord = null;
		WTransactionVO buyerTransRecord = null;
		for (WTransactionVO wTransactionVO : wtransactionList) {
			if (wTransactionVO.getAccountNumber().compareTo(buyerAccountNumber) == 0) {
				if (depositRecord == null && wTransactionVO.getType() == Type.ACHDEPOSIT.getByte()
						&& wTransactionVO.getSubtype() != Subtype.ACHDEPOSIT_AFS.getByte()) {
					depositRecord = wTransactionVO;
				} else if (buyerTransRecord == null && wTransactionVO.getType() == Type.USERUSER.getByte()) {
					buyerTransRecord = wTransactionVO;
				}
			}
		}
		Assert.assertNotNull(depositRecord, "Verify funding 'H' row is loaded by PRS call");
		Assert.assertFalse(transactionHelper.isP10Stack(depositRecord),
				"Validate transaction is not a 1.0 transaction");

		request.setPaymentHandle(buyerTransRecord.getEncryptedId());
		request.setFundingPlanInfo(buildFundingPlanInfo(buyerTransRecord, depositRecord));
		request.setActorInfo(ActorInfoVOBuilder.newBuilder(buyerAccountNumber).actorId(buyerAccountNumber).build());

		return request;

	}

	@Override
	public RecoverFundsResponse executePostPay(final RecoverFundsRequest request, final boolean call2PEX) {
		Assert.assertNotNull(request);

		return this.postPaymentService.recoverFundsSerice(request, call2PEX);
	}

	@Override
	public void assertPostPayResponse(final RecoverFundsResponse recoverFundsResponse,
			final PostPaymentRequest postPayRequest, final Context context) {
		Assert.assertNotNull(recoverFundsResponse, "post payment response should not be null");
		LOGGER.info("recover_funds api response {} ", recoverFundsResponse.getResultAsEnum());
		Assert.assertTrue(recoverFundsResponse.getResultAsEnum() == RecoverFundsResultType.RESULT_OK,
				"Validate recover_funds call is success");

		LOGGER.info("Recover Funds Operation Passed");
	}

	@Override
	protected void populateActivityId(final Context context, final RecoverFundsResponse postPayResponse) {
		GetLegacyEquivalentByPaymentReferenceResponse prsResponse = paymentReadServRetrieverTask.execute(context);
		List<WTransactionVO> wTransactionVOList = prsResponse.getLegacyEquivalent().getWtransactions();

		BigInteger transactionID = BigInteger.ZERO;
		String encryptedTxnId = null;

		for (WTransactionVO wTransactionVO : wTransactionVOList) {
			if (wTransactionVO.getType() == WTransactionConstants.Type.ACHDEPOSIT.getValue() &&
					wTransactionVO.getSubtype() == WTransactionConstants.Subtype.ACHDEPOSIT_RECOVERY_ADD_FUNDS.getByte()) {
				transactionID = wTransactionVO.getId();
				encryptedTxnId = wTransactionVO.getEncryptedId();
				break;
			}
		}

		if (encryptedTxnId != null) {
			context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), encryptedTxnId);
		}

		final BigInteger activityId = populateActivityIdHelper.getLatestActivityIdForReferenceValue(
				transactionID.toString());
		Assert.assertNotNull(activityId, this.getClass().getSimpleName() + "activityId should not be null");
		context.setData(this.getActivityIdKey(), activityId);

		LOGGER.info("Recover Funds - Encrypted Txn Id - {}", encryptedTxnId);
		LOGGER.info("RecoverFundsResponse Operation: Activity ID - {}, Transaction ID - {} - {}", activityId,
				transactionID, this.getActivityIdKey());
	}

	private GetLegacyEquivalentByPaymentReferenceResponse loadTransactionDetails(final String referenceValue) {
		final GetLegacyEquivalentByPaymentReferenceRequest request = new GetLegacyEquivalentByPaymentReferenceRequest();
		final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
		paymentSideReferenceVO.setReferenceValue(referenceValue);
		paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
		request.setPaymentReference(paymentSideReferenceVO);
		final List<LegacyTable> legacyTable = new ArrayList<LegacyTable>();
		legacyTable.add(LegacyTable.WTRANSACTION);
		request.setRequestedLegacyTablesAsEnum(legacyTable);
		request.setMinimumReadDepth(ReadDepth.WHOLE_PAYMENT_TREE);
		request.setRequireRiskDecision(false);
		return this.plsBridge.getLegacyEquivalentByPaymentReference(request);
	}

	private FundingSourceVO buildFundingPlanInfo(final WTransactionVO buyerTransaction,
			final WTransactionVO depositRecord) {
		FundingSourceVO fundingPlanInfo = new FundingSourceVO();

		final GetFisByAccountResponse getFisByAccountResponse = fiBridge
				.getFisByAccount(buyerTransaction.getAccountNumber(), depositRecord.getAchId().toString());

		if (!getFisByAccountResponse.getSuccess()) {
			Assert.fail("Account not found for accountNumber " + buyerTransaction.getAccountNumber());
		}

		final InstrumentIdentifierVO instrumentIdentifierVO = new InstrumentIdentifierVO();
		instrumentIdentifierVO.setWalletInstrumentId(getFisByAccountResponse.getInstruments().getInstrument().get(0));
		fundingPlanInfo.setInstrument(instrumentIdentifierVO);
		fundingPlanInfo.setTotalFundsIn(depositRecord.getAmount());
		fundingPlanInfo.setAllowOverdraft(false);
		fundingPlanInfo.setFundingMethod(FundingMethodType.ECHECK);

		final UserIdentifierVO userIdentifierVO = new UserIdentifierVO();
		userIdentifierVO.setAccountNumber(depositRecord.getAccountNumber());
		userIdentifierVO.setInstrumentIdentifier(instrumentIdentifierVO);
		fundingPlanInfo.setUser(userIdentifierVO);

		return fundingPlanInfo;
	}

}
