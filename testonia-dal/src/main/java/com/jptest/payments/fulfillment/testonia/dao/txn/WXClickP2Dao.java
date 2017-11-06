package com.jptest.payments.fulfillment.testonia.dao.txn;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WxClickDTO;

/**
 * Represents WXCLICK_P2 table of TXN database
 */
@Singleton
public class WXClickP2Dao extends TxnDao {

    private static final String GET_WXCLICK_P2_DETAILS_QUERY = "SELECT * FROM WXCLICK_P2 where trans_data_map_id in "
            + "(select MAP_ID from WTRANS_DATA_MAP_P2 where transactionlike_id in ({commaSeparatedIds}) and transactionlike_type = 'T' and BITAND(FLAGS, {wxClickFlag}) > 0) "
            + "order by trans_data_map_id";
    private static final String WXCLICK_FLAG_REPLACEMENT_TOKEN = "{wxClickFlag}";
    private static final String WXCLICK_FLAG_VALUE = "1";
    private static final String ADJUSTMENT_AMOUNT_COL = "ADJUSTMENT_AMOUNT";
    private static final String GIFT_MESSAGE_COL = "GIFT_MESSAGE";
    private static final String GIFT_WRAP_NAME_COL = "GIFT_WRAP_NAME";
    private static final String HANDLING_AMOUNT_COL = "HANDLING_AMOUNT";
    private static final String INVOICE_COL = "INVOICE";
    private static final String ITEM_AMOUNT_COL = "ITEM_AMOUNT";
    private static final String ITEM_NAME_COL = "ITEM_NAME";
    private static final String ITEM_NUMBER_COL = "ITEM_NUMBER";
    private static final String PROMOTIONAL_EMAIL_ADDRESS_COL = "PROMOTIONAL_EMAIL_ADDRESS";
    private static final String SALES_TAX_COL = "SALES_TAX";
    private static final String SURVEY_ANSWER_COL = "SURVEY_ANSWER";
    private static final String SURVEY_QUESTION_COL = "SURVEY_QUESTION";

    /**
     * Queries WXCLICK_P2 table for input transaction
     * @param transactions
     * @return
     */
    public List<WxClickDTO> getWxClickDetails(List<WTransactionDTO> transactions) {
        String query = GET_WXCLICK_P2_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()))
                .replace(WXCLICK_FLAG_REPLACEMENT_TOKEN, WXCLICK_FLAG_VALUE);
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WxClickDTO> wxClicks = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WxClickDTO wxClick = new WxClickDTO();
            wxClick.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            wxClick.setAdjustmentAmount(getLong(result.get(ADJUSTMENT_AMOUNT_COL)));
            wxClick.setCounterparty(getBigInteger(result.get(COUNTERPARTY_COL)));
            wxClick.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            wxClick.setFlags(getLong(result.get(FLAGS_COL)));
            wxClick.setGiftMessage(getString(result.get(GIFT_MESSAGE_COL)));
            wxClick.setGiftWrapName(getString(result.get(GIFT_WRAP_NAME_COL)));
            wxClick.setHandlingAmount(getLong(result.get(HANDLING_AMOUNT_COL)));
            wxClick.setInvoice(getString(result.get(INVOICE_COL)));
            wxClick.setItemAmount(getLong(result.get(ITEM_AMOUNT_COL)));
            wxClick.setItemName(getString(result.get(ITEM_NAME_COL)));
            wxClick.setItemNumber(getString(result.get(ITEM_NUMBER_COL)));
            wxClick.setPromotionalEmailAddress(getString(result.get(PROMOTIONAL_EMAIL_ADDRESS_COL)));
            wxClick.setQuantity(getInteger(result.get(QUANTITY_COL)));
            wxClick.setSalesTax(getBigDecimal(result.get(SALES_TAX_COL)));
            wxClick.setShippingAmount(getLong(result.get(SHIPPING_AMOUNT_COL)));
            wxClick.setSurveyAnswer(getString(result.get(SURVEY_ANSWER_COL)));
            wxClick.setSurveyQuestion(getString(result.get(SURVEY_QUESTION_COL)));
            wxClick.setTransDataMapId(getBigInteger(result.get(TRANS_DATA_MAP_ID_COL)));
            wxClicks.add(wxClick);
        }
        return wxClicks;
    }
}
