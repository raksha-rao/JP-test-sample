package com.jptest.payments.fulfillment.testonia.dao.money;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.BaseDao;

/**
 * This class serves as a parent class for all MONEY DB related DAO classes
 * It holds all common items such as common methods, common fields etc for MONEY DB related queries.
 */
@Singleton
public abstract class MoneyDao extends BaseDao {

    private static final String DATABASE_NAME = "MONEY";

    // common tokens
    protected static final String COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN = "{commaSeparatedIds}";
    protected static final String SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{senderAccountNumber}";
    protected static final String AUCTION_ITEM_ID_REPLACEMENT_TOKEN = "{auctionItemIds}";

    // Column names common across tables
    protected static final String ACTIVITY_ID_COL = "ACTIVITY_ID";
    protected static final String ID_COL = "ID";
    protected static final String TYPE_COL = "TYPE";
    protected static final String STATUS_COL = "STATUS";

    protected static final String ACCOUNT_NUMBER_COL = "ACCOUNT_NUMBER";
    protected static final String AMOUNT_COL = "AMOUNT";
    protected static final String AUCTION_ITEM_ID_COL = "AUCTION_ITEM_ID";
    protected static final String BA_ID_COL = "BA_ID";
    protected static final String BALANCE_COL = "BALANCE";
    protected static final String BID_AMOUNT_COL = "BID_AMOUNT";
    protected static final String BUFS_ID_COL = "BUFS_ID";
    protected static final String BUFS_TYPE_COL = "BUFS_TYPE";
    protected static final String COLLATERAL_AMOUNT_COL = "COLLATERAL_AMOUNT";
    protected static final String COUNTERPARTY_COL = "COUNTERPARTY";
    protected static final String COUNTRY_CODE_COL = "COUNTRY_CODE";
    protected static final String CPARTY_TRANS_ID_COL = "CPARTY_TRANS_ID";
    protected static final String CURRENCY_CODE_COL = "CURRENCY_CODE";
    protected static final String DATA_COL = "DATA";
    protected static final String EXCHANGE_RATE_COL = "EXCHANGE_RATE";
    protected static final String FLAGS_COL = "FLAGS";
    protected static final String FS_ID_COL = "FS_ID";
    protected static final String FS_TYPE_COL = "FS_TYPE";
    protected static final String INVOICE_COL = "INVOICE";
    protected static final String MEMO_COL = "MEMO";
    protected static final String MERCHANT_ACCOUNT_NUMBER_COL = "MERCHANT_ACCOUNT_NUMBER";
    protected static final String QUANTITY_COL = "QUANTITY";
    protected static final String SHIPPING_AMOUNT_COL = "SHIPPING_AMOUNT";
    protected static final String TIME_CREATED_COL = "TIME_CREATED";
    protected static final String TIME_UPDATED_COL = "TIME_UPDATED";
    protected static final String TOTAL_FEE_AMOUNT_COL = "TOTAL_FEE_AMOUNT";
    protected static final String TRANS_DATA_MAP_ID_COL = "TRANS_DATA_MAP_ID";
    protected static final String TRANSACTION_ID_COL = "TRANSACTION_ID";
    protected static final String RELATED_TRANSACTION_ID_COL = "RELATED_TRANSACTION_ID";
    protected static final String ASSOCIATION_TYPE_COL = "ASSOCIATION_TYPE";

    protected static final String COUNT_COL = "CNT";

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }

}
