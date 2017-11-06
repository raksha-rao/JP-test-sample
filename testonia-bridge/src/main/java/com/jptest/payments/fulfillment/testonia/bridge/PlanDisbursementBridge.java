package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.DisbursementPlanning;
import com.jptest.money.PlanDisbursementRequest;
import com.jptest.money.PlanDisbursementResponse;

/**
 * Represents bridge for MPS plan_disbursement API call
 */
@Singleton
public class PlanDisbursementBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanDisbursementBridge.class);

    @Inject
    @Named("moneyplanningserv_ca")
    private DisbursementPlanning disbursementPlanningClient;


    /**
     * Call to planDisbursement() operation 
     * @param planDisbursementRequest
     * @return
     */
    public PlanDisbursementResponse planDisbursement(PlanDisbursementRequest request) {
    	LOGGER.info("PlanDisbursement request: {}", printValueObject(request));
        PlanDisbursementResponse response = disbursementPlanningClient.plan_disbursement(request);
        LOGGER.info("PlanDisbursement response: {}", printValueObject(response));
        return response;
    }
    
}
