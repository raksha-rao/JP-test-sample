package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WReturnsFeeDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WRETURNS_FEE table of MONEY database
 */
@Singleton
public class WReturnsFeeDao extends MoneyDao {

    private static final String GET_RETURNS_FEE_DETAILS_QUERY = "SELECT * FROM WRETURNS_FEE where transaction_id in ({commaSeparatedIds}) order by time_created,transaction_id";
    private static final String BENE_FEE_AMOUNT_COL = "BENE_FEE_AMOUNT";
    private static final String BENE_FEE_CURRENCY_CODE_COL = "BENE_FEE_CURRENCY_CODE";
    private static final String MARKUP_AMOUNT_COL = "MARKUP_AMOUNT";
    private static final String MARKUP_CURRENCY_CODE_COL = "MARKUP_CURRENCY_CODE";
    private static final String TOTAL_FEE_CURRENCY_CODE_COL = "TOTAL_FEE_CURRENCY_CODE";
    private static final String VEND_FEE_AMOUNT_COL = "VEND_FEE_AMOUNT";
    private static final String VEND_FEE_CURRENCY_CODE_COL = "VEND_FEE_CURRENCY_CODE";

    /**
     * Queries WRETURNS_FEE table for input transaction
     * @param transactions
     * @return
     */
    public List<WReturnsFeeDTO> getWReturnsFeeDetails(List<WTransactionDTO> transactions) {
        String query = GET_RETURNS_FEE_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WReturnsFeeDTO> returnsFees = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WReturnsFeeDTO returnsFee = new WReturnsFeeDTO();
            returnsFee.setBeneficiaryFeeAmount(getLong(result.get(BENE_FEE_AMOUNT_COL)));
            returnsFee.setBeneficiaryFeeCurrencyCode(getString(result.get(BENE_FEE_CURRENCY_CODE_COL)));
            returnsFee.setMarkupAmount(getLong(result.get(MARKUP_AMOUNT_COL)));
            returnsFee.setMarkupCurrencyCode(getString(result.get(MARKUP_CURRENCY_CODE_COL)));
            returnsFee.setStatus(getByte(result.get(STATUS_COL)));
            returnsFee.setTotalFeeAmount(getLong(result.get(TOTAL_FEE_AMOUNT_COL)));
            returnsFee.setTotalFeeCurrencyCode(getString(result.get(TOTAL_FEE_CURRENCY_CODE_COL)));
            returnsFee.setVendorFeeAmount(getLong(result.get(VEND_FEE_AMOUNT_COL)));
            returnsFee.setVendorFeeCurrencyCode(getString(result.get(VEND_FEE_CURRENCY_CODE_COL)));
            returnsFees.add(returnsFee);
        }
        return returnsFees;
    }
}
