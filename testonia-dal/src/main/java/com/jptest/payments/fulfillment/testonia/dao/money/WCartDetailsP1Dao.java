package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WCartDetailsDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WCART_DETAILS table of MONEY database
 */
@Singleton
public class WCartDetailsP1Dao extends MoneyDao {

    private static final String GET_WXCLICK_URL_P2_DETAILS_QUERY = "SELECT * FROM WCART_DETAILS where trans_data_map_id in "
            + "(select MAP_ID from WTRANS_DATA_MAP where transactionlike_id in ({commaSeparatedIds}) and transactionlike_type = 'T' and BITAND(FLAGS, 32) > 0) "
            + "order by trans_data_map_id";

    private static final String CHARSET_COL = "CHARSET";
    private static final String COST_COL = "COST";
    private static final String EXTRA_SHIPPING_COL = "EXTRA_SHIPPING";
    private static final String HANDLING_COL = "HANDLING";
    private static final String HOSTED_BUTTON_ID_COL = "HOSTED_BUTTON_ID";
    private static final String HOSTED_BUTTON_KEYS_TEXT_COL = "HOSTED_BUTTON_KEYS_TEXT";
    private static final String INCENTIVE_TYPE_COL = "INCENTIVE_TYPE";
    private static final String INSURANCE_COL = "INSURANCE";
    private static final String ITEM_AMOUNT_COL = "ITEM_AMOUNT";
    private static final String ITEM_DISCOUNT_COL = "ITEM_DISCOUNT";
    private static final String ITEM_NAME_COL = "ITEM_NAME";
    private static final String ITEM_NUMBER_COL = "ITEM_NUMBER";
    private static final String QUANTITY_UNIT_COL = "QUANTITY_UNIT";
    private static final String SHIPPING_COL = "SHIPPING";
    private static final String TAX_AMOUNT_COL = "TAX_AMOUNT";
    private static final String TAX_RATE_COL = "TAX_RATE";
    private static final String TOTAL_AMOUNT_COL = "TOTAL_AMOUNT";

    /**
     * Queries WXCLICK_P2 table for input transaction
     * @param transactions
     * @return
     */
    public List<WCartDetailsDTO> getCartDetails(List<WTransactionDTO> transactions) {
        String query = GET_WXCLICK_URL_P2_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WCartDetailsDTO> wCartDetailsList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WCartDetailsDTO wCartDetails = new WCartDetailsDTO();
            wCartDetails.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            wCartDetails.setCharset(getString(result.get(CHARSET_COL)));
            wCartDetails.setCost(getBigInteger(result.get(COST_COL)));
            wCartDetails.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            wCartDetails.setExtraShipping(getBigInteger(result.get(EXTRA_SHIPPING_COL)));
            wCartDetails.setFlags(getLong(result.get(FLAGS_COL)));
            wCartDetails.setHandling(getBigInteger(result.get(HANDLING_COL)));
            wCartDetails.setHostedButtonId(getBigInteger(result.get(HOSTED_BUTTON_ID_COL)));
            wCartDetails.setHostedButtonKeysText(getString(result.get(HOSTED_BUTTON_KEYS_TEXT_COL)));
            wCartDetails.setIncentiveType(getBigInteger(result.get(INCENTIVE_TYPE_COL)));
            wCartDetails.setInsurance(getBigInteger(result.get(INSURANCE_COL)));
            wCartDetails.setItemAmount(getBigInteger(result.get(ITEM_AMOUNT_COL)));
            wCartDetails.setItemDiscount(getBigInteger(result.get(ITEM_DISCOUNT_COL)));
            wCartDetails.setItemName(getString(result.get(ITEM_NAME_COL)));
            wCartDetails.setItemNumber(getString(result.get(ITEM_NUMBER_COL)));
            wCartDetails.setQuantity(getBigInteger(result.get(QUANTITY_COL)));
            wCartDetails.setQuantityUnit(getBigInteger(result.get(QUANTITY_UNIT_COL)));
            wCartDetails.setShipping(getBigInteger(result.get(SHIPPING_COL)));
            wCartDetails.setTaxAmount(getBigInteger(result.get(TAX_AMOUNT_COL)));
            wCartDetails.setTaxRate(getBigInteger(result.get(TAX_RATE_COL)));
            wCartDetails.setTotalAmount(getBigInteger(result.get(TOTAL_AMOUNT_COL)));
            wCartDetails.setTransDataMapId(getBigInteger(result.get(TRANS_DATA_MAP_ID_COL)));
            wCartDetailsList.add(wCartDetails);
        }
        return wCartDetailsList;
    }
}
