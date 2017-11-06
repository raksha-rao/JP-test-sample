package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.SearchForCustomerTransactionLegacyEquivalentsResponse;
import com.jptest.money.SearchStatus;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.vo.ValueObject;

/**
 * Basic PSS Response Validation
 * Validates the status
 * Validates the wtransactionList returned is not empty
 */


public class PSSValidationAsserter extends BaseAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PSSValidationAsserter.class);

    @Override
    public void validate(final Context context) {

        final SearchForCustomerTransactionLegacyEquivalentsResponse pssResponse = (SearchForCustomerTransactionLegacyEquivalentsResponse) this
                .getDataFromContext(context, ContextKeys.PSS_RESPONSE_KEY.getName());

        Assert.assertNotNull(pssResponse, "PSSResponse should be not null.");
        Assert.assertTrue(pssResponse.getResult().getStatusAsEnum() == SearchStatus.SUCCESS,
                "PSS call status " + pssResponse.getResult().getStatusAsEnum());

        final List<ValueObject> wTransactionList = pssResponse.getResult().getItems();

        if (wTransactionList != null) {
            LOGGER.info("Retrived {} transactions for encryptedTransactionId {}", wTransactionList.size());
            Assert.assertTrue(wTransactionList.size() > 0,
                    "PSS returned records correctly");
        }
        else {
            Assert.fail("PSS returned empty WtransactionList");
        }
    }
}
