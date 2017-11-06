package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.List;
import javax.inject.Inject;
import org.testng.Assert;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * Validates payment stack
 */
public class PaymentStackAsserter extends BaseAsserter {

    public static final String P10_STACK = "P10";
    public static final String P20_STACK = "P20";
    public static final String P21_STACK = "P21";

    @Inject
    private TransactionHelper transactionHelper;

    private String expectedStack;

    public PaymentStackAsserter(String expectedStack) {
        this.expectedStack = expectedStack;
    }

    @Override
    public void validate(Context context) {
        List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        WTransactionVO recipientTransaction = transactionHelper.getRecipientTransaction(wTransactionList);

        Boolean isExpectedStack = false;
        switch (expectedStack) {
        case P10_STACK:
            isExpectedStack = transactionHelper.isP10Stack(recipientTransaction);
            break;
        case P20_STACK:
            isExpectedStack = transactionHelper.isP20Stack(recipientTransaction);
            break;
        default:
            isExpectedStack = transactionHelper.isCheetahStack(recipientTransaction);
            break;
        }

        Assert.assertTrue(isExpectedStack, "Stack isn't " + expectedStack);
    }

}
