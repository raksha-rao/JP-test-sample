package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.business.service.StoredValueServHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * This task will debit the given amount from the user's balance for 
 * the given currency. If the appropriate balance account is not present then one
 * will be created and the money will be debited from the newly created account. 
 * This task can be used for making user's balance negative.
 */
public class DebitHoldingAccountTask extends BaseTask<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebitHoldingAccountTask.class);

    @Inject
    private StoredValueServHelper svHelper;

    private DebitHoldingAccountTaskInput taskInput;

    public DebitHoldingAccountTask(DebitHoldingAccountTaskInput input) {
        taskInput = input;
    }

    @Override
    public Void process(Context context) {
        try {
            svHelper.debitMoneyFromBalance(getAccountNumber(taskInput.getUserContextKey(), context),
                    taskInput.getCurrencyCode(),
                    taskInput.getUnsignedAmount());
        } catch (Exception e) {
            LOGGER.error("Error while debiting money from user's balance", e);
            throw new TestExecutionException(e);
        }

        return null;
    }

    /**
     * @param userContextKey
     * @param context
     * @return
     */
    private String getAccountNumber(String userContextKey, Context context) {
        User user = (User) this.getDataFromContext(context, userContextKey);
        if (user == null)
            throw new TestExecutionException("Couldn't find user object in context");
        return user.getAccountNumber();
    }

    public static class DebitHoldingAccountTaskInput {
        private String currencyCode;
        private String unsignedAmount;
        private String userContextKey;

        /**
         * @param currency
         * @param unsignedAmtInDefaultNativeRepresentaion This value will be relayed as it is to StoredValueServ which
         * treats this value as the default representation for the given currency. E.g. for USD the value "10" will be treated as
         * $10 (10 dolloars) where as if the currency code is bitcoin, it will be treated as the 8 deimal points value. So please 
         * make sure to pass the value correctly (e.g. in Dollar for USD Not in cents)
         * Also since the operation is debit, this amount can be absolute value "10" (shouldn't be "-10")
         * @param buyerVoKey
         */
        public DebitHoldingAccountTaskInput(String currency, String unsignedAmtInDefaultNativeRepresentaion,
                String userContextKey) {
            this.currencyCode = currency;
            this.unsignedAmount = unsignedAmtInDefaultNativeRepresentaion;
            this.userContextKey = userContextKey;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getUnsignedAmount() {
            return unsignedAmount;
        }

        public String getUserContextKey() {
            return userContextKey;
        }

    }

}
