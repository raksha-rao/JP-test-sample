package com.jptest.payments.fulfillment.testonia.dao.txn;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionAuthDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WTRANSACTION_AUTH_P2 table of TXN database
 */
@Singleton
public class WTransactionAuthP2Dao extends TxnDao {

    private static final String GET_WTRANSACTION_AUTH_P2_QUERY = "SELECT * FROM WTRANSACTION_AUTH_P2 where transaction_id in ({commaSeparatedIds}) order by time_created,transaction_id";
    private static final String ACTIVE_AUTH_ID_COL = "ACTIVE_AUTH_ID";
    private static final String AMOUNT_AUTHORIZED_COL = "AMOUNT_AUTHORIZED";
    private static final String AMOUNT_AUTHORIZED_USD_COL = "AMOUNT_AUTHORIZED_USD";
    private static final String AMOUNT_SETTLED_COL = "AMOUNT_SETTLED";
    private static final String AMOUNT_SETTLED_USD_COL = "AMOUNT_SETTLED_USD";
    private static final String AUTH_EXPIRATION_TIME_COL = "AUTH_EXPIRATION_TIME";
    private static final String BALANCE_AMOUNT_COL = "BALANCE_AMOUNT";
    private static final String BALANCE_AMOUNT_USD_COL = "BALANCE_AMOUNT_USD";
    private static final String BUFS_AMOUNT_COL = "BUFS_AMOUNT";
    private static final String BUFS_AMOUNT_USD_COL = "BUFS_AMOUNT_USD";
    private static final String BUFS_CURRENCY_CODE_COL = "BUFS_CURRENCY_CODE";
    private static final String BUFS_ID_COL = "BUFS_ID";
    private static final String BUFS_TYPE_COL = "BUFS_TYPE";
    private static final String CPARTY_TRANS_ID_COL = "CPARTY_TRANS_ID";
    private static final String FS_AMOUNT_COL = "FS_AMOUNT";
    private static final String FS_AMOUNT_USD_COL = "FS_AMOUNT_USD";
    private static final String FS_CURRENCY_CODE_COL = "FS_CURRENCY_CODE";
    private static final String FS_ID_COL = "FS_ID";
    private static final String FS_TYPE_COL = "FS_TYPE";
    private static final String HONOR_EXPIRATION_TIME_COL = "HONOR_EXPIRATION_TIME";
    private static final String NONTARGET_BAL_AMOUNT_COL = "NONTARGET_BAL_AMOUNT";
    private static final String NONTARGET_BAL_AMOUNT_USD_COL = "NONTARGET_BAL_AMOUNT_USD";
    private static final String NONTARGET_BAL_CURRENCY_CODE_COL = "NONTARGET_BAL_CURRENCY_CODE";
    private static final String NUMBER_OF_SETTLEMENTS_COL = "NUMBER_OF_SETTLEMENTS";
    private static final String ORIGINAL_AUTH_ID_COL = "ORIGINAL_AUTH_ID";
    private static final String REAUTH_TIME_COL = "REAUTH_TIME";

    /**
     * Queries WTRANSACTION_AUTH_P2 table for input transaction
     * @param transactions
     * @return
     */
    public List<WTransactionAuthDTO> getWTransactionAuthDetails(List<WTransactionDTO> transactions) {
        String query = GET_WTRANSACTION_AUTH_P2_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransactionAuthDTO> transactionAuths = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransactionAuthDTO transactionAuth = new WTransactionAuthDTO();
            transactionAuth.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            transactionAuth.setActiveAuthId(getBigInteger(result.get(ACTIVE_AUTH_ID_COL)));
            transactionAuth.setAmountAuthorized(getLong(result.get(AMOUNT_AUTHORIZED_COL)));
            transactionAuth.setAmountAuthorizedUSD(getLong(result.get(AMOUNT_AUTHORIZED_USD_COL)));
            transactionAuth.setAmountSettled(getLong(result.get(AMOUNT_SETTLED_COL)));
            transactionAuth.setAmountSettledUSD(getLong(result.get(AMOUNT_SETTLED_USD_COL)));
            transactionAuth.setAuthExpirationTime(getLong(result.get(AUTH_EXPIRATION_TIME_COL)));
            transactionAuth.setBalanceAmount(getLong(result.get(BALANCE_AMOUNT_COL)));
            transactionAuth.setBalanceAmountUSD(getLong(result.get(BALANCE_AMOUNT_USD_COL)));
            transactionAuth.setBufsAmount(getLong(result.get(BUFS_AMOUNT_COL)));
            transactionAuth.setBufsAmountUSD(getLong(result.get(BUFS_AMOUNT_USD_COL)));
            transactionAuth.setBufsCurrencyCode(getString(result.get(BUFS_CURRENCY_CODE_COL)));
            transactionAuth.setBufsId(getBigInteger(result.get(BUFS_ID_COL)));
            transactionAuth.setBufsType(getByte(result.get(BUFS_TYPE_COL)));
            transactionAuth.setCounterparty(getBigInteger(result.get(COUNTERPARTY_COL)));
            transactionAuth.setCounterpartyTransactionId(getBigInteger(result.get(CPARTY_TRANS_ID_COL)));
            transactionAuth.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            transactionAuth.setFlags(getLong(result.get(FLAGS_COL)));
            transactionAuth.setFsAmount(getLong(result.get(FS_AMOUNT_COL)));
            transactionAuth.setFsAmountUSD(getLong(result.get(FS_AMOUNT_USD_COL)));
            transactionAuth.setFsCurrencyCode(getString(result.get(FS_CURRENCY_CODE_COL)));
            transactionAuth.setFsId(getBigInteger(result.get(FS_ID_COL)));
            transactionAuth.setFsType(getByte(result.get(FS_TYPE_COL)));
            transactionAuth.setHonorExpirationTime(getLong(result.get(HONOR_EXPIRATION_TIME_COL)));
            transactionAuth.setNonTargetBalanceAmount(getLong(result.get(NONTARGET_BAL_AMOUNT_COL)));
            transactionAuth.setNonTargetBalanceAmountUSD(getLong(result.get(NONTARGET_BAL_AMOUNT_USD_COL)));
            transactionAuth.setNonTargetBalanceCurrencyCode(getString(result.get(NONTARGET_BAL_CURRENCY_CODE_COL)));
            transactionAuth.setNumberOfSettlements(getInteger(result.get(NUMBER_OF_SETTLEMENTS_COL)));
            transactionAuth.setOriginalAuthId(getBigInteger(result.get(ORIGINAL_AUTH_ID_COL)));
            transactionAuth.setReauthTime(getLong(result.get(REAUTH_TIME_COL)));
            transactionAuth.setStatus(getByte(result.get(STATUS_COL)));
            transactionAuth.setTimeCreated(getLong(result.get(TIME_CREATED_COL)));
            transactionAuth.setTimeUpdated(getLong(result.get(TIME_UPDATED_COL)));
            transactionAuth.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            transactionAuths.add(transactionAuth);
        }
        return transactionAuths;
    }
}
