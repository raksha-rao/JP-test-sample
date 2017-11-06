package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;

import com.jptest.money.FulfillPaymentResponse;
import com.jptest.payments.fulfillment.testonia.business.service.AMQAssistant;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Gets AMQ messageIds for Credit and Debit payloads from FP response for 1.0 transactions
 * <p>
 * It then compares the Credit and Debit payloads with golden file and displays difference, if any.
 */
public class PayloadValidationP1Asserter extends PayloadValidationBaseAsserter {

    @Inject
    private AMQAssistant amqAssistant;

    public PayloadValidationP1Asserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    /**
     * Gets messageIds for Credit and Debit AMQ messages from FP response
     *   
     * @param activityDetails
     * @return
     */
    @Override
    protected List<String> getMessageIds(Context context) {
        FulfillPaymentResponse fulfillPaymentResponse = (FulfillPaymentResponse) getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());
        List<String> messageIds = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(fulfillPaymentResponse.getTransactionUnitStatus())
                && fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails() != null) {
            messageIds
                    .add(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails()
                            .getCreditSideHandle());
            messageIds
                    .add(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails()
                            .getDebitSideHandle());
        }

        if (messageIds.isEmpty()) {
            Assert.fail("No AMQ Credit/Debit MessageId found in FP response");
        }
        return messageIds;
    }

    /**
     * Splits messageId string into  messageType, messageId.
     * Returns actual XML string response by making DAO call 
     * 
     * @param messageId
     * @return 
     */
    protected String getActualResponseXmlString(String messageId) {
        return amqAssistant.getPayloadStringByMsgIdAndType(messageId, "TXNA");
    }
}
