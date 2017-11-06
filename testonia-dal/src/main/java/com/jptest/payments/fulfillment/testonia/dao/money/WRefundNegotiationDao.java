package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WRefundNegotiationDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WREFUND_NEGOTIATION table of MONEY database
 */
@Singleton
public class WRefundNegotiationDao extends MoneyDao {

    private static final String GET_REFUND_NEGOTIATION_DETAILS_QUERY = "SELECT * FROM WREFUND_NEGOTIATION where transaction_id in ({commaSeparatedIds}) order by time_created";
    private static final String ASK_AMOUNT_COL = "ASK_AMOUNT";
    private static final String FEE_AMOUNT_COL = "FEE_AMOUNT";
    private static final String ASK_AMOUNT_USD_COL = "ASK_AMOUNT_USD";
    private static final String BID_AMOUNT_USD_COL = "BID_AMOUNT_USD";
    private static final String FEE_AMOUNT_USD_COL = "FEE_AMOUNT_USD";

    /**
     * Queries WREFUND_NEGOTIATION table for input transaction
     * @param transactions
     * @return
     */
    public List<WRefundNegotiationDTO> getWRefundNegotiationDetails(List<WTransactionDTO> transactions) {
        String query = GET_REFUND_NEGOTIATION_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WRefundNegotiationDTO> refundNegotiations = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WRefundNegotiationDTO refundNegotiation = new WRefundNegotiationDTO();
            refundNegotiation.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            refundNegotiation.setAskAmount(getLong(result.get(ASK_AMOUNT_COL)));
            refundNegotiation.setAskAmountUSD(getLong(result.get(ASK_AMOUNT_USD_COL)));
            refundNegotiation.setBidAmount(getLong(result.get(BID_AMOUNT_COL)));
            refundNegotiation.setBidAmountUSD(getLong(result.get(BID_AMOUNT_USD_COL)));
            refundNegotiation.setCounterparty(getBigInteger(result.get(COUNTERPARTY_COL)));
            refundNegotiation.setFeeAmount(getLong(result.get(FEE_AMOUNT_COL)));
            refundNegotiation.setFeeAmountUSD(getLong(result.get(FEE_AMOUNT_USD_COL)));
            refundNegotiation.setFlags(getLong(result.get(FLAGS_COL)));
            refundNegotiation.setStatus(getByte(result.get(STATUS_COL)));
            refundNegotiation.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            refundNegotiations.add(refundNegotiation);
        }
        return refundNegotiations;
    }
}
