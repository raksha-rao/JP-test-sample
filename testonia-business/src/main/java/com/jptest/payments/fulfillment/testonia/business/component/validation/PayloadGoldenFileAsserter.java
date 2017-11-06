package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.MoneyEventWriteStatusVO;
import com.jptest.payments.fulfillment.testonia.business.service.AMQAssistant;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.ActivityDetails;

/**
 * Gets AMQ messageIds for Credit and Debit payloads from activity-logs for 2.0 transactions
 * <p>
 * It then compares the Credit and Debit payloads with golden file and displays difference, if any.
 */
public class PayloadGoldenFileAsserter extends PayloadValidationBaseAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadGoldenFileAsserter.class);

    private static final String PUBLISH_LEGACY_EVENTS_TASK = "PUBLISH_LEGACY_EVENTS";
    private static final String MONEY_EVENT_RESPONSE = "money_event_pub_info_response";
    private static final String TXNA_MESSAGE_TYPE = "TXNA-";

    @Inject
    private AMQAssistant amqAssistant;

    public PayloadGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    /**
     * Gets messageIds for Credit and Debit AMQ messages from activity-logs
     *   
     * @param activityDetails
     * @return
     */
    @Override
    protected List<String> getMessageIds(Context context) {
        List<String> messageIds = new ArrayList<String>();
        ActivityDetails activityDetails = (ActivityDetails) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_ACTIVITY_DETAILS_KEY.getName());

        activityDetails.getTaskList().get(PUBLISH_LEGACY_EVENTS_TASK).forEach(taskDetails -> {
            if (taskDetails == null) {
                Assert.fail("No PUBLISH_LEGACY_EVENTS task for activity_id:" + activityDetails.getActivityId());
            }

            if (taskDetails.getTaskVO() == null || taskDetails.getTaskVO().getResponse() == null) {
                Assert.fail("No ResponseVO found for PUBLISH_LEGACY_EVENTS task for activity_id:"
                        + activityDetails.getActivityId());
            }
            List<MoneyEventWriteStatusVO> moneyEvents = (List<MoneyEventWriteStatusVO>) taskDetails.getTaskVO()
                    .getResponse().get(MONEY_EVENT_RESPONSE);
            if (moneyEvents == null || moneyEvents.isEmpty()) {
                Assert.fail("No MoneyEventWriteStatusVO found for PUBLISH_LEGACY_EVENTS task for activity_id:"
                        + activityDetails.getActivityId());
            }



            for (MoneyEventWriteStatusVO moneyEvent : moneyEvents) {
                if (isDebitOrCreditMessage(moneyEvent)) {
                    LOGGER.debug("AMQ MessageId:{}", moneyEvent.getMessageId());
                    messageIds.add(moneyEvent.getMessageId());
                }
            }
            if (messageIds.isEmpty()) {
                Assert.fail("No AMQ Credit/Debit MessageId found for PUBLISH_LEGACY_EVENTS task for activity_id:"
                        + activityDetails.getActivityId());
            }


    });
        return messageIds;
    }

    private static boolean isDebitOrCreditMessage(MoneyEventWriteStatusVO moneyEvent) {
        return moneyEvent.getMessageId() != null && moneyEvent.getMessageId().contains(TXNA_MESSAGE_TYPE);
    }

    /**
     * Splits messageId string into  messageType, subqueueId, dqTime and messageId.
     * Returns actual XML string response by making DAO call 
     * 
     * @param messageId
     * @return 
     */
    protected String getActualResponseXmlString(String messageId) {
        return amqAssistant.getPayloadStringByMsgId(messageId);
    }
}
