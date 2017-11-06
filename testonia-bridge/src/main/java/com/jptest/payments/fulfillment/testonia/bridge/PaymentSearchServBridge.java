package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.money.PaymentSearch;
import com.jptest.money.SearchForCustomerTransactionLegacyEquivalentsRequest;
import com.jptest.money.SearchForCustomerTransactionLegacyEquivalentsResponse;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;


/**
 * Represents bridge for paymentsearchserv API calls
 */

@Singleton
public class PaymentSearchServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSearchServBridge.class);

    private PaymentSearch paymentSearchServ;

    @Inject
    public PaymentSearchServBridge(@Named("paymentsearchserv") final PaymentSearch paymentSearchServ) {
        this.paymentSearchServ = paymentSearchServ;
    }

    /**
     * Returns transaction details for input account number
     *
     * @param accountNumber
     * @return
     * @throws IOException
     */
    public SearchForCustomerTransactionLegacyEquivalentsResponse searchForCustomerTransactionLegacyEquivalents(
            final SearchForCustomerTransactionLegacyEquivalentsRequest request) throws TestExecutionException {
        LOGGER.info("PSS SearchForCustomerTransactionLegacyEquivalents request: {}", printValueObject(request));
        SearchForCustomerTransactionLegacyEquivalentsResponse response = null;
        try {
            response = this.paymentSearchServ
                    .search_for_customer_transaction_legacy_equivalents(request);
            LOGGER.info("PSS SearchForCustomerTransactionLegacyEquivalents  response: {}", printValueObject(response));

            if (response == null || response.getResult().getItems() == null) {
                throw new TestExecutionException("Empty Response from PSS");
            }
        }
        catch (final Exception e) {
            LOGGER.error("Encountered exception while calling PaymentSearchService", e);
            throw new TestExecutionException("Encountered exception while calling PaymentSearchService");
        }

        return response;
    }
}
