package com.jptest.payments.fulfillment.testonia.dao.cloc;

import com.jptest.payments.fulfillment.testonia.dao.BaseDao;

/**
 * This class serves as a parent class for all CLOC DB related DAO classes
 * It holds all common items such as common methods, common fields etc for CLOC DB related queries.
 */
public abstract class ClockDao extends BaseDao {

    private static final String DATABASE_NAME = "CLOC";

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }
}
