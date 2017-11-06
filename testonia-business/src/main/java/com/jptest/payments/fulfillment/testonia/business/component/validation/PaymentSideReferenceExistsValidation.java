package com.jptest.payments.fulfillment.testonia.business.component.validation;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableBaseAsserter;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.dao.pymt.PaymentSideReferenceDao;


/**
 * Validates that holding_transaction is updated with credit records after payment is done
 */
public class PaymentSideReferenceExistsValidation extends RetriableBaseAsserter<Integer> {

    private String subBalanceHandle;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSideReferenceExistsValidation.class);

    @Inject
    private PaymentSideReferenceDao paymentSideReferenceDao;

    public PaymentSideReferenceExistsValidation(final String subBalanceHandle) {
        this.subBalanceHandle = subBalanceHandle;
    }

    public PaymentSideReferenceExistsValidation() {
        super(BizConfigKeys.TRANSACTION_EXISTS_CHECK_WAIT_TIME_IN_MS.getName(),
                BizConfigKeys.TRANSACTION_EXISTS_CHECK_RETRY_INTERVAL_IN_MS.getName());
    }

    /**
     * Continue to retry if output is not the desired one.
     */
    @Override
    public boolean isDesiredOutput(final Integer count) {
        return count != null && count != 0;
    }

    @Override
    public Integer retriableExecute(final Context context) {
        Integer count = null;
        if (this.subBalanceHandle != null) {
            count = this.paymentSideReferenceDao.getCountForHoldsHandle(this.subBalanceHandle);
        }
        return count;
    }

    @Override
    public void onSuccess(final Context context, final Integer output) {
        LOGGER.info("{} Payment Side Reference exists for given Subbalance handle input.", output);
    }

    @Override
    public void onFailure(final Context context, final Integer output) {
        LOGGER.warn("Unable to retrive Payment Side Reference exists for given Subbalance handle input. count:{}",
                output);
    }

}
