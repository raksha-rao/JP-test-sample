package com.jptest.payments.fulfillment.testonia.business.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.dao.cloc.WUserDAO;

@Singleton
public class WUserHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(WUserHelper.class);

    @Inject
    private WUserDAO wUserDao;

    /**
     * Update first name for given account number
     * 
     * @param accountNumber
     * @param firstName
     * @return
     */
    public void updateFirstName(final String accountNumber, final String firstName) {
    	wUserDao.updateFirstName(accountNumber, firstName);
    	
    	LOGGER.info("First name updated to: ", firstName, " for account# : {}", accountNumber);
    }
}
