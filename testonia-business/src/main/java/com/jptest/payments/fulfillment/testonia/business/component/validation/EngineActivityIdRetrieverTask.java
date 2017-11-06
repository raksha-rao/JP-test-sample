package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.math.BigInteger;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Inject;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableBaseTask;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.eng.FulfillmentActivityDao;
import com.jptest.payments.fulfillment.testonia.dao.pymt.MoneyMovementDao;
import com.jptest.payments.fulfillment.testonia.dao.pymt.PaymentSideReferenceDao;
import com.jptest.payments.fulfillment.testonia.dao.txn.EventOsAlternateIdDao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.test.money.constants.WTransactionConstants;

/**
 * Retrieves Engine ActivityId based on the transaction id. This is a retriable
 * task. Retries/waits until the activity id is retrieved for a particular
 * operation.
 */
public class EngineActivityIdRetrieverTask extends RetriableBaseTask<BigInteger> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EngineActivityIdRetrieverTask.class);
    @Inject
    private PaymentSideReferenceDao paymentSideReferenceDao;

	@Inject
	private EventOsAlternateIdDao eventOsAlternateIdDao;

	@Inject
	private FulfillmentActivityDao fulfillmentActivityDao;

    @Inject
    private WTransactionP20DaoImpl wtransactionDao;

    @Inject
    private MoneyMovementDao moneyMovementDao;

    private static final int ASYNC_COMPLETE = 2;

    private final BigInteger transactionId;

	public EngineActivityIdRetrieverTask(BigInteger transactionId) {
		super();
		this.transactionId = transactionId;
	}

	/**
	 * Calls PYMT or TXN DAO to retrieve activity ID
	 */

	private BigInteger getActivityId(Context context) {

		BigInteger activityId = this.paymentSideReferenceDao
				.getActivityIdFromTransactionId(this.transactionId.toString());
		if (activityId == null) {
			activityId = this.eventOsAlternateIdDao.getActivityIdFromTransactionId(this.transactionId.toString());
            final WTransactionDTO wtransaction = this.wtransactionDao.getTransactionDetails(this.transactionId);
            if (activityId == null && wtransaction != null && this.isTransactionOfType5(wtransaction)) {
                final BigInteger paymentId = this.moneyMovementDao.findPaymentSideId(this.transactionId);
                activityId = this.paymentSideReferenceDao.getActivityIdFromTransactionId(paymentId.toString());
            }
        }
        if (activityId != null && isAsyncCompleteCheckRequired(context)) {
            new AsyncCompleteRetriableTask(this.fulfillmentActivityDao).execute(activityId);
        }
        return activityId;
	}

    /**
     * If skipAsyncCompletionCheck is mentioned as true in PostPaymentRequest in input json, this function will skip
     * check for ASYNC_COMPLETE status for an Activity, e.g for boleto cases we dont allow reversal after a payment
     * completion.
     *
     * @param context
     * @return boolean - true or false
     */

    private boolean isAsyncCompleteCheckRequired(Context context) {
        PostPaymentRequest postPaymentRequest = (PostPaymentRequest) context
                .getData(ContextKeys.POST_PAYMENT_REQUEST_KEY.getName());
        return !(postPaymentRequest != null
                && postPaymentRequest.isSkipAsyncCompletionCheck());
    }

    // This method to check the wtransaction table of type column is 5
    protected boolean isTransactionOfType5(final WTransactionDTO wtransaction) {
        boolean ret = false;
        final Byte type5 = WTransactionConstants.Type.PAYABLE.getByte();
        final Byte type = wtransaction.getType();
        if (type.equals(type5)) {
            LOGGER.info("The transaction: {} is of type5", wtransaction.getId());
            ret = true;
        }

        return ret;
    }

	@Override
	protected boolean isDesiredOutput(BigInteger output) {
		return output != null;
	}

	@Override
	protected BigInteger retriableExecute(Context context) {
		return this.getActivityId(context);
	}

	@Override
	protected BigInteger onSuccess(Context context, BigInteger output) {
		return output;
	}

	@Override
	protected BigInteger onFailure(Context context, BigInteger output) {
		return null;
	}

	public static class AsyncCompleteRetriableTask extends RetriableTask<BigInteger, Boolean> {

		private final FulfillmentActivityDao fulfillmentActivityDao;

		public AsyncCompleteRetriableTask(FulfillmentActivityDao fulfillmentActivityDao) {
			super();
			this.fulfillmentActivityDao = fulfillmentActivityDao;
		}

		@Override
		protected boolean isDesiredOutput(Boolean output) {
			return output;
		}

		@Override
		protected Boolean retriableExecute(BigInteger input) {
			return this.checkAsyncCompleteStatusForActivityId(input);
		}

		@Override
		protected Boolean onSuccess(BigInteger input, Boolean output) {
			return output;
		}

		@Override
		protected Boolean onFailure(BigInteger input, Boolean output) {
			throw new TestExecutionException("Timed out before ASYNC_COMPLETE");
		}

		/**
		 * Calls PYMT or TXN DAO to retrieve activity ID
		 */

		private boolean checkAsyncCompleteStatusForActivityId(BigInteger activityId) {

			final List<FulfillmentActivityDTO> fulfillmentActivityIds = this.fulfillmentActivityDao
					.getRecordsByActivityId(activityId);

			for (final FulfillmentActivityDTO faDto : fulfillmentActivityIds) {
				if (faDto.getStatus() == ASYNC_COMPLETE) {
					return true;

				}

			}
			return false;
		}

	}

}
