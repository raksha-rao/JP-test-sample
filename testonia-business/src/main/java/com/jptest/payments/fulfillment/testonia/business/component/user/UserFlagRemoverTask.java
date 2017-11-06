package com.jptest.payments.fulfillment.testonia.business.component.user;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.inject.Inject;

import com.jptest.payments.fulfillment.testonia.bridge.UserBridge;
import com.jptest.payments.fulfillment.testonia.business.service.UserTaskHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.qi.rest.domain.pojo.UserFlag;
import com.jptest.qi.rest.domain.pojo.UserFlagList;

/**
 * Task to remove list of flags for given user
 * User is input by Function<Context, User>
 */
public class UserFlagRemoverTask extends BaseTask<Void> {

    @Inject
    private UserBridge userBridge;

    @Inject
    private UserTaskHelper userTaskHelper;

    private List<String> userFlagsToBeRemoved;

    private Function<Context, User> getUserFunction;

    public UserFlagRemoverTask(Function<Context, User> getUserFunction, List<String> userFlagsToBeRemoved) {
        this.getUserFunction = getUserFunction;
        this.userFlagsToBeRemoved = userFlagsToBeRemoved;
    }

    @Override
    public Void process(Context context) {
        final User seller = getUserFunction.apply(context);
        String accountNumber = seller.getAccountNumber();
        UserFlagList clearFlagList = getUserFlags(userFlagsToBeRemoved);
        userBridge.clearFlags(accountNumber, clearFlagList);
        return null;
    }

    /**
     * @param flags
     * @return
     */
    private UserFlagList getUserFlags(List<String> flags) {
        List<UserFlag> userFlags = new ArrayList<UserFlag>(flags.size());
        for (String flag : flags) {
            userFlags.add(userTaskHelper.getUserFlag(flag));
        }
        return new UserFlagList(userFlags);
    }

}
