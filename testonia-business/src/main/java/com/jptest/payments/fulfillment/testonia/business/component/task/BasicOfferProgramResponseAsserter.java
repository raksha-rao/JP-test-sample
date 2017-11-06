package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.testng.Assert;
import com.jptest.interfaces.rs.resources.OfferProgramVO;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;


/**
@JP Inc.
 */
public class BasicOfferProgramResponseAsserter extends BaseAsserter {

    private String contextKey = null;

    public BasicOfferProgramResponseAsserter(String contextKey) {
        this.contextKey = contextKey;
    }

    @Override
    public void validate(final Context context) {
        final String METHOD_NAME = this.getClass().getSimpleName() + ".validate() - ";
        final OfferProgramVO offerProgramVO = (OfferProgramVO) this.getDataFromContext(context, contextKey);

        Assert.assertNotNull(offerProgramVO, METHOD_NAME + "OfferProgramVO");
        Assert.assertNotNull(offerProgramVO.getOfferProgramId(), METHOD_NAME + "OfferProgramId");
    }
}
