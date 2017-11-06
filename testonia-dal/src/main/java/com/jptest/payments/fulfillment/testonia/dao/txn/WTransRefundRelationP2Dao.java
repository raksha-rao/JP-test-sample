package com.jptest.payments.fulfillment.testonia.dao.txn;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransRefundRelationDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WTRANS_REFUND_RELATION_P2 table of TXN database
 */
@Singleton
public class WTransRefundRelationP2Dao extends TxnDao {

    private static final String GET_TRANS_REFUND_RELATION_P2_DETAILS_QUERY = "SELECT * FROM WTRANS_REFUND_RELATION_P2 where refund_id in ({commaSeparatedIds}) order by time_created";
    private static final String BC_CREDIT_ID_COL = "BC_CREDIT_ID";
    private static final String CC_CREDIT_ID_COL = "CC_CREDIT_ID";
    private static final String CURRENCY_CONVERSION_ID_COL = "CURRENCY_CONVERSION_ID";
    private static final String FEE_REFUND_ID_COL = "FEE_REFUND_ID";
    private static final String REFUND_ID_COL = "REFUND_ID";
    private static final String TEMP_HOLD_ID_COL = "TEMP_HOLD_ID";
    private static final String WTRANS_REFUND_RELATION_P2_COUNT_QUERY = 
    		"select count(refund_id) as COUNT from WTRANS_REFUND_RELATION_P2 where refund_id = {transaction_id}";
    private static final String TRANSACTION_ID_REPLACEMENT_TOKEN = "{transaction_id}";

    /**
     * Queries WTRANS_REFUND_RELATION_P2 table for input transaction
     * @param transactions
     * @return
     */
    public List<WTransRefundRelationDTO> getWTransactionRefundRelationDetails(
            List<WTransactionDTO> transactions) {
        String query = GET_TRANS_REFUND_RELATION_P2_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransRefundRelationDTO> transactionRefundRelations = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransRefundRelationDTO transactionRefundRelation = new WTransRefundRelationDTO();
            transactionRefundRelation.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            transactionRefundRelation.setBcCreditId(getBigInteger(result.get(BC_CREDIT_ID_COL)));
            transactionRefundRelation.setCcCreditId(getBigInteger(result.get(CC_CREDIT_ID_COL)));
            transactionRefundRelation.setCurrencyConversionId(getBigInteger(result.get(CURRENCY_CONVERSION_ID_COL)));
            transactionRefundRelation.setFeeRefundId(getBigInteger(result.get(FEE_REFUND_ID_COL)));
            transactionRefundRelation.setRefundId(getBigInteger(result.get(REFUND_ID_COL)));
            transactionRefundRelation.setTempHoldId(getBigInteger(result.get(TEMP_HOLD_ID_COL)));
            transactionRefundRelations.add(transactionRefundRelation);
        }
        return transactionRefundRelations;
    }
    
    /**
     * Queries WTRANS_REFUND_RELATION_P2 to find number of records matching input transaction id
     * @param transactionId
     * @return
     */
    public Integer getTransactionRecordCount(final String transactionId) {
        final String query = WTRANS_REFUND_RELATION_P2_COUNT_QUERY.replace(TRANSACTION_ID_REPLACEMENT_TOKEN, 
        		transactionId);
        final Map<String, Object> dbResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
                query);

        return this.getInteger(dbResult.get(COUNT_COL));
    }
}
