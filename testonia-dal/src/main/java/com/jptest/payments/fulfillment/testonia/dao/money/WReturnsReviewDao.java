package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WReturnsReviewDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WRETURNS_REVIEW table of MONEY database
 */
@Singleton
public class WReturnsReviewDao extends MoneyDao {

    private static final String GET_RETURNS_REVIEW_DETAILS_QUERY = "SELECT * FROM WRETURNS_REVIEW where transaction_id in ({commaSeparatedIds}) order by time_created,transaction_id";

    /**
     * Queries WRETURNS_REVIEW table for input transaction
     * @param transactions
     * @return
     */
    public List<WReturnsReviewDTO> getWReturnsReviewDetails(List<WTransactionDTO> transactions) {
        String query = GET_RETURNS_REVIEW_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WReturnsReviewDTO> returnsReviews = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WReturnsReviewDTO returnsReview = new WReturnsReviewDTO();
            returnsReview.setAmount(getLong(result.get(AMOUNT_COL)));
            returnsReview.setCountryCode(getString(result.get(COUNTRY_CODE_COL)));
            returnsReview.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            returnsReview.setStatus(getByte(result.get(STATUS_COL)));
            returnsReviews.add(returnsReview);
        }
        return returnsReviews;
    }
}
