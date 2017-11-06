package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.model.money.PActivityTransMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WPaymentExtensionDataDTO;

/**
 * Represents WPAYMENT_EXTENSION_DATA table of MONEY database
 */
@Singleton
public class WPaymentExtensionDataP1Dao extends MoneyDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(WPaymentExtensionDataP1Dao.class);

    private static final String GET_PAYMENT_EXTENSION_DATA_DETAILS_QUERY = "SELECT * FROM WPAYMENT_EXTENSION_DATA where activity_id in ({activityIds}) order by time_created";
    private static final String ACTIVITY_ID_REPLACEMENT_TOKEN = "{activityIds}";
    private static final String EXTENSION_DATA_COL = "EXTENSION_DATA";
    private static final String PAYER_ACCOUNT_NUMBER_COL = "PAYER_ACCOUNT_NUMBER";

    /**
     * Queries WPAYMENT_EXTENSION_DATA table for input transaction
     * @param paymentActivityTransactionMap
     */
    public List<WPaymentExtensionDataDTO> getWPaymentExtensionDataDetails(
            List<PActivityTransMapDTO> paymentActivityTransactionMap) {
        String query = GET_PAYMENT_EXTENSION_DATA_DETAILS_QUERY.replace(ACTIVITY_ID_REPLACEMENT_TOKEN,
                mapIdToString(paymentActivityTransactionMap,
                        paymentActivityTransaction -> paymentActivityTransaction.getActivityId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WPaymentExtensionDataDTO> paymentExtensionDataList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WPaymentExtensionDataDTO paymentExtensionData = new WPaymentExtensionDataDTO();
            paymentExtensionData.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            try {
                paymentExtensionData.setExtensionData(blobToString(result.get(EXTENSION_DATA_COL)));
            } catch (IOException e) {
                LOGGER.error("Error occurred getting extension data ", e);
            }
            paymentExtensionData.setPayerAccountNumber(getBigInteger(result.get(PAYER_ACCOUNT_NUMBER_COL)));
            paymentExtensionDataList.add(paymentExtensionData);
        }
        return paymentExtensionDataList;
    }

}
