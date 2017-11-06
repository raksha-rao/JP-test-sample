package com.jptest.payments.fulfillment.testonia.dao.txn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WasOrderDTO;

/**
 * Represents WAS_ORDER_P2 table of TXN database
 */
@Singleton
public class WasOrderP2Dao extends TxnDao {

    private static final String GET_WAS_ORDER_P2_DETAILS_QUERY = 
    		"SELECT * FROM WAS_ORDER_P2 where ACCOUNT_NUMBER='{senderAccountNumber}' order by db_ts_created,id";
    private static final String GET_ORDER_STATUS = 
    		"SELECT * FROM WAS_ORDER_P2 where ACCOUNT_NUMBER='{senderAccountNumber}'";
    private static final String ORDER_STATUS_COL = "STATUS";
    private static final String ACCOUNT_NUMBER_TOKEN = "{senderAccountNumber}";
    
    // common tokens
    protected static final String COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN = "{commaSeparatedIds}";
    protected static final String SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{senderAccountNumber}";
    
    private static final String DATABASE_NAME = "TXN";
    
    // Column names for ORDER table
    protected static final String ID_COL = "ID";
    protected static final String ACCOUNT_NUMBER_COL = "ACCOUNT_NUMBER";
    protected static final String ACTIVITY_ID_COL = "ACTIVITY_ID";
    protected static final String AMOUNT_COL = "AMOUNT";
    protected static final String BUFS_TYPE_COL = "BUFS_TYPE";
    protected static final String COUNT_COL = "COUNT";
    protected static final String COUNTERPARTY_COL = "COUNTERPARTY";
    protected static final String CURRENCY_CODE_COL = "CURRENCY_CODE";
    protected static final String FLAGS_COL = "FLAGS";
    protected static final String FS_TYPE_COL = "FS_TYPE";
    protected static final String MESSAGE_ID_COL = "MESSAGE_ID";
    protected static final String STATUS_COL = "STATUS";
    protected static final String TYPE_COL = "TYPE";
    protected static final String TIME_CREATED_COL = "TIME_CREATED";

    /**
     * Queries WAS_ORDER_P2 table for input order
     * @param orders
     * @return
     */
    public List<WasOrderDTO> getWasOrderP2Details(final List<WTransactionDTO> transactions) {
    	List<WasOrderDTO> wasOrders = new ArrayList<>();
    	Map<BigInteger, Boolean> accountNumbersQueried = new HashMap<>();
    	for (WTransactionDTO transaction : transactions) {
    		// Skip account numbers for which we have already queried was_order_p2
    		if (accountNumbersQueried.containsKey(transaction.getAccountNumber())) {
    			continue;
    		}
    		
	        String query = GET_WAS_ORDER_P2_DETAILS_QUERY.replace(SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
	        		transaction.getAccountNumber().toString());
	        accountNumbersQueried.put(transaction.getAccountNumber(), true);
	        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
	        for (Map<String, Object> result : queryResult) {
	        	WasOrderDTO wasOrder = new WasOrderDTO();
	        	wasOrder.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
	        	wasOrder.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
	        	wasOrder.setAmount(getLong(result.get(AMOUNT_COL)));
	        	wasOrder.setBufsType(getString(result.get(BUFS_TYPE_COL)));
	        	wasOrder.setCounterParty(getBigInteger(result.get(COUNTERPARTY_COL)));
	        	wasOrder.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
	        	wasOrder.setFlags(getLong(result.get(FLAGS_COL)));
	        	wasOrder.setBufsType(getString(result.get(FS_TYPE_COL)));
	        	wasOrder.setMessageId(getBigInteger(result.get(MESSAGE_ID_COL)));
	        	wasOrder.setStatus(getString(result.get(STATUS_COL)));
	        	wasOrder.setType(getString(result.get(TYPE_COL)));
	        	wasOrders.add(wasOrder);
	        }
    	}
        return wasOrders;
    }

    public String getOrderStatus(String accountNumber, String reasonCode) {
        String selectQuery = GET_ORDER_STATUS.replace(ACCOUNT_NUMBER_TOKEN, accountNumber);
        Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), selectQuery);
        return result != null ? getString(result.get(ORDER_STATUS_COL)) : null;
    }

	@Override
	protected String getDatabaseName() {
		return DATABASE_NAME;
	}
}
