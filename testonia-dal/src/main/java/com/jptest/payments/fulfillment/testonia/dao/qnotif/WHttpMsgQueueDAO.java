package com.jptest.payments.fulfillment.testonia.dao.qnotif;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import com.jptest.payments.fulfillment.testonia.model.money.NotificationDTO;

/**
 * DAO for whttp_msg_queue table of QNOTIF DB.
 */
@Singleton
public class WHttpMsgQueueDAO extends QnotifDao {

    // Filter using encrypted payment id, account number and order by time created (descending)
    private static final String GET_IPN_CONTENT_USING_ENC_ID_QUERY =
            "select * from "
                    + "(select UTL_RAW.CAST_TO_VARCHAR2(http_body) as content, time_created "
                    + "from whttp_msg_queue "
                    + "where payment_id_encrypted = '{encryptedId}' "
                    + "and status is not null and target is not null "
                    + "union "
                    + "select UTL_RAW.CAST_TO_VARCHAR2(http_body) as content, time_created "
                    + "from whttp_msg_queue_archive "
                    + "where payment_id_encrypted = '{encryptedId}' "
                    + "and status is not null and target is not null) "
                    + "order by time_created desc";

    private static final String ENCRYPTED_TXN_ID_REPLACEMENT_TOKEN = "{encryptedId}";
    private static final String CONTENT_COL = "CONTENT";
    private static final String TIME_CREATED_COL = "TIME_CREATED";

    /**
     * Return latest notification for given encrypted payment id and account number.
     *
     * The query returns data in descending order of time created
     *
     * @param wTransactionVO
     * @return
     */
    public List<NotificationDTO> getNotificationContentUsingEncryptedId(String encryptedTxnId) {

        List<NotificationDTO> notifications = new ArrayList<>();

        String query = GET_IPN_CONTENT_USING_ENC_ID_QUERY
                .replace(ENCRYPTED_TXN_ID_REPLACEMENT_TOKEN, encryptedTxnId);

        List<Map<String, Object>> dbResults = dbHelper.executeSelectQuery(getDatabaseName(), query);

        // Query returns data in descending order of time created
        for (Map<String, Object> dbResult : dbResults) {
            NotificationDTO notification = new NotificationDTO(
                    (String) dbResult.get(CONTENT_COL),
                    (String) dbResult.get(TIME_CREATED_COL));
            notifications.add(notification);
        }

        return notifications;
    }
}
