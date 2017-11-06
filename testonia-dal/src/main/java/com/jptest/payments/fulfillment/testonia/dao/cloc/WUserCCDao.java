package com.jptest.payments.fulfillment.testonia.dao.cloc;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.collections.CollectionUtils;
import org.testng.collections.Lists;

/**
 * DAO for wuser_cc table of CLOC DB
 */
@Singleton
public class WUserCCDao extends ClockDao {

    private static final String UPDATE_WUSER_CC_REJECT_QUERY = "update wuser_cc set first_name = 'CCREJECT-REFUSED' where account_number = '{accountNumber}' ";
    private static final String GET_CC_ID_FOR_ACCT_NUM_AND_TYPE_QUERY = "select id from wuser_cc where account_number='{accountNumber}' and TYPE='{type}'";

    // tokens
    private static final String ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{accountNumber}";
    private static final String CC_TYPE_TOKEN = "{type}";
    private static final String ID_COLUMN = "ID";

    public void rejectCC(final String accountNumber) {
        final String query = UPDATE_WUSER_CC_REJECT_QUERY
                .replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber);
        this.dbHelper.executeUpdateQuery(getDatabaseName(), query);
    }

    /**
     * Gets credit card ids for a given account number and type of card.
     * @param accountNumber
     * @param type
     * @return
     */
    public List<BigInteger> getCreditCardIds(String accountNumber, char type) {
        final String query = GET_CC_ID_FOR_ACCT_NUM_AND_TYPE_QUERY
                .replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber)
                .replace(CC_TYPE_TOKEN, String.valueOf(type));
        List<Map<String, Object>> result = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<BigInteger> ccIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(result)) {
            for (Map<String, Object> row : result) {
                ccIds.add(new BigInteger(row.get(ID_COLUMN).toString()));
            }
        }
        return ccIds;
    }
}
