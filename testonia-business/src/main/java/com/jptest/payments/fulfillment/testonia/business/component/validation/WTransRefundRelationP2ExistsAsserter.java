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
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransRefundRelationP2Dao;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;

/**
 * Checks whether txn exists in wtrans_refund_relation_p2 table
 * 
 */
public class WTransRefundRelationP2ExistsAsserter extends BaseAsserter implements TimeoutAwareComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(WTransRefundRelationP2ExistsAsserter.class);

    @Inject
    private RetriableRefundRelationP2ExistsCheckTask retriableTransactionExistsCheck;

    @Inject
    private Configuration config;
    
    @Inject
    private TransactionHelper transactionHelper;

    @Override
    public void validate(Context context) {
        @SuppressWarnings("unchecked")
		List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        if (CollectionUtils.isEmpty(wTransactionList)) {
            throw new TestExecutionException("No Transactions found");
        }
        
        final WTransactionVO senderTransaction = this.transactionHelper.getSenderTransaction(wTransactionList);
        LOGGER.info("Found Sender Transaction Id: {}", senderTransaction.getId());
        // wtrans_refund_relation_p2 table will not have records for 1.0 txns
        if (this.transactionHelper.isP10Stack(senderTransaction)) { 
        	return;
        }
        
        for (WTransactionVO transactionVO : wTransactionList) {
            LOGGER.info("TransactionId: {}, Type: {}, SubType: {}, Amount: {}, Flags5: {}",
                    new Object[] { transactionVO.getId().toString(), transactionVO.getType(), 
                    		transactionVO.getSubtype(), transactionVO.getAmount(), transactionVO.getFlags5() });
            if (transactionVO.getType() == WTransactionConstants.Type.REVERSAL.getByte() && 
            		transactionVO.getSubtype() == WTransactionConstants.Type.USERUSER.getByte()) {
            	boolean recordExists = retriableTransactionExistsCheck.execute(transactionVO);
	            if (!recordExists) {
	                LOGGER.error(
	                        "Unable to find transaction in WTrans_Refund_Relation_P2 table in database."
	                                + "Chances are that AMQ has not picked up the record for Async processing. "
	                                + "Please check activity status.");
	            }
	            Assert.assertTrue(recordExists, this.getClass().getSimpleName() + ".validate() - failed to validate "
	            		+ "whether wtrans_refund_relation_p2 record is persisted for " 
	            		+ transactionVO.getId().toString() + " with-in stipulated time");
            }
        }
    }

    private static class RetriableRefundRelationP2ExistsCheckTask extends RetriableTask<WTransactionVO, Boolean> {

        @Inject
        private WTransRefundRelationP2Dao wTransRefundRelationP2Dao;

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
                resultCount = wTransRefundRelationP2Dao.getTransactionRecordCount(transactionVO.getId().toString());
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
