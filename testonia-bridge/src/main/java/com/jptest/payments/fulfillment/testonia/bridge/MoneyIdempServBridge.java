package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.CheckUniqueIdConstraintRequest;
import com.jptest.money.CheckUniqueIdConstraintResponse;
import com.jptest.money.ClaimRequest;
import com.jptest.money.ClaimResponse;
import com.jptest.money.FulfillmentUniqueIdParticipant;
import com.jptest.money.IdempotencyParticipant;

/**
 * Represents bridge for moneyidempotencyserv,  API bridge for idempotency consume and Unique Id Check
 */

@Singleton
public class MoneyIdempServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoneyIdempServBridge.class);

    @Inject
    @Named("moneyidempserv")
    private IdempotencyParticipant idempotencyParticipant;

    @Inject
    @Named("moneyidempserv")
    private FulfillmentUniqueIdParticipant uniqueIdParticipant;

    /**
     *      Calls claim API on moneyidempotencyserv
    		* @param  request ClaimRequest
    		* @return ClaimResponse 
     */
    public ClaimResponse idempotencyConsume(ClaimRequest request) {
        LOGGER.info("idempotency claim request: {}", printValueObject(request));
        ClaimResponse result = idempotencyParticipant.claim(request);
        LOGGER.info("idempotency claim response: {}", printValueObject(result));
        return result;
    }

    /**
     *      Calls check_unique_id_constraint API on moneyidempotencyserv
            * @param  request CheckUniqueIdConstraintRequest
            * @return CheckUniqueIdConstraintResponse 
     */
    public CheckUniqueIdConstraintResponse checkUniqueId(CheckUniqueIdConstraintRequest request) {
        LOGGER.info("unique id request: {}", printValueObject(request));
        CheckUniqueIdConstraintResponse result = uniqueIdParticipant.check_unique_id_constraint(request);
        LOGGER.info("unique id response: {}", printValueObject(result));
        return result;
    }

}
