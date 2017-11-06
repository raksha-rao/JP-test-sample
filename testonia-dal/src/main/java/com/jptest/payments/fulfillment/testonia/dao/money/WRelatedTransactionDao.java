package com.jptest.payments.fulfillment.testonia.dao.money;

import com.jptest.payments.fulfillment.testonia.model.money.WRelatedTransactionDTO;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Represents WRELATED_TRANSACTION table of MONEY database
 */
@Singleton
public class WRelatedTransactionDao extends MoneyDao {
    private static final String REPAYMENT_TRANSACTION_ID_REPLACEMENT_TOKEN = "{repaymentTxnId}";
    private static final String GET_WRelatedTransaction_QUERY =
            "SELECT RELATED_TRANSACTION_ID, ASSOCIATION_TYPE FROM WRELATED_TRANSACTION WHERE TRANSACTION_ID = {repaymentTxnId}";

    /**
     * Queries WRELATED_TRANSACTION table for input transaction
     *
     * @param repaymentTransactionId
     * @return
     */
    public List<WRelatedTransactionDTO> getWRelatedTransactionDetails(String repaymentTransactionId) {
        final String query = GET_WRelatedTransaction_QUERY.replace(REPAYMENT_TRANSACTION_ID_REPLACEMENT_TOKEN,
                repaymentTransactionId);
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        final List<WRelatedTransactionDTO> wRelatedTransactionDTOS = new ArrayList<>();
        for (final Map<String, Object> result : queryResult) {
            final WRelatedTransactionDTO wRelatedTransactionDTO = new WRelatedTransactionDTO();
            wRelatedTransactionDTO.setAssociationType(this.getString(result.get(ASSOCIATION_TYPE_COL)));
            wRelatedTransactionDTO.setRelatedTransactionId(this.getBigInteger(result.get(RELATED_TRANSACTION_ID_COL)));
            wRelatedTransactionDTO.setTransactionId(this.getBigInteger(result.get(TRANSACTION_ID_COL)));
            wRelatedTransactionDTOS.add(wRelatedTransactionDTO);
        }

        return wRelatedTransactionDTOS;
    }

}
