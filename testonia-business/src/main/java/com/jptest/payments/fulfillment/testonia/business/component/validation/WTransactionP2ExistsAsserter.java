package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.exception.TransactionNotPresentException;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.IwTransactionDao;

/**
 * Validates that input list of WTransactionVOs exist in database
 */
public class WTransactionP2ExistsAsserter extends BaseAsserter
        implements TimeoutAwareComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(WTransactionP2ExistsAsserter.class);

    @Inject
    private RetriableTransactionExistsCheckTask retriableTransactionExistsCheck;

    @Inject
    private Configuration config;

    @Override
    public void validate(Context context) {
        List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        if (CollectionUtils.isEmpty(wTransactionList)) {
            throw new TestExecutionException("No Transactions found");
        }
        for (WTransactionVO transactionVO : wTransactionList) {
            LOGGER.info("TransactionId: {}, Type: {}, Amount: {}, Flags5: {}",
                    new Object[] { transactionVO.getId().toString(), transactionVO.getType(), transactionVO.getAmount(),
                            transactionVO.getFlags5() });
            boolean recordExists = retriableTransactionExistsCheck.execute(transactionVO);
            if (!recordExists) {
                throw new TransactionNotPresentException(
                        "Unable to find transaction " + transactionVO.getId() + " in WTransaction_P2 table in database."
                                + "Chances are that AMQ has not picked up the record for Async processing. Please check activity status.");
            }
        }
    }

    private static class RetriableTransactionExistsCheckTask extends RetriableTask<WTransactionVO, Boolean> {

        @Inject
        @Named("WTransactionDao")
        private IwTransactionDao transactionDao;

        @Inject
        private Configuration config;

        @Inject
        private void init() {
            setMaxWaitInMs(config.getLong(BizConfigKeys.TRANSACTION_EXISTS_CHECK_WAIT_TIME_IN_MS.getName()));
            setRepeatIntervalInMs(
                    config.getLong(BizConfigKeys.TRANSACTION_EXISTS_CHECK_RETRY_INTERVAL_IN_MS.getName()));
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
        protected Boolean retriableExecute(WTransactionVO transactionVO) {
            Integer resultCount = null;
            if (transactionVO != null) {
                resultCount = transactionDao.getTransactionRecordCount(transactionVO.getId().toString());
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
