package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.exception.TransactionNotPresentException;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.HoldingBalanceDao;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;

/**
 * Validates holding balance record exist in database
 */
public class HoldingBalanceExistsAsserter extends BaseAsserter implements TimeoutAwareComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(HoldingBalanceExistsAsserter.class);

    @Inject
    private RetriableHoldingBalanceExistsCheckTask retriableHoldingBalanceExistsCheck;

    @Inject
    private Configuration config;
    
    @Inject
    private TransactionHelper transactionHelper;
    
    private static final String HOLDING_BALANCE_TYPE_PENDING_FUNDS = "P";

    @Override
    public void validate(Context context) {
        @SuppressWarnings("unchecked")
        final List<WTransactionVO> wTransactionList = (List<WTransactionVO>) this.getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());

        if (CollectionUtils.isEmpty(wTransactionList)) {
            throw new TransactionNotPresentException("No Transactions found");
        }
        final WTransactionVO senderTransaction = this.transactionHelper.getSenderTransaction(wTransactionList);
        LOGGER.info("Found Sender Transaction Id: {}", senderTransaction.getId());
        
        boolean recordExists = true;
        if ((char) senderTransaction.getStatus().byteValue() == WTransactionConstants.Status.PENDING.getValue()) {
        	recordExists = retriableHoldingBalanceExistsCheck.execute(senderTransaction);
        }
        if (!recordExists) {
            LOGGER.error("Unable to find pending funds row in HOLDING_BALANCE table in database. "
            		+ "Chances are that stored value service did not invoke credit_funds operation. "
            		+ "Please check CAL logs.");
        }
        Assert.assertTrue(recordExists, this.getClass().getSimpleName() + ".validate() - failed to validate "
        		+ "whether holding_balance record is persisted for " + senderTransaction.getId().toString() 
        		+ " with-in stipulated time");
    }

    private static class RetriableHoldingBalanceExistsCheckTask extends RetriableTask<WTransactionVO, Boolean> {

        @Inject
        private HoldingBalanceDao holdingBalanceDao;

        @Inject
        private Configuration config;

        @Inject
        private void init() {
            setMaxWaitInMs(config.getLong(BizConfigKeys.HOLDING_BALANCE_EXISTS_CHECK_WAIT_TIME_IN_MS.getName()));
            setRepeatIntervalInMs(
                    config.getLong(BizConfigKeys.HOLDING_BALANCE_EXISTS_CHECK_RETRY_INTERVAL_IN_MS.getName()));
        }

        /**
         * Continue to retry if output is not the desired one.
         * The method returns output - which is same as saying "output == true"
         */
        @Override
        protected boolean isDesiredOutput(Boolean output) {
            return output;
        }

        @Override
        protected Boolean retriableExecute(WTransactionVO wTransactionVO) {
            Integer resultCount = null;
            if ((char) wTransactionVO.getStatus().byteValue() == WTransactionConstants.Status.PENDING.getValue()) {
	            resultCount = holdingBalanceDao.getCount(wTransactionVO.getAccountNumber().toString(), 
	            		HOLDING_BALANCE_TYPE_PENDING_FUNDS);
            }
            return resultCount != null && resultCount == 1;
        }

        @Override
        protected Boolean onSuccess(WTransactionVO input, Boolean output) {
            return output;
        }

        @Override
        protected Boolean onFailure(WTransactionVO input, Boolean output) {
            return false;
        }

    }

    @Override
    public long getTimeoutInMs() {
        long timeout = config.getLong(BizConfigKeys.TRANSACTION_EXISTS_CHECK_WAIT_TIME_IN_MS.getName());
        long interval = config.getLong(BizConfigKeys.TRANSACTION_EXISTS_CHECK_RETRY_INTERVAL_IN_MS.getName());
        return (timeout + interval);
    }

}
