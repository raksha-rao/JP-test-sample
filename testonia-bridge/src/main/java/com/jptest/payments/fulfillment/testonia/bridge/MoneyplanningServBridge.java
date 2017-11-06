package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.AgreementPlanning;
import com.jptest.money.DisbursementPlanning;
import com.jptest.money.MoneyPlanning;
import com.jptest.money.PlanAgreementRequest;
import com.jptest.money.PlanAgreementResponse;
import com.jptest.money.PlanDisbursementRequest;
import com.jptest.money.PlanDisbursementResponse;
import com.jptest.money.PlanPaymentV2Request;
import com.jptest.money.PlanPaymentV2Response;
import com.jptest.money.PlanPayoffTabV2Request;
import com.jptest.money.PlanPayoffTabV2Response;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;


/**
 * MoneyplanningServBridge represents mps plan_payment_v2, plan_disbursement operations, checks
 * for decline codes in response and catches operational exceptions.
 *
 * @JP Inc.
 *
 */
@Singleton
public class MoneyplanningServBridge {

    private static final Logger logger = LoggerFactory.getLogger(MoneyplanningServBridge.class);

    @Inject
    @Named("moneyplanningserv_ca")
    private MoneyPlanning moneyPlanningClient;

    @Inject
    @Named("moneyplanningserv_ca")
    private DisbursementPlanning disbursementPlanningClient;
    
    @Inject
    @Named("moneyplanningserv_ca")
    private AgreementPlanning agreementPlanningClient;

    public PlanPaymentV2Response planPaymentV2(PlanPaymentV2Request rq) {
        logger.info("PlanPaymentV2Request {}", printValueObject(rq));
        PlanPaymentV2Response rs;
        try {

            rs = moneyPlanningClient.plan_payment_v2(rq);

            logger.info("PlanPaymentV2Response {}", printValueObject(rs));
            if (CollectionUtils.isNotEmpty(rs.getMessages())
                    && rs.getMessages().get(0).getDeclineReasonCodeAsEnum() != null) {
                throw new TestExecutionException("Failed during MPS call plan_payment_v2 with "
                        + rs.getMessages().get(0).getDeclineReasonCodeAsEnum());
            }
            return rs;

        }
        catch (Exception ex) {
            throw new TestExecutionException("Failed during MPS call plan_payment_v2 invocation:" + ex.getMessage(), ex);
        }
    }

    /**
     * Planning call to disburse the money from delayed queue to merchant holding or partner GL.
     * @param {@link PlanDisbursementRequest} rq
     * @return {@link PlanDisbursementResponse}
     */
    public PlanDisbursementResponse planDisbursement(PlanDisbursementRequest rq) {
        logger.info("PlanDisbursementRequest {}", printValueObject(rq));
        PlanDisbursementResponse rs;
        try {
            rs = disbursementPlanningClient.plan_disbursement(rq);

            logger.info("PlanDisbursementResponse {}", printValueObject(rs));
            if (CollectionUtils.isNotEmpty(rs.getMessages())
                    && rs.getMessages().get(0).getDeclineReasonCodeAsEnum() != null) {
                throw new TestExecutionException("Failed during MPS call plan_disbursement with " + rs.getMessages()
                        .get(0).getDeclineReasonCodeAsEnum());
            }
            return rs;

        } catch (Exception ex) {
            throw new TestExecutionException("Failed during MPS call plan_disbursement invocation:" + ex.getMessage(), ex);
        }
    }
    
    /**
     * Call to plan_agreement() operation 
     * @param request
     * @return
     */
    public PlanAgreementResponse planAgreement(PlanAgreementRequest request) {
    	logger.info("plan_agreement request: {}", printValueObject(request));
        PlanAgreementResponse response = agreementPlanningClient.plan_agreement(request);
        logger.info("plan_agreement response: {}", printValueObject(response));
        return response;
    }

	/**
	 * Planning call to pay off TAB
	 *
	 * @param {@link
	 * 			PlanPayoffTabV2Request} request
	 * @return {@link PlanPayoffTabV2Response}
	 */
	public PlanPayoffTabV2Response planPayoffTabV2(PlanPayoffTabV2Request request) {
		logger.info("PlanDisbursementRequest {}", printValueObject(request));
		PlanPayoffTabV2Response response;
		try {
			response = moneyPlanningClient.plan_payoff_tab_v2(request);
			logger.info("PlanPayoffTabV2Response: {}", printValueObject(response));
		} catch (Exception ex) {
			throw new TestExecutionException("MPS::plan_payoff_tab_v2 invocation failed with error: " + ex.getMessage(),
					ex);
		}

		return response;
	}
}
