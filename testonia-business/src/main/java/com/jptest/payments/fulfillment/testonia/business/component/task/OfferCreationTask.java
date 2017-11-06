package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.interfaces.rs.resources.OfferProgramVO;
import com.jptest.interfaces.rs.resources.OfferVO;
import com.jptest.offers.common.enums.OfferStatus;
import com.jptest.offers.common.enums.UserIdType;
import com.jptest.offers.common.enums.ValueType;
import com.jptest.payments.fulfillment.testonia.bridge.OfferServBridge;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * Creates Offer
 */
public class OfferCreationTask extends BaseTask<OfferVO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferCreationTask.class);

    @Inject
    private OfferServBridge offerServBridge;

    @Inject
    private CryptoUtil cryptoUtil;

    private final User funder;
    private final String offerProgramKey;

    public OfferCreationTask(User funder, String offerProgramKey) {
        this.funder = funder;
        this.offerProgramKey = offerProgramKey;
    }

    @Override
    public OfferVO process(Context context) {

        OfferProgramVO offerProgramVO = (OfferProgramVO) context.getData(offerProgramKey);

        // Construct Offer Request
        OfferVO offerVO = new OfferVO();
        offerVO.setOwnerId(funder.getEmailAddress());
        offerVO.setOwnerIdType(UserIdType.EMAIL_ID);
        offerVO.setOfferProgramVO(offerProgramVO);
        offerVO.setOfferProgramId(offerProgramVO.getOfferProgramId());
        offerVO.setOfferType(offerProgramVO.getOfferType());
        offerVO.setLifecycleStatus(OfferStatus.ENABLED);
        offerVO.setValueType(ValueType.FIXED);
        offerVO.setRedemptionStartTime(offerProgramVO.getRedemptionStartTime());
        offerVO.setRedemptionEndTime(offerProgramVO.getRedemptionEndTime());
        offerVO.setValueIssued(offerProgramVO.getComputationRules().get(0).getOfferValue().getValue());
        offerVO.setCurrencyCode(offerProgramVO.getCurrencyCode());
        offerVO.setApplicabilityRule(offerProgramVO.getApplicabilityRule());

        OfferVO offerResponse = null;
        try {
            offerResponse = offerServBridge.createOffer(offerVO);
            if (offerResponse != null && offerResponse.getOfferId() != null) {
                context.setData(ContextKeys.DECRYPTED_OFFER_ID_KEY.getName(),
                        this.cryptoUtil.decryptOfferId(offerResponse.getOfferId()));
                context.setData(ContextKeys.DECRYPTED_OFFER_PROGRAM_ID_KEY.getName(),
                        this.cryptoUtil.decryptOfferId(offerResponse.getOfferProgramId()));
            }
        }
        catch (Exception e) {
            LOGGER.error("Exception occurred while creating Offer", e);
        }

        return offerResponse;
    }
}
