/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.dao.gmf;

import com.jptest.payments.fulfillment.testonia.dao.BaseDao;

/**
 * @JP Inc.
 *
 * This class serves as a parent class for all GMF DB related DAO classes
 * It holds all common items such as common methods, common fields etc for GMF DB related queries.
 *
 */
public class GmfDao extends BaseDao {

	/* (non-Javadoc)
	 * @see com.jptest.payments.fulfillment.testonia.dao.BaseDao#getDatabaseName()
	 */
	
	private static final String DATABASE_NAME = "GMF";
	
	@Override
	protected String getDatabaseName() {
		return DATABASE_NAME;
	}

}
