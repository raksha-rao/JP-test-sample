package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransRefundRelationDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WTRANS_REFUND_RELATION table of MONEY database
 */
@Singleton
public class WTransRefundRelationDao extends MoneyDao {

    private static final String GET_TRANS_REFUND_RELATION_DETAILS_QUERY = "SELECT * FROM WTRANS_REFUND_RELATION where refund_id in ({commaSeparatedIds}) order by time_created";
    private static final String BC_CREDIT_ID_COL = "BC_CREDIT_ID";
    private static final String CC_CREDIT_ID_COL = "CC_CREDIT_ID";
    private static final String CURRENCY_CONVERSION_ID_COL = "CURRENCY_CONVERSION_ID";
    private static final String FEE_REFUND_ID_COL = "FEE_REFUND_ID";
    private static final String REFUND_ID_COL = "REFUND_ID";
    private static final String TEMP_HOLD_ID_COL = "TEMP_HOLD_ID";

    /**
     * Queries WTRANS_REFUND_RELATION table for input transaction
     * @param transactions
     * @return
     */
    public List<WTransRefundRelationDTO> getWTransactionRefundRelationDetails(
            List<WTransactionDTO> transactions) {
        String query = GET_TRANS_REFUND_RELATION_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
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
}
