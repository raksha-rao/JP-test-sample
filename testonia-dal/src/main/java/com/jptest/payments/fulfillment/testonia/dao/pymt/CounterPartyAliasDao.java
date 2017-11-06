package com.jptest.payments.fulfillment.testonia.dao.pymt;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.CounterPartyAliasDTO;

/**
 * DAO for table COUNTERPARTY_ALIAS in PYMT DB
 * 
 */
public class CounterPartyAliasDao extends PymtDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CounterPartyAliasDao.class);
	
    // Column names for COUNTERPARTY_ALIAS table
    protected static final String PAYMENT_SIDE_ID_COL = "PAYMENT_SIDE_ID";
    protected static final String ALIAS_VALUE_COL = "ALIAS_VALUE";
    protected static final String ALIAS_TYPE_COL = "ALIAS_TYPE";
    
    private static final String GET_COUNTERPARTY_ALIAS_QUERY = 
    		"SELECT * FROM COUNTERPARTY_ALIAS where PAYMENT_SIDE_ID in ({paymentSideId})";
    
    private static final String PAYMENT_SIDE_ID_TOKEN = "{paymentSideId}";
    
    /**
     * Queries COUNTERPARTY_ALIAS table for input payment txn
     * @param orders
     * @return
     */
    public List<CounterPartyAliasDTO> getCounterPartyAliasDetails(final List<WTransactionDTO> transactions) {
        List<CounterPartyAliasDTO> aliasValues = new ArrayList<CounterPartyAliasDTO>();
        if (transactions != null) {
        	List<String> paymentSideIds = new ArrayList<String>();
        	for (WTransactionDTO transaction : transactions) {
        		paymentSideIds.add(transaction.getId().toString());
        	}
            String query = GET_COUNTERPARTY_ALIAS_QUERY.replace(PAYMENT_SIDE_ID_TOKEN,
                    mapIdToString(paymentSideIds, paymentSideId -> paymentSideId));
            List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
            for (Map<String, Object> result : queryResult) {
            	aliasValues.add(mapAliasValue(result));
            }
        }
        LOGGER.debug("Found counterparty alias values:{}", aliasValues.size());
        return aliasValues;
    }
    
    private CounterPartyAliasDTO mapAliasValue(Map<String, Object> result) {
    	CounterPartyAliasDTO alias = new CounterPartyAliasDTO();
    	alias.setPaymentSideId(getBigInteger(result.get(PAYMENT_SIDE_ID_COL)));
    	alias.setAliasValue(getString(result.get(ALIAS_VALUE_COL)));
    	alias.setAliasType(getString(result.get(ALIAS_TYPE_COL)));
        return alias;
    }

}
