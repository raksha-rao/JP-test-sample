package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.util.List;
import javax.inject.Inject;
import com.jptest.payments.fulfillment.testonia.bridge.RiskAdminServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.user.UserCreationTask;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.risk.fmf.BaseFMFData;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * Sets given FMF filters to a given user. FMF filter setting during user creation is taken care by
 * {@link UserCreationTask}. This task is useful where FMF filters have to be set after the user creation, say after
 * performing a payment.
 *
 * @JP Inc.
 */

public class FMFfilterSetterTask extends BaseTask<Object> {

    @Inject
    private RiskAdminServBridge fmfBridge;

    private ContextKeys userIdentifierKey;

    private List<? extends BaseFMFData> fmfFilters;

    /**
     * @param userIdentifierKey
     *            Conetext Key to identify the user. Ex:ContextKeys.SELLER_VO_KEY
     * @param fmfFilters
     *            FMF filters to set for the user
     */
    public FMFfilterSetterTask(final ContextKeys userIdentifierKey, final List<? extends BaseFMFData> fmfFilters) {

        this.userIdentifierKey = userIdentifierKey;
        this.fmfFilters = fmfFilters;
    }

    @Override
    public Object process(final Context context) {

        final User actualUser = (User) this.getDataFromContext(context, this.userIdentifierKey.getName());

        this.fmfBridge.addFMFFilters(actualUser, this.fmfFilters);

        return null;
    }

}
