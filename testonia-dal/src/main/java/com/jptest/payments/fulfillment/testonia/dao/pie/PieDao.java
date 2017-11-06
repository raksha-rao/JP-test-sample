package com.jptest.payments.fulfillment.testonia.dao.pie;

import com.jptest.payments.fulfillment.testonia.dao.BaseDao;

/**
 * This class serves as a parent class for all PIE DB related DAO classes
 * It holds all common items such as common methods, common fields etc for PIE DB related queries.
 */
public class PieDao extends BaseDao {

    private static final String DATABASE_NAME = "PIE";

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }
}
