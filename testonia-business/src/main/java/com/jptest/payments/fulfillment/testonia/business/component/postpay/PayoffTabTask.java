package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.common.ActorInfoVO;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.FulfillPayoffTabRequest;
import com.jptest.money.FulfillPayoffTabResponse;
import com.jptest.money.PayoffTabFulfillmentPlanVO;
import com.jptest.money.PayoffTabFulfillmentResultType;
import com.jptest.money.PayoffTabStrategyVO;
import com.jptest.money.PlanPaymentV2Response;
import com.jptest.money.PlanPayoffTabV2Request;
import com.jptest.money.PlanPayoffTabV2Response;
import com.jptest.payments.fulfillment.testonia.bridge.FiManagementServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.MoneyplanningServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.vo.money.ActorInfoVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.PayoffTabStrategyVOBuilder;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.cloc.WUserAchDAO;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.FulfillPayoffTabWrapperRequest;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;

/**
 * PayoffTabTask takes care of constructing FulfillPayoffTabRequest. To build
 * this request, a PlanPayoffTabV2 request had to be requested from MPS.
 *
 * @JP Inc.
 */
public class PayoffTabTask
		extends BasePostPaymentOperationTask<FulfillPayoffTabWrapperRequest, FulfillPayoffTabResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PayoffTabTask.class);

	private Currency payoffAmount;

	@Inject
	private PaymentServBridge paymentServBridge;

	@Inject
	MoneyplanningServBridge mpsBridge;

	@Inject
	private FiManagementServBridge fiBridge;

	@Inject
	private WUserAchDAO wUserAchDao;

	@Inject
	private WTransactionP20DaoImpl wTransactionP2DAO;

	@Override
	public FulfillPayoffTabWrapperRequest constructPostPayRequest(final FulfillPayoffTabWrapperRequest request,
			final Context context) {

		this.payoffAmount = request.getPayOffAmount().getCurrency();

		String paymentResponseKey = ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName();
		if (this.getDataFromContext(context, paymentResponseKey) instanceof PlanPaymentV2Response) {
			paymentResponseKey = ContextKeys.FULFILL_PAYMENT_RESPONSE_KEY.getName();
		}
		final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
				paymentResponseKey);
		String transactionHandle = fulfillResponse.getTransactionUnitStatus().get(0).getHandleDetails()
				.getDebitSideHandle();

		final User sender = (User) getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
		final BigInteger senderAccountNumber = new BigInteger(sender.getAccountNumber());

		final ActorInfoVOBuilder actorInfo = ActorInfoVOBuilder.newBuilder(senderAccountNumber);
		final ActorInfoVO actroInfoVO = actorInfo.build();

		FulfillPayoffTabRequest payoffTabRequest = new FulfillPayoffTabRequest();
		payoffTabRequest.setActorInfo(actroInfoVO);
		payoffTabRequest.setIdempotenceId(this.paymentServBridge.createActivityId().toString());
		payoffTabRequest.setPayoffPlan(getPayOffPlan(actroInfoVO, senderAccountNumber, transactionHandle));
		payoffTabRequest.setPlanningActivityHandle("ASDF");

		request.setFulfillRequest(payoffTabRequest);

		return request;
	}

	@Override
	public FulfillPayoffTabResponse executePostPay(final FulfillPayoffTabWrapperRequest payoffTabRequest,
			final boolean call2PEX) {
		LOGGER.info("executePostPay:");
		Assert.assertNotNull(payoffTabRequest);

		return this.paymentServBridge.fulfillPayoffTab(payoffTabRequest.getFulfillRequest());
	}

	@Override
	public void assertPostPayResponse(FulfillPayoffTabResponse payOffTabResponse, PostPaymentRequest postPayRequest,
			Context context) {
		// validate response
		Assert.assertNotNull(payOffTabResponse, "FulfillPayoffTabResponse should not be null");
		Assert.assertEquals(payOffTabResponse.getResultAsEnum(), PayoffTabFulfillmentResultType.SUCCESS,
				this.getClass().getSimpleName() + ".assertPostPayResponse() failed: Not SUCCESS");

		// validate transaction
		Assert.assertEquals(payOffTabResponse.getPaidOffAmount(), payoffAmount,
				".assertPostPayResponse() failed: Amount mismatched");

		LOGGER.info("assertPostPayResponse: FulfillPayoffTab_TransactionHandle={}",
				payOffTabResponse.getTransactionHandle());
	}

	/**
	 * Prepare the payoff-tab plan required for fulfillment request
	 *
	 * @param actroInfoVO
	 * @param accountNumber
	 * @param transactionHandle
	 * @return
	 */
	protected PayoffTabFulfillmentPlanVO getPayOffPlan(final ActorInfoVO actroInfoVO, final BigInteger accountNumber,
			final String transactionHandle) {

		String achId = this.wUserAchDao.getAchId(accountNumber.toString());
		String tabIdData = fiBridge.getTabAccountId(accountNumber.toString());

		List<BigInteger> transactionIdList = wTransactionP2DAO.getTransactionIdByAccountNumberAndTypeAndStatus(
				accountNumber, String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
				String.valueOf(WTransactionConstants.Status.SUCCESS.getValue()));

		final PayoffTabStrategyVOBuilder strategyVOBuilder = new PayoffTabStrategyVOBuilder()
				.senderAccountNumber(accountNumber).encryptedActivityId(transactionHandle).achId(achId)
				.transactionId(transactionIdList.get(0)).tabId(new BigInteger(tabIdData)).amount(payoffAmount);
		final PayoffTabStrategyVO strategyVO = strategyVOBuilder.build();

		PlanPayoffTabV2Request planningRequest = new PlanPayoffTabV2Request();
		planningRequest.setActorInfo(actroInfoVO);
		planningRequest.setStrategy(strategyVO);

		PlanPayoffTabV2Response planningResponse = mpsBridge.planPayoffTabV2(planningRequest);
		if (planningResponse.getAvailableFulfillmentPlans().isEmpty()) {
			LOGGER.info("executePostPay: Failed to create PlanPayoffTabV2");
			throw new TestExecutionException("Failed to create PlanPayoffTabV2");
		}

		return planningResponse.getDefaultFulfillmentPlan();
	}
}
