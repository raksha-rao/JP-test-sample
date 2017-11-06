package com.jptest.payments.fulfillment.testonia.dao.pymt;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.model.pymt.PaymentSideReferenceDTO;


/**
 * DAO for PAYMENT_SIDE_REFERENCE table of PYMT (2.1) DB
 */
@Singleton
public class PaymentSideReferenceDao extends PymtDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSideReferenceDao.class);

    private static final String GET_ACTIVITY_IDS_FROM_TRANSACTION_IDS = "select distinct a.reference_value as alternate_id, b.reference_value as activity_id , a.row_created_time "
            + "from payment_side_reference a, payment_side_reference b where b.reference_type_code = 'FFAID' and a.payment_side_id = b.payment_side_id "
            + "and a.payment_side_id in ({alternateId}) order by a.row_created_time asc";

    private static final String GET_ACTIVITY_ID_FROM_TRANSACTION_ID = "select distinct b.reference_value as activity_id "
            + "from payment_side_reference a, payment_side_reference b where b.reference_type_code = 'FFAID' and a.payment_side_id = b.payment_side_id "
            + "and a.payment_side_id = {alternateId}";

    private static final String GET_PAYMENT_SIDE_REFERENCE_DETAILS_FROM_TRANSACTION_IDS = "select distinct a.reference_value as alternate_id, b.reference_value as activity_id , a.row_created_time as row_created_time, "
            + "a.payment_side_id as payment_side_id from payment_side_reference a where "
            + "a.payment_side_id in ({alternateId}) order by a.row_created_time asc";

    private static final String GET_ACTIVITY_ID_FROM_HOLDS_HANDLE_ID = "select distinct b.reference_value as activity_id "
            + "from payment_side_reference a, payment_side_reference b where b.reference_type_code = 'FFAID' and a.payment_side_id = b.payment_side_id "
            + "and a.reference_value = '{alternateId}' order by activity_id desc";

    private static final String GET_COUNT_FROM_HOLDS_HANDLE_ID = "select count(*) "
            + "from payment_side_reference a, payment_side_reference b where b.reference_type_code = 'FFAID' and a.payment_side_id = b.payment_side_id "
            + "and a.reference_value = '{alternateId}'";
    
    private static final String GET_TIME_SORTED_ACTIVITY_ID_FROM_REFERENCE_VALUE = "select distinct b.reference_value as activity_id, b.row_created_time "
            + "from payment_side_reference a, payment_side_reference b where b.reference_type_code = 'FFAID' and a.payment_side_id = b.payment_side_id "
            + "and a.reference_value = '{alternateId}' order by b.row_created_time desc";

    private static final String GET_TRANSACTION_IDS_FROM_ACTIVITY_IDS = "select distinct a.payment_side_id, a.row_created_time "
            + "from payment_side_reference a where a.reference_type_code = 'FFAID' and a.reference_value"
            + " in ({alternateId}) order by a.row_created_time, a.payment_side_id asc";

    private static final String ALTERNATE_ID_REPLACEMENT_TOKEN = "{alternateId}";

    private static final String ACTIVITY_ID_COL = "ACTIVITY_ID";

    private static final String PYMT_SIDE_ID_COL = "PAYMENT_SIDE_ID";
    /**
     * Returns activityIds for input transactions
     *
     * @param wTransactionList
     * @return
     */
    public Set<BigInteger> getActivityIdFromTransactionIds(final String commaSeparatedIds) {
        final Set<BigInteger> activitySet = new LinkedHashSet<>();
        final String query = GET_ACTIVITY_IDS_FROM_TRANSACTION_IDS.replace(ALTERNATE_ID_REPLACEMENT_TOKEN,
                commaSeparatedIds);
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        for (final Map<String, Object> result : queryResult) {
            final String activityId = (String) result.get(ACTIVITY_ID_COL);
            activitySet.add(new BigInteger(activityId));
            LOGGER.info("ACTIVITY ID from DB :" + activityId);
        }
        LOGGER.info("Found activitySet:{}", activitySet);
        return activitySet;
    }

    public BigInteger getActivityIdFromTransactionId(final String transactionId) {
        final String query = GET_ACTIVITY_ID_FROM_TRANSACTION_ID.replace(ALTERNATE_ID_REPLACEMENT_TOKEN, transactionId);
        final Map<String, Object> queryResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
                query);
        if (queryResult != null) {
            final String activityId = (String) queryResult.get(ACTIVITY_ID_COL);
            if (activityId != null) {
                LOGGER.info("ACTIVITY ID : {}, TRANSACTION ID : {} from DB : {} ", activityId, transactionId);
                return new BigInteger(activityId);
            }
        }
        return null;
    }

    public BigInteger getTransactionIdFromActivityId(final String activityId) {
        if(activityId == null)
            return null;
        final String query = GET_TRANSACTION_IDS_FROM_ACTIVITY_IDS.replace(ALTERNATE_ID_REPLACEMENT_TOKEN, activityId);
        final Map<String, Object> queryResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
                query);
        if (queryResult != null) {
            final String pymtSideID = (String) queryResult.get(PYMT_SIDE_ID_COL);
            LOGGER.info("ACTIVITY ID : {}, TRANSACTION ID : {} from DB : {} ", activityId, pymtSideID);
            return new BigInteger(pymtSideID);
        }
        return null;
    }
    public List<PaymentSideReferenceDTO> getPaymentSideRefFromTransactionIds(final Set<String> commaSeparatedIds) {
        final List<PaymentSideReferenceDTO> paymentSideReferenceDTOList = new ArrayList<>();

        final String query = GET_PAYMENT_SIDE_REFERENCE_DETAILS_FROM_TRANSACTION_IDS.replace(
                ALTERNATE_ID_REPLACEMENT_TOKEN,
                mapIdToString(commaSeparatedIds, transaction -> transaction));

        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        for (final Map<String, Object> result : queryResult) {
            paymentSideReferenceDTOList.add(this.mapPaymentSideReference(result));
        }
        LOGGER.debug("Found PaymentSideRef:{}", paymentSideReferenceDTOList.toString());
        return paymentSideReferenceDTOList;
    }

    private PaymentSideReferenceDTO mapPaymentSideReference(final Map<String, Object> result) {
        final PaymentSideReferenceDTO paymentSideReferenceDTO = new PaymentSideReferenceDTO();
        paymentSideReferenceDTO.setReferenceValue(this.getString(result.get("REFERENCE_VALUE")));
        paymentSideReferenceDTO.setReferenceTypeCode(this.getString(result.get("REFERENCE_TYPE_CODE")));
        paymentSideReferenceDTO.setPaymentSideId(this.getBigInteger(result.get("PAYMENT_SIDE_ID")));
        paymentSideReferenceDTO.setRowCreatedTime(this.getLong(result.get("ROW_CREATED_TIME")));
        return paymentSideReferenceDTO;
    }

    public BigInteger getActivityIdFromHoldsHandle(final String decryptedSubbalanceHandle) {
        final String query = GET_ACTIVITY_ID_FROM_HOLDS_HANDLE_ID.replace(ALTERNATE_ID_REPLACEMENT_TOKEN,
                decryptedSubbalanceHandle);
        final Map<String, Object> queryResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
                query);
        if (queryResult != null) {
            final String activityId = (String) queryResult.get(ACTIVITY_ID_COL);
            if (activityId != null) {
                LOGGER.info("ACTIVITY ID : {}, TRANSACTION ID : {} from DB : {} ", activityId,
                        decryptedSubbalanceHandle);
                return new BigInteger(activityId);
            }
        }
        return null;
    }

    public List<Map<String, Object>> getActivityIdsFromHoldsHandle(final String decryptedSubbalanceHandle) {
        final String query = GET_ACTIVITY_ID_FROM_HOLDS_HANDLE_ID.replace(ALTERNATE_ID_REPLACEMENT_TOKEN,
                decryptedSubbalanceHandle);
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        return queryResult;
    }

    public Integer getCountForHoldsHandle(final String decryptedSubbalanceHandle) {
        final String query = GET_COUNT_FROM_HOLDS_HANDLE_ID.replace(ALTERNATE_ID_REPLACEMENT_TOKEN,
                decryptedSubbalanceHandle);
        final Map<String, Object> queryResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
                query);
        final Integer count = Integer.parseInt(queryResult.get("COUNT(*)").toString());
        return count;
    }

    /**
     * Returns latest activityId for input payment side reference value
     *
     * @param referenceValue (payment side reference value)
     * @return
     */
    public List<BigInteger> getLatestActivityIdForReferenceValue(final String referenceValue) {
        final List<BigInteger> activityList = new ArrayList<>();
        final String query = GET_TIME_SORTED_ACTIVITY_ID_FROM_REFERENCE_VALUE.replace(ALTERNATE_ID_REPLACEMENT_TOKEN,
                referenceValue);
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        for (final Map<String, Object> result : queryResult) {
            final String activityId = (String) result.get(ACTIVITY_ID_COL);
            activityList.add(new BigInteger(activityId));
            LOGGER.info("ACTIVITY ID from DB :" + activityId);
        }
        LOGGER.info("Found activitySet:{}", activityList);
        return activityList;
    }
}
