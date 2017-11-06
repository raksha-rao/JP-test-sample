package com.jptest.payments.fulfillment.testonia.dao.txn;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.BaseDao;

/**
 * This class serves as a parent class for all TXN DB related DAO classes
 * It holds all common items such as common methods, common fields etc for TXN DB related queries.
 */
@Singleton
public abstract class TxnDao extends BaseDao {

    private static final String DATABASE_NAME = "TXN";

    // common tokens
    protected static final String COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN = "{commaSeparatedIds}";
    protected static final String AUCTION_ITEM_ID_REPLACEMENT_TOKEN = "{auctionItemIds}";

    // Column names common across tables
    protected static final String ACCOUNT_NUMBER_COL = "ACCOUNT_NUMBER";
    protected static final String ACTIVITY_ID_COL = "ACTIVITY_ID";
    protected static final String AUCTION_ITEM_ID_COL = "AUCTION_ITEM_ID";
    protected static final String COUNT_COL = "COUNT";
    protected static final String COUNTERPARTY_COL = "COUNTERPARTY";
    protected static final String CURRENCY_CODE_COL = "CURRENCY_CODE";
    protected static final String AMOUNT_COL = "AMOUNT";
    protected static final String DATA_COL = "DATA";
    protected static final String FLAGS_COL = "FLAGS";
    protected static final String QUANTITY_COL = "QUANTITY";
    protected static final String SHIPPING_AMOUNT_COL = "SHIPPING_AMOUNT";
    protected static final String STATUS_COL = "STATUS";
    protected static final String TYPE_COL = "TYPE";
    protected static final String TIME_CREATED_COL = "TIME_CREATED";
    protected static final String TIME_UPDATED_COL = "TIME_UPDATED";
    protected static final String TRANS_DATA_MAP_ID_COL = "TRANS_DATA_MAP_ID";
    protected static final String TRANSACTION_ID_COL = "TRANSACTION_ID";

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }

}
