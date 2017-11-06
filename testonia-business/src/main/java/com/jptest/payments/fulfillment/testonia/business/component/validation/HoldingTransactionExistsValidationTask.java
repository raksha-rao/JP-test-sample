package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableBaseAsserter;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.HoldingTransactionDao;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * Validates that holding_transaction is updated with credit records after payment is done
 */
public class HoldingTransactionExistsValidationTask extends RetriableBaseAsserter<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HoldingTransactionExistsValidationTask.class);

    @Inject
    private HoldingTransactionDao holdingTransactionDao;
    
    @Inject
    private TransactionHelper transactionHelper;

    public HoldingTransactionExistsValidationTask() {
        super(BizConfigKeys.HOLDING_TRANSACTION_EXISTS_CHECK_WAIT_TIME_IN_MS.getName(),
                BizConfigKeys.HOLDING_TRANSACTION_EXISTS_CHECK_RETRY_INTERVAL_IN_MS.getName());
    }

    /**
     * Continue to retry if output is not the desired one.
     */
    @Override
    public boolean isDesiredOutput(Integer count) {
        return count != null && count != 0;
    }

    @Override
    public Integer retriableExecute(Context context) {
    	@SuppressWarnings("unchecked")
    	List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
    			ContextKeys.WTRANSACTION_LIST_KEY.getName());
    	final WTransactionVO senderTransaction = this.transactionHelper.getSenderTransaction(wTransactionList);
    	LOGGER.info("Found Sender Transaction Id: {}", senderTransaction.getId());
    	// holding_transaction table will not have records for pending txns
    	if (senderTransaction.getStatus().byteValue() == (byte)WTransactionConstants.Status.PENDING.getValue()) {
    		// Mocking output value (count) for pending txns to reflect count of 1 since pending txns 
    		// will not have entry in this table
    		return 1;
    	}

        final User seller = (User) this.getDataFromContext(context,
                ContextKeys.SELLER_VO_KEY.getName());
        Integer count = null;
        if (seller.getAccountNumber() != null) {
            count = this.holdingTransactionDao.getCount(seller.getAccountNumber());

        }
        return count;
    }

    @Override
    public void onSuccess(Context context, Integer output) {
        LOGGER.info("{} holding transactions exists for given input.", output);
    }

    @Override
    public void onFailure(Context context, Integer output) {
        LOGGER.warn("Unable to retrive holding transactions for given input. count:{}", output);
    }

}
