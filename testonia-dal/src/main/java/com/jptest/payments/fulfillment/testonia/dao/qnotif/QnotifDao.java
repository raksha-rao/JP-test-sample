package com.jptest.payments.fulfillment.testonia.dao.qnotif;

import com.jptest.payments.fulfillment.testonia.dao.BaseDao;

/**
 * This class serves as a parent class for all QNOTIF DB related DAO classes
 * It holds all common items such as common methods, common fields etc for QNOTIF DB related queries.
 */
public class QnotifDao extends BaseDao {

    private static final String DATABASE_NAME = "QNOTIF";

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }
}
