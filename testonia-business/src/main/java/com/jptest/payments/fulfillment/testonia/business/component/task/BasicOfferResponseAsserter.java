package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.testng.Assert;
import com.jptest.interfaces.rs.resources.OfferVO;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;


/**
@JP Inc.
 */
public class BasicOfferResponseAsserter extends BaseAsserter {

    private String contextKey = null;

    public BasicOfferResponseAsserter(String contextKey) {
        this.contextKey = contextKey;
    }

    @Override
    public void validate(final Context context) {
        final String METHOD_NAME = this.getClass().getSimpleName() + ".validate() - ";
        final OfferVO offerVO = (OfferVO) this.getDataFromContext(context, contextKey);

        Assert.assertNotNull(offerVO, METHOD_NAME + "OfferVO");
        Assert.assertNotNull(offerVO.getOfferId(), METHOD_NAME + "OfferId");
    }
}
