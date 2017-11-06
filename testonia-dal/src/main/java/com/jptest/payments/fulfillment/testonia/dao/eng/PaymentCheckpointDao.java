package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.PaymentCheckpointDTO;

/**
 * Represents PAYMENT_CHECKPOINT table in ENG DB
 */
@Singleton
public class PaymentCheckpointDao extends EngDao {

    private static final String GET_RECORD_BY_ACTIVITY_ID = "SELECT * FROM PAYMENT_CHECKPOINT where activity_id in ({activityIds})";

    private static final String ALIAS_TYPE_CODE_COL = "ALIAS_TYPE_CODE";
    private static final String FLAGS01_COL = "FLAGS01";
    private static final String FLAGS02_COL = "FLAGS02";
    private static final String FLAGS03_COL = "FLAGS03";
    private static final String FLAGS04_COL = "FLAGS04";
    private static final String FLAGS05_COL = "FLAGS05";
    private static final String FLAGS06_COL = "FLAGS06";
    private static final String FUNDING_SOURCE_NAME_COL = "FUNDING_SOURCE_NAME";
    private static final String REASON_CODE_COL = "REASON_CODE";
    private static final String STATUS_CODE_COL = "STATUS_CODE";
    private static final String SUBTYPE_CODE_COL = "SUBTYPE_CODE";
    private static final String TYPE_CODE_COL = "TYPE_CODE";

    /**
     * Returns list of records matching input activityId
     * 
     * @param activityId
     * @return
     */
    public List<PaymentCheckpointDTO> getRecordsByActivityId(BigInteger activityId) {
        String query = GET_RECORD_BY_ACTIVITY_ID.replace(COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<PaymentCheckpointDTO> paymentCheckpoints = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            PaymentCheckpointDTO paymentCheckpoint = new PaymentCheckpointDTO();
            paymentCheckpoint.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            paymentCheckpoint.setAliasTypeCode(getByte(result.get(ALIAS_TYPE_CODE_COL)));
            paymentCheckpoint.setFlags1(getLong(result.get(FLAGS01_COL)));
            paymentCheckpoint.setFlags2(getLong(result.get(FLAGS02_COL)));
            paymentCheckpoint.setFlags3(getLong(result.get(FLAGS03_COL)));
            paymentCheckpoint.setFlags4(getLong(result.get(FLAGS04_COL)));
            paymentCheckpoint.setFlags5(getBigInteger(result.get(FLAGS05_COL)));
            paymentCheckpoint.setFlags6(getBigInteger(result.get(FLAGS06_COL)));
            paymentCheckpoint.setFundingSourceName(getString(result.get(FUNDING_SOURCE_NAME_COL)));
            paymentCheckpoint.setReasonCode(getString(result.get(REASON_CODE_COL)));
            paymentCheckpoint.setStatusCode(getByte(result.get(STATUS_CODE_COL)));
            paymentCheckpoint.setSubtypeCode(getByte(result.get(SUBTYPE_CODE_COL)));
            paymentCheckpoint.setTypeCode(getByte(result.get(TYPE_CODE_COL)));
            paymentCheckpoints.add(paymentCheckpoint);
        }
        return paymentCheckpoints;
    }
}
