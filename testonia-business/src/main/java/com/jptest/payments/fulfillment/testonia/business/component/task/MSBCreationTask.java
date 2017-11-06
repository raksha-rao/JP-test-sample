package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.incentive.IncentiveInstrumentVO;
import com.jptest.incentive.IncentiveProgramVO;
import com.jptest.payments.fulfillment.testonia.bridge.IncentiveLifecycleServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.IncentiveDetails;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

/**
 * Creates funder user and performs following action on it:
 * <li>create and activate tab on user</li>
 * <li>Add MSB as FI for funder</li>
 * <li>Create MSB Programs</li>
 */
@Singleton
public class MSBCreationTask extends BaseTask<IncentiveInstrumentVO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSBCreationTask.class);

    @Inject
    private IncentiveLifecycleServBridge incentiveLifecycleServBridge;

    private final User funder;
    private final IncentiveDetails incentiveDetails;

    public MSBCreationTask(User funder, IncentiveDetails incentiveDetails) {
        this.funder = funder;
        this.incentiveDetails = incentiveDetails;
    }

    @Override
    public IncentiveInstrumentVO process(Context context) {
        // Create MSB program
        final long currentTimeInSec = System.currentTimeMillis() / 1000;
        final String programCode = "MSB$" + currentTimeInSec;

        final IncentiveProgramVO incentiveProgram = this.createMSBProgram(funder, programCode, currentTimeInSec,
        		Long.parseLong(incentiveDetails.getIncentiveAmount().getAmount()), incentiveDetails);
        LOGGER.info("Created new incentive program : {}", printValueObject(incentiveProgram));
        
        User buyer = (User) getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
        IncentiveInstrumentVO result = incentiveLifecycleServBridge.addIncentive(buyer.getAccountNumber(),
                programCode);
        LOGGER.info("Added incentive to buyer : {}", printValueObject(result));

        return result;
    }

    private IncentiveProgramVO createMSBProgram(User funder, String programCode, long currentTimeInSec,
            Long discountValue, IncentiveDetails incentiveDetails) {
        final long endTimeInSec = currentTimeInSec + 51960000L; // Keep incentive program valid for 1.5+ years
        
        boolean isPreFunded = false;
        if (incentiveDetails.getType().equals(IncentiveDetails.PRE_FUNDED_MSB_TYPE) || 
        		incentiveDetails.getType().equals(IncentiveDetails.MPSB_TYPE)) {
        	isPreFunded = true;
        }
        
        return incentiveLifecycleServBridge.createIncentiveProgram(new BigInteger(funder.getAccountNumber()),
                new BigInteger(funder.getAccountNumber()), 0L, endTimeInSec, programCode,
                "US", "en_US", "FIXED_DISCOUNT", incentiveDetails.getIncentiveAmount().getCurrencyCode(), 
                discountValue, new Currency(incentiveDetails.getIncentiveAmount().getCurrencyCode(), 
                		Long.parseLong(incentiveDetails.getIncentiveAmount().getAmount())), isPreFunded);
    }
}
