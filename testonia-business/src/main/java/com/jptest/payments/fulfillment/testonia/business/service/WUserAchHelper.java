package com.jptest.payments.fulfillment.testonia.business.service;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.dao.cloc.WUserAchDAO;


public class WUserAchHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(WUserAchHelper.class);

    @Inject
    private WUserAchDAO wUserAchDao;

    /**
     * Update ACH Status for the given account number
     *
     * @param accountNumber
     * @return
     */
    public void markBankAsBad(final String accountNumber) {

        final String achId = this.wUserAchDao.getAchId(accountNumber);

        this.wUserAchDao.updateAchStatus(achId);

        LOGGER.info("Updated ACH id : {}", achId);
    }
}
