package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.inject.name.Named;
import com.jptest.common.DateRangeVO;
import com.jptest.incentive.AddIncentiveRequest;
import com.jptest.incentive.AddIncentiveResponse;
import com.jptest.incentive.CreateIncentiveProgramRequest;
import com.jptest.incentive.CreateIncentiveProgramResponse;
import com.jptest.incentive.DiscountTypeEnum;
import com.jptest.incentive.IncentiveInstrumentVO;
import com.jptest.incentive.IncentiveLifecycle;
import com.jptest.incentive.IncentiveLimitsVO;
import com.jptest.incentive.IncentiveOwnerVO;
import com.jptest.incentive.IncentiveProgramDescriptionVO;
import com.jptest.incentive.IncentiveProgramDiscountVO;
import com.jptest.incentive.IncentiveProgramVO;
import com.jptest.incentive.IncentiveRedemptionEnum;
import com.jptest.incentive.IncentiveTypeEnum;
import com.jptest.incentive.OwnerTypeEnum;
import com.jptest.incentive.TransferConstraintEnum;
import com.jptest.types.Currency;

/**
 * Represents bridge for IncentiveLifecycleServ API calls
 */
@Singleton
public class IncentiveLifecycleServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncentiveLifecycleServBridge.class);

    private static final String INCENTIVE_PROGRAM_NAME = "School Campaign";
    private static final String INCENTIVE_PROGRAM_TITLE = "5 OFF";
    private static final String INCENTIVE_PROGRAM_DESCRIPTION = "5 OFF";
    private static final String INCENTIVE_PROGRAM_TERMS = "Terms";
    private static final String INCENTIVE_PROGRAM_RESTRICTIONS = "Restriction";
    private static final String INCENTIVE_PROGRAM_MEMO = "Memo";

    @Inject
    @Named("incentivelifecycle")
    private IncentiveLifecycle incentiveLifecycle;

    /**
     * Creates IncentiveProgram
     *  
     * @param issuerAccountNumber e.g. 1548330495641321268
     * @param funder e.g. 1548330495641321268
     * @param startDate e.g. 0
     * @param endDate e.g. 26182818
     * @param programCode e.g. MSB1$1482525394
     * @param countryCode e.g. US
     * @param locale e.g. en_US
     * @param discountType e.g. FIXED_DISCOUNT
     * @param discountCurrency e.g. USD
     * @param discountValue
     * @param budget
     * @param preFunded
     * @return IncentiveProgramVO
     */
    public IncentiveProgramVO createIncentiveProgram(BigInteger issuerAccountNumber, BigInteger funderAccountNumber,
            Long startDate, Long endDate, String programCode, String countryCode, String locale, String discountType,
            String discountCurrency, Long discountValue, Currency budget, boolean preFunded) {
        CreateIncentiveProgramRequest request = new CreateIncentiveProgramRequest();
        // set basic info
        request.setIssuer(Arrays.asList(issuerAccountNumber));
        request.setFunder(funderAccountNumber);
        request.setIncentiveType(IncentiveTypeEnum.STORE_CREDIT);
        request.setProgramCode(programCode);
        request.setCountryCode(countryCode);

        // set date range
        request.setDateRange(new DateRangeVO());
        request.getDateRange().setLowerBound(startDate);
        request.getDateRange().setUpperBound(endDate);

        // set description
        request.setDescription(new ArrayList<>());
        request.getDescription().add(new IncentiveProgramDescriptionVO());
        request.getDescription().get(0).setLocale(locale);
        request.getDescription().get(0).setName(INCENTIVE_PROGRAM_NAME);
        request.getDescription().get(0).setTitle(INCENTIVE_PROGRAM_TITLE);
        request.getDescription().get(0).setDescription(INCENTIVE_PROGRAM_DESCRIPTION);
        request.getDescription().get(0).setTerms(INCENTIVE_PROGRAM_TERMS);
        request.getDescription().get(0).setRestrictions(INCENTIVE_PROGRAM_RESTRICTIONS);

        // set discount
        request.setDiscount(new IncentiveProgramDiscountVO());
        request.getDiscount().setDiscountType(DiscountTypeEnum.getEnumByName(discountType));
        request.getDiscount().setDiscountCurrency(discountCurrency);
        request.getDiscount().setDiscountValue(discountValue);

        // set limits
        request.setLimits(new IncentiveLimitsVO());
        request.getLimits().setBudget(budget);

        request.setRedemption(IncentiveRedemptionEnum.REDEEM_jptest);
        request.setMemo(INCENTIVE_PROGRAM_MEMO);
        request.setPreFunded(preFunded);
        request.setSharing(true);
        request.setTransferConstraint(TransferConstraintEnum.TRANSFER_USED_jptest_USER);

        return createIncentiveProgram(request);
    }

    /**
     * Creates IncentiveProgram 
     * @param request
     * @return
     */
    public IncentiveProgramVO createIncentiveProgram(CreateIncentiveProgramRequest request) {
        LOGGER.info("create_incentive_program request: {}", printValueObject(request));
        CreateIncentiveProgramResponse response = incentiveLifecycle.create_incentive_program(request);
        LOGGER.info("create_incentive_program response: {}", printValueObject(response));
        if (!response.getStatus()) {
            Assert.fail(
                    "createIncentiveProgram call failed with returnCode [" + response.getReturnCode().getReturnCode()
                            + "] and returnCodeDescription [" + response.getReturnCode().getReturnMessage() + "]");

        }

        return response.getProgram();
    }

    /**
     * Adds incentive to buyer's account
     *  
     * @param buyerAccountNumber
     * @param programCode
     * @return String incentiveId
     */
    public IncentiveInstrumentVO addIncentive(String buyerAccountNumber, String programCode) {
        AddIncentiveRequest request = new AddIncentiveRequest();
        request.setIncentiveProgram(programCode);
        // set owner
        request.setOwner(new IncentiveOwnerVO());
        request.getOwner().setOwnerId(buyerAccountNumber);
        request.getOwner().setOwnerType(OwnerTypeEnum.jptest_ACCOUNT);
        LOGGER.info("add_incentive request: {}", printValueObject(request));
        AddIncentiveResponse response = incentiveLifecycle.add_incentive(request);
        LOGGER.info("add_incentive response: {}", printValueObject(response));
        if (!response.getStatus()) {
            Assert.fail(
                    "addIncentive call failed with returnCode [" + response.getReturnCode().getReturnCode()
                            + "] and returnCodeDescription [" + response.getReturnCode().getReturnMessage() + "]");

        }
        return response.getIncentive();
    }
}
