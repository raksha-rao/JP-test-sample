package com.jptest.payments.fulfillment.testonia.dao.pymt;

import com.jptest.payments.fulfillment.testonia.dao.BaseDao;

/**
 * This class serves as a parent class for all PYMT DB related DAO classes
 * It holds all common items such as common methods, common fields etc for PYMT DB related queries.
 */
public class PymtDao extends BaseDao {

    private static final String DATABASE_NAME = "PYMT";

    protected static final String TYPE_CODE_COL = "TYPE_CODE";
    protected static final String SUBTYPE_CODE_COL = "SUBTYPE_CODE";
    protected static final String STATUS_CODE_COL = "STATUS_CODE";
    protected static final String MONEY_AMOUNT_COL = "MONEY_AMOUNT";
    protected static final String CURRENCY_CODE_COL = "CURRENCY_CODE";
    protected static final String FLAGS01_COL = "FLAGS01";
    protected static final String FLAGS02_COL = "FLAGS02";
    protected static final String FLAGS03_COL = "FLAGS03";
    protected static final String FLAGS04_COL = "FLAGS04";
    protected static final String FLAGS05_COL = "FLAGS05";
    protected static final String FLAGS06_COL = "FLAGS06";
    protected static final String FLAGS07_COL = "FLAGS07";
    protected static final String FLAGS08_COL = "FLAGS08";
    protected static final String FLAGS09_COL = "FLAGS09";
    protected static final String FLAGS10_COL = "FLAGS10";
    protected static final String INITIATION_TIME_COL = "INITIATION_TIME";
    protected static final String LAST_UPDATED_TIME_COL = "LAST_UPDATED_TIME";
    protected static final String USD_MONEY_AMOUNT_COL = "USD_MONEY_AMOUNT";
    protected static final String REASON_CODE_COL = "REASON_CODE_COL";

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }
}
