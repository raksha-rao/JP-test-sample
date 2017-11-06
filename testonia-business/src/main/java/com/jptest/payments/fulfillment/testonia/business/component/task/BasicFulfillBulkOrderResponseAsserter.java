package com.jptest.payments.fulfillment.testonia.business.component.task;

import com.jptest.money.FulfillBulkOrderResponse;
import com.jptest.money.FulfillmentRequestStatus;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
@JP Inc.
 */
public class BasicFulfillBulkOrderResponseAsserter extends BaseAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicFulfillBulkOrderResponseAsserter.class);

    @Override
    public void validate(final Context context) {

        final FulfillBulkOrderResponse fulfillBulkOrderResponse = (FulfillBulkOrderResponse) this.getDataFromContext
                (context, ContextKeys.FULFILL_BULKORDER_RESPONSE_KEY.getName());

        Assert.assertNotNull(fulfillBulkOrderResponse, "fulfillBulkOrderResponse should not be NULL");
        Assert.assertEquals(fulfillBulkOrderResponse.getStatusAsEnum(), FulfillmentRequestStatus.PROCESSED,
        		this.getClass().getSimpleName() + ".validate() failed for status:");
        fulfillBulkOrderResponse.getPurchaseUnitToOrderMap().stream().forEach(each -> {
            LOGGER.info("\n FulfillBulkOrder Encrypted Transaction Handle:{}", each.getOrderHandle());
        });
    }
}
