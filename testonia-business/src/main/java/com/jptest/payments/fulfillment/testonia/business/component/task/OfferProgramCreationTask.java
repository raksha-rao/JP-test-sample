package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.interfaces.rs.exception.OfferException;
import com.jptest.interfaces.rs.resources.ApplicabilityRuleVO;
import com.jptest.interfaces.rs.resources.CartRuleVO;
import com.jptest.interfaces.rs.resources.ComputationRuleVO;
import com.jptest.interfaces.rs.resources.LocalizedAttributesVO;
import com.jptest.interfaces.rs.resources.OfferProgramVO;
import com.jptest.interfaces.rs.resources.OfferValueVO;
import com.jptest.offers.common.enums.DiscountLevel;
import com.jptest.offers.common.enums.FundingType;
import com.jptest.offers.common.enums.OfferApplicableLevel;
import com.jptest.offers.common.enums.OfferType;
import com.jptest.offers.common.enums.RedemptionChannel;
import com.jptest.offers.common.enums.ValueType;
import com.jptest.payments.fulfillment.testonia.bridge.OfferServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.UserLifecycleServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.model.IncentiveDetails;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.user.LoadUserDataResponse;


/**
 * Creates Offer Program and adds Incentive as FI for funder
 */
public class OfferProgramCreationTask extends BaseTask<OfferProgramVO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferProgramCreationTask.class);

    @Inject
    private OfferServBridge offerServBridge;

    @Inject
    private UserLifecycleServBridge userLifecycleServBridge;

    private final User funder;
    private final IncentiveDetails incentiveDetail;

    public OfferProgramCreationTask(User funder, IncentiveDetails incentiveDetail) {
        this.funder = funder;
        this.incentiveDetail = incentiveDetail;
    }

    @Override
    public OfferProgramVO process(Context context) {

        final String incentiveCurrencyCode = incentiveDetail.getIncentiveAmount().getCurrencyCode();
        final Long incentiveAmount = Long.valueOf(incentiveDetail.getIncentiveAmount().getAmount());

        final LoadUserDataResponse funderAccount = userLifecycleServBridge
                .loadUserData(new BigInteger(funder.getAccountNumber()));
        final String encryptedFunderAccNumber = funderAccount.getAccount().get(0).getEncryptedAccountNumber();

        final long currentTimeMillis = System.currentTimeMillis();
        final String offerProgramCode = incentiveDetail.getType() + "-" + (currentTimeMillis / 1000);

        LocalizedAttributesVO localizedAttributesVO = new LocalizedAttributesVO();
        localizedAttributesVO.setTitle("TE_Testing");
        localizedAttributesVO.setSubTitle("TE_Testing");
        localizedAttributesVO.setDescription("TE_Testing");

        List<LocalizedAttributesVO> localizedAttributesVOList = new ArrayList<>();
        localizedAttributesVOList.add(localizedAttributesVO);

        Set<RedemptionChannel> redemptionChannels = new HashSet<>();
        redemptionChannels.add(RedemptionChannel.ALL);

        CartRuleVO cartRuleVO = new CartRuleVO();
        cartRuleVO.setMinCartTotal(0L);
        cartRuleVO.setRedemptionChannels(redemptionChannels);

        ApplicabilityRuleVO applicabilityRuleVO = new ApplicabilityRuleVO();
        applicabilityRuleVO.setCartRule(cartRuleVO);

        OfferValueVO offerValueVO = new OfferValueVO();
        offerValueVO.setType(ValueType.FIXED);
        offerValueVO.setValue(incentiveAmount);
        offerValueVO.setMaxDiscount(incentiveAmount);

        ComputationRuleVO computationRuleVO = new ComputationRuleVO();
        computationRuleVO.setCartRule(cartRuleVO);
        computationRuleVO.setOfferValue(offerValueVO);

        List<ComputationRuleVO> computationRuleVOList = new ArrayList<>();
        computationRuleVOList.add(computationRuleVO);

        // Construct Offer Program Request
        OfferProgramVO offerProgramVO = new OfferProgramVO();

        offerProgramVO.setOfferProgramCode(offerProgramCode);           // required field
        offerProgramVO.setApplicableLevel(OfferApplicableLevel.CART);   // required field
        offerProgramVO.setDiscountLevel(DiscountLevel.ITEM_TOTAL);      // required field

        offerProgramVO.setFunderAccountNumber(encryptedFunderAccNumber);
        offerProgramVO.setCurrencyCode(incentiveCurrencyCode);
        offerProgramVO.setMaximumIssuableAmount(incentiveAmount);

        offerProgramVO.setRedemptionStartTime(new Date(currentTimeMillis));
        offerProgramVO.setRedemptionEndTime(new Date(currentTimeMillis + 1800000)); // +30min

        offerProgramVO.setLocalizedAttributes(localizedAttributesVOList);
        offerProgramVO.setApplicabilityRule(applicabilityRuleVO);
        offerProgramVO.setComputationRules(computationRuleVOList);

        switch (incentiveDetail.getType()) {
            case IncentiveDetails.MPSB_TYPE:
                offerProgramVO.setOfferType(OfferType.MPSB);
                offerProgramVO.setFundingType(FundingType.UNCHECKED);
                break;
            case IncentiveDetails.PRE_FUNDED_MSB_TYPE:
                offerProgramVO.setOfferType(OfferType.MSB);
                offerProgramVO.setFundingType(FundingType.PRE_FUNDED);
                break;
            case IncentiveDetails.POST_FUNDED_MSB_TYPE:
                offerProgramVO.setOfferType(OfferType.MSB);
                offerProgramVO.setFundingType(FundingType.POST_FUNDED);
                break;
            default:
                LOGGER.error(this.getClass().getSimpleName()
                        + ".process() - Incentive Type '{}' NOT yet supported.", incentiveDetail.getType());
                return null;
        }

        OfferProgramVO offerProgramResponse = null;
        try {
            offerProgramResponse = offerServBridge.createOfferProgram(offerProgramVO);
        }
        catch (OfferException e) {
            LOGGER.error("Exception occurred while creating Offer Program", e);
        }

        return offerProgramResponse;
    }
}
