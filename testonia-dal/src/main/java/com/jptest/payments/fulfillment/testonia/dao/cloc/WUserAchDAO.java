package com.jptest.payments.fulfillment.testonia.dao.cloc;

import java.util.Map;
import javax.inject.Singleton;


/**
 * DAO for wuser_cc table of CLOC DB
 */
@Singleton
public class WUserAchDAO extends ClockDao {

    private static final String ACCOUNT_NUMBER = "{account_number}";
    private static final String ACH_ID = "{ach_id}";

    private static final String SELECT_ACH_ID_QUERY = "select id from wuser_ach where account_number = '{account_number}' and (status ='M' or status = 'A') order by id ";
    private static final String UPDATE_ACH_STATUS_QUERY = "update wuser_ach set status = 'B' where id = '{ach_id}' ";

    public String getAchId(final String accountNumber) {

        final String selectQuery = SELECT_ACH_ID_QUERY.replace(ACCOUNT_NUMBER, accountNumber);
        final Map<String, Object> result = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
                selectQuery);

        return (String) result.get("ID");
    }

    public void updateAchStatus(final String achId) {

        final String updateQuery = UPDATE_ACH_STATUS_QUERY.replace(ACH_ID, achId);
        this.dbHelper.executeUpdateQuery(this.getDatabaseName(), updateQuery);
    }
}
