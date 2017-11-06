package com.jptest.payments.fulfillment.testonia.business.component.user;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.bridge.UserBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.model.UserCreationTaskInput;
import com.jptest.qi.rest.domain.pojo.User;

public class UserLoadTask extends BaseTask<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoadTask.class);

    @Inject
    private UserBridge userBridge;

    private UserCreationTaskInput input;

    public UserLoadTask(UserCreationTaskInput input) {
        this.input = input;
    }

    @Override
    public User process(Context context) {
        String accountNumber = input.getUser().getAccountNumber();
        String emailAddress = input.getUser().getEmailAddress();
        User existingUser = new User();
        if (accountNumber != null) {
            existingUser = userBridge.getUser(accountNumber, true);
            LOGGER.info("Loaded user with account number " + accountNumber);
        } else if (emailAddress != null) {
            existingUser = userBridge.getUserByEmailAddress(emailAddress);
            LOGGER.info("Loaded user with email address " + emailAddress);
        }
        return existingUser;
    }

}
