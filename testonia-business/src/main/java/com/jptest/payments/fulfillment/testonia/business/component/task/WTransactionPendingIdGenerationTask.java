package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * This task queries the TXN db and finds out the base id for the 
 * pending transaction based on the seller's account number.
 * This task keeps polling the WTRANSACTION_P2 table for row type "U" and status "P" (pending) 
*/
public class WTransactionPendingIdGenerationTask extends BaseTask<String>
        implements TimeoutAwareComponent {

    @Inject
    private RetriablePendingIdGenerationTask innerTask;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHoldReleaseTask.class);

    @Override
    public String process(Context context) {

        User seller = (User) getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());
        String accountNumber = seller.getAccountNumber();

        String pendingId = innerTask.execute(accountNumber);
        LOGGER.debug("pending id for the seller is: {}", pendingId);
        return pendingId;
    }

    private static class RetriablePendingIdGenerationTask extends RetriableTask<String, String> {

        @Inject
        private WTransactionP20DaoImpl wTransactionP20Dao;

        @Override
        public boolean isDesiredOutput(String output) {
			return output != null;
        }

        @Override
        public String retriableExecute(String input) {
            return wTransactionP20Dao.getBaseIdForPendingTransaction(input);
        }

        @Override protected String onSuccess(String input, String output) {
            return output;
        }

        @Override protected String onFailure(String input, String output) {
            return output;
        }

    }

    @Override
    public long getTimeoutInMs() {
        return 60000;
    }

}
