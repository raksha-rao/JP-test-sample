package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.TransactionAttributeVO;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableBaseAsserter;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionAttributesDao;

/**
 * Validates WTransaction attributes for sender
 */
public class WTransactionAttributesAsserter extends RetriableBaseAsserter<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WTransactionAttributesAsserter.class);

    @Inject
    private WTransactionAttributesDao wTransactionAttributesDao;

    @Inject
    private TransactionHelper transactionHelper;

    private List<TransactionAttributeVO> expectedAttributes;

    private Integer expectedTxnAttributesCount;

    public WTransactionAttributesAsserter(List<TransactionAttributeVO> expectedAttributes,
            int expectedTxnAttributesCount) {
        this.expectedAttributes = expectedAttributes;
        this.expectedTxnAttributesCount = expectedTxnAttributesCount;
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
        List<WTransactionVO> wTransactionList = (List<WTransactionVO>) this
                .getDataFromContext(context, ContextKeys.WTRANSACTION_LIST_KEY.getName());

        WTransactionVO senderTransaction = transactionHelper.getSenderTransaction(wTransactionList);
        context.addReportingAttribute(ReportingAttributes.SENDER_TRANSACTION_ACTIVITY_ID,
                senderTransaction.getActivityId());

        int outputCount = 0;
        for (TransactionAttributeVO attribute : expectedAttributes) {
            outputCount = wTransactionAttributesDao.getTransAttributesCount(senderTransaction.getActivityId(),
                    attribute.getAttributeNameAsEnum().getName());
        }
        return outputCount;
    }

    @Override
    public void onSuccess(Context context, Integer output) {
        LOGGER.info("WTransactionAttributesAsserter is success");
    }

    @Override
    public void onFailure(Context context, Integer output) {
        Assert.assertEquals(output, expectedTxnAttributesCount,
                this.getClass().getSimpleName()
                        + ".validateResponse() failed - Transaction Attributes count did not match");
    }

}
