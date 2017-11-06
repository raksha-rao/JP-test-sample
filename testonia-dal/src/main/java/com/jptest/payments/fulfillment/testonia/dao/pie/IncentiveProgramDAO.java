package com.jptest.payments.fulfillment.testonia.dao.pie;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

/**
 * Represents incentive_program table of PIE database
 */
@Singleton
public class IncentiveProgramDAO extends PieDao {

    private static final String UPDATE_INCENTIVE_PROGRAM = "update incentive_program set redeem_start_date = '{redeemStartDate}' where code in ({programCodes})";
    private static final String REDEEM_START_DATE = "{redeemStartDate}";
    private static final String COMMA_SEPARATED_PROGRAM_CODES = "{programCodes}";

    /**
     * 
     * @param redeemStartDate - example 15-DEC-11 10.42.54.000000 AM
     * @param codes - example 'code1','code2'
     * @return
     */
    public void updateIncentive(String redeemStartDate, List<String> codes) {
        String updateQuery = UPDATE_INCENTIVE_PROGRAM.replace(REDEEM_START_DATE, redeemStartDate)
                .replace(COMMA_SEPARATED_PROGRAM_CODES,
                        codes.stream().map(code -> "'" + code + "'").collect(Collectors.joining(",")));
        dbHelper.executeUpdateQuery(getDatabaseName(), updateQuery);
    }
}
