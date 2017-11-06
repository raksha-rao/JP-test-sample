package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WMerchantPullLogDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WMERCHANT_PULL_LOG table of MONEY database
 */
@Singleton
public class WMerchantPullLogDao extends MoneyDao {

    private static final String GET_MERCHANT_PULL_LOG_DETAILS_QUERY = "SELECT * FROM WMERCHANT_PULL_LOG where TRANSACTION_ID in ({commaSeparatedIds}) order by transaction_id";
    private static final String MERCHANT_TRANSACTION_ID_COL = "MERCHANT_TRANSACTION_ID";
    private static final String MP_ID_COL = "MP_ID";
    private static final String REASON_COL = "REASON";
    private static final String RC_COL = "RC";

    /**
     * Queries WMERCHANT_PULL_LOG table for input transaction
     * @param transactions
     * @return
     */
    public List<WMerchantPullLogDTO> getWMerchantPullLogDetails(List<WTransactionDTO> transactions) {
        String query = GET_MERCHANT_PULL_LOG_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WMerchantPullLogDTO> merchantPullLogs = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WMerchantPullLogDTO merchantPullLog = new WMerchantPullLogDTO();
            merchantPullLog.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            merchantPullLog.setAmount(getLong(result.get(AMOUNT_COL)));
            merchantPullLog.setBaId(getLong(result.get(BA_ID_COL)));
            merchantPullLog.setBufsId(getBigInteger(result.get(BUFS_ID_COL)));
            merchantPullLog.setBufsType(getByte(result.get(BUFS_TYPE_COL)));
            merchantPullLog.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            merchantPullLog.setFlags(getLong(result.get(FLAGS_COL)));
            merchantPullLog.setFsId(getBigInteger(result.get(FS_ID_COL)));
            merchantPullLog.setFsType(getByte(result.get(FS_TYPE_COL)));
            merchantPullLog.setInvoice(getString(result.get(INVOICE_COL)));
            merchantPullLog.setMerchantAccountNumber(getBigInteger(result.get(MERCHANT_ACCOUNT_NUMBER_COL)));
            merchantPullLog.setMerchantTransactionId(getBigInteger(result.get(MERCHANT_TRANSACTION_ID_COL)));
            merchantPullLog.setMpId(getBigInteger(result.get(MP_ID_COL)));
            merchantPullLog.setReason(getString(result.get(REASON_COL)));
            merchantPullLog.setReasonCode(getLong(result.get(RC_COL)));
            merchantPullLog.setStatus(getByte(result.get(STATUS_COL)));
            merchantPullLog.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            merchantPullLogs.add(merchantPullLog);
        }
        return merchantPullLogs;
    }

}
