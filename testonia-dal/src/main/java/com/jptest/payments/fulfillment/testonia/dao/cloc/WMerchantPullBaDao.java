package com.jptest.payments.fulfillment.testonia.dao.cloc;

import com.google.inject.Singleton;

/**
 * DAO for wmerchant_pull_ba table of CLOC database
 */

@Singleton
public class WMerchantPullBaDao extends ClockDao {

    private static final String BUYER_ACCOUNT_NUMBER = "{buyerAccountNumber}";
    private static final String SELLER_ACCOUNT_NUMBER = "{sellerAccountNumber}";
    
    
    private static final String UPDATE_MERCHANT_PULL = "update wmerchant_pull_ba set account_number= {buyerAccountNumber} "
            + ", merchant_account_number= {sellerAccountNumber}"
                       + " , BILL_VOLUME_AMT_TOTAL = 0,  BILL_VOLUME_AMT = 0 where mp_id=873";
    
    public void updateMerchantPullBa(final String recipientAccountNumber, final String senderAccountNumber) {
        
    final String update_query = UPDATE_MERCHANT_PULL.replace(BUYER_ACCOUNT_NUMBER, recipientAccountNumber)
           .replace(SELLER_ACCOUNT_NUMBER, senderAccountNumber);
    
    this.dbHelper.executeUpdateQuery(this.getDatabaseName(), update_query);
    
    }
}
