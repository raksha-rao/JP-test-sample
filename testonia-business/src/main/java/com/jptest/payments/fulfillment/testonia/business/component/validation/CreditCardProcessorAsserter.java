package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.jptest.finsys.CasAuthorizeTransactionResponse;
import com.jptest.fulfillmentengine.TaskState;
import com.jptest.fulfillmentengine.TaskVO;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.money.Processor;
import com.jptest.payments.fulfillment.testonia.model.ActivityDetails;


/**
 * Validates the Real Time Processor of ACEAUTH or PAS_CHARGE tasks
 */
public class CreditCardProcessorAsserter extends BaseAsserter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreditCardProcessorAsserter.class);

    private static final String ACEAUTH_TASK = "ACEAUTH";

    private static final String PASCHARGE_TASK = "PAS_CHARGE";

    private Processor expectedProcessor;
    
    private static ObjectMapper mapper = new ObjectMapper();

    @Inject
    private ActivityIdRetrieverTask activityIdRetrieverTask;
    @Inject
    private ActivityDetailsRetrieverTask activityDetailsRetrieverTask;
    @Inject
    private TransactionHelper transactionHelper;

    public CreditCardProcessorAsserter(Processor processor) {
        this.expectedProcessor = processor;
    }

    @Override
    public void validate(Context context) {
        List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        doExecute(wTransactionList);
    }

    private void doExecute(List<WTransactionVO> wTransactionList) {
        // retrieve list of activities
        Set<BigInteger> activityIdSet = activityIdRetrieverTask.execute(wTransactionList);

        if (activityIdSet.isEmpty()) {
            Assert.fail(getClass().getSimpleName() + ".doExecute() - activityIdSet should not be empty");
        }

        // for each activity, get activity details
        for (BigInteger activityId : activityIdSet) {
            ActivityDetails activityDetails = activityDetailsRetrieverTask.execute(activityId);
            Assert.assertNotNull(activityDetails, getClass().getSimpleName() + ".doExecute() - actity-details is expected to be not-null");
            WTransactionVO senderTransaction = transactionHelper.getSenderTransaction(wTransactionList);
            // validate ACEAUTH/PASAUTH task
            if (!transactionHelper.isFlexTransaction(senderTransaction)) {
                validateProcessorInAceAuthTask(activityId, activityDetails);
            } else {
                validateProcessorInPasChargeTask(activityId, activityDetails);
            }

        }

    }

    /**
     * Validate that ACEAUTH task has processor details for given activityId
     *
     * @param activityId
     * @param activityDetails
     */
    private void validateProcessorInAceAuthTask(BigInteger activityId, ActivityDetails activityDetails) {
    	String realTimeTask = ACEAUTH_TASK;

        activityDetails.getTaskList().get(realTimeTask).forEach(taskDetails -> {
            Assert.assertNotNull(taskDetails, getClass().getSimpleName() + ".validateProcessorInAceAuthTask() - No "
                    + realTimeTask + "details for activity_id: " + activityId);

            TaskVO taskVO = taskDetails.getTaskVO();
            Assert.assertNotNull(taskVO, getClass().getSimpleName() + ".validateProcessorInAceAuthTask() - "
                    + realTimeTask + " task should be present.");

            if (taskVO.getStateAsEnum() != TaskState.FINISHED) {
                Assert.fail(realTimeTask + getClass().getSimpleName() + ".validateProcessorInAceAuthTask() "
                        + "- task is not in Finished state for activity " + activityId);
            }

            String jsonResponse = taskVO.getJsonResponse();
            if (jsonResponse == null) {
                Assert.fail(getClass().getSimpleName() + ".validateProcessorInAceAuthTask() - "
                        + "jsonResponse message is null for " + realTimeTask + " task of activity " + activityId);
            }

            CasAuthorizeTransactionResponse response = (CasAuthorizeTransactionResponse) taskVO.getResponse();
            String actualProcessor = response.getResult().getCctransDataVo().get(0).getCctrans().getRealtimeProcessor();
            Assert.assertEquals(actualProcessor, expectedProcessor.getValue(),
                    this.getClass().getSimpleName() + ".validateRTTaskProcessor() failed for the expected Processor " + expectedProcessor.getValue()
                            + " but actual Processor is " + actualProcessor);
        });
    }
    
    /**
     * Validate that PAS_CHARGE task has expected processor in JSON response object
     *  
     * @param activityId
     * @param activityDetails
     */
    private void validateProcessorInPasChargeTask(BigInteger activityId, ActivityDetails activityDetails) {
    	String realTimeTask = PASCHARGE_TASK;

        activityDetails.getTaskList().get(realTimeTask).forEach(taskDetails -> {
        Assert.assertNotNull(taskDetails, getClass().getSimpleName() + ".validateProcessorInPasChargeTask() - "
        		+ "No " + realTimeTask + "details for activity_id: " + activityId);
        
        TaskVO taskVO = taskDetails.getTaskVO();
        Assert.assertNotNull(taskVO, getClass().getSimpleName() + ".validateProcessorInPasChargeTask() - "
        				+ realTimeTask + " task should be present.");
        
        if (taskVO.getStateAsEnum() != TaskState.FINISHED) {
            Assert.fail(realTimeTask + getClass().getSimpleName() + ".validateProcessorInPasChargeTask() "
            		+ "- task is not in Finished state for activity " + activityId);
        }
        
        String jsonResponse = taskVO.getJsonResponse();
        if (jsonResponse == null) {
            Assert.fail(getClass().getSimpleName() + ".validateProcessorInPasChargeTask() - "
            		+ "jsonResponse message is null for " + realTimeTask + " task of activity " + activityId);
        }
        
        String actualProcessor = null;
        
		try {
			JsonNode responseNode = mapper.readTree(jsonResponse);
			actualProcessor = responseNode.get("simplePaymentMessage").get("paymentInstruction")
					.get(0).get("acquirerContext").get("acquiringProcessor").asText();
		} catch (Exception e) {
			LOGGER.error("Unable to retrieve processorId from json message {}", jsonResponse, e);
		}
        if (actualProcessor != null) {
            Assert.assertEquals(actualProcessor, expectedProcessor.getName(),
                    this.getClass().getSimpleName() + ".validateRTTaskProcessor() failed for the expected Processor " + expectedProcessor.getName()
                            + " but actual Processor is " + actualProcessor);
        }
        });
    }

}
