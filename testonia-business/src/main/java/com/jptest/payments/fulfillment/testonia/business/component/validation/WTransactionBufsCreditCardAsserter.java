package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.WTransactionBUFSVO;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * Validation: first transaction (from input list of transactions from PLS) has credit card as BUFS
 */
public class WTransactionBufsCreditCardAsserter extends BaseAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WTransactionBufsCreditCardAsserter.class);

    @Override
    public void validate(Context context) {
        List<WTransactionBUFSVO> wTransactionBufsList = (List<WTransactionBUFSVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_BUFS_LIST_KEY.getName());
        Assert.assertNotNull(wTransactionBufsList, "WTransactionBufs can not be null");
        Assert.assertFalse(wTransactionBufsList.isEmpty(), "WTransactionBufs can not be empty");
        WTransactionBUFSVO wtxnBufsVO = wTransactionBufsList.get(0);
        LOGGER.info("bufs type : {}, card byte:{}", wtxnBufsVO.getType(), WTransactionConstants.Type.CARD.getByte());
        if (wtxnBufsVO.getType().equals(WTransactionConstants.Type.CARD.getByte())
                && wtxnBufsVO.getAccountNumber().equals(getBuyerAccountNumber(context))) {
            LOGGER.info("Found BUFS WTransaction with card");
        } else {
            Assert.fail("Could not find BUFS WTransaction with card type");
        }
    }

    private BigInteger getBuyerAccountNumber(Context context) {
        User buyer = (User) getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
        return new BigInteger(buyer.getAccountNumber());
    }

}
