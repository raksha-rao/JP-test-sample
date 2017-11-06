package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionAdminDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WTRANSACTION_ADMIN table of MONEY database
 */
@Singleton
public class WTransactionAdminDao extends MoneyDao {

    private static final String GET_TRANSACTION_ADMIN_DETAILS_QUERY = "SELECT * FROM WTRANSACTION_ADMIN where transaction_id in ({commaSeparatedIds}) order by id";
    private static final String CENTER_CODE_COL = "CENTER_CODE";
    private static final String COMPANY_CODE_COL = "COMPANY_CODE";
    private static final String GL_ACCOUNT_NUMBER_COL = "GL_ACCOUNT_NUMBER";
    private static final String INTERNAL_MEMO_COL = "INTERNAL_MEMO";
    private static final String ORIGINAL_TRANSACTION_ID_COL = "ORIGINAL_TRANSACTION_ID";
    private static final String REASON_ID_COL = "REASON_ID";

    /**
     * Queries WTRANSACTION_ADMIN table for input transaction
     * @param transactions
     * @return
     */
    public List<WTransactionAdminDTO> getWTransactionAdminDetails(List<WTransactionDTO> transactions) {
        final String query = GET_TRANSACTION_ADMIN_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransactionAdminDTO> transactionAdminList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransactionAdminDTO transactionAdmin = new WTransactionAdminDTO();
            transactionAdmin.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            transactionAdmin.setCenterCode(getInteger(result.get(CENTER_CODE_COL)));
            transactionAdmin.setCompanyCode(getInteger(result.get(COMPANY_CODE_COL)));
            transactionAdmin.setFlags(getLong(result.get(FLAGS_COL)));
            transactionAdmin.setGlAccountNumber(getBigInteger(result.get(GL_ACCOUNT_NUMBER_COL)));
            transactionAdmin.setInternalMemo(getString(result.get(INTERNAL_MEMO_COL)));
            transactionAdmin.setOriginalTransactionId(getBigInteger(result.get(ORIGINAL_TRANSACTION_ID_COL)));
            transactionAdmin.setReasonId(getBigInteger(result.get(REASON_ID_COL)));
            transactionAdmin.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            transactionAdminList.add(transactionAdmin);
        }
        return transactionAdminList;
    }
}
