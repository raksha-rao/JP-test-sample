package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.util.ArrayList;
import java.util.List;

import com.jptest.merchant.MerchantFeatureStatusVO;
import com.jptest.money.FacilitatorVO;
import com.jptest.money.UserDetailsVO;
import com.jptest.money.UserIdentifierVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;

public class FacilitatorVOBuilder extends ListWrappedVOBuilder<FacilitatorVO> {

    private static final String DEFAULT_EMAIL = "facilitator@facilitator.com";
    private static final int PIMP_FEATURE_ACCOUNT_jpinc = 35;

    public static FacilitatorVOBuilder newBuilder() {
        return new FacilitatorVOBuilder();
    }

    @Override
    public FacilitatorVO build() {
        FacilitatorVO vo = new FacilitatorVO();
        UserIdentifierVO userId = new UserIdentifierVO();
        userId.setEmailAddress(DEFAULT_EMAIL);
        UserDetailsVO userDetails = new UserDetailsVO();

        MerchantFeatureStatusVO merchantFeatureStatus = new MerchantFeatureStatusVO();
        merchantFeatureStatus.setFeatureId(PIMP_FEATURE_ACCOUNT_jpinc);
        merchantFeatureStatus.setIsActive(true);

        List<MerchantFeatureStatusVO> merchantFeatureStatusList = new ArrayList<>();
        merchantFeatureStatusList.add(merchantFeatureStatus);

        userDetails.setMerchantFeatureStatus(merchantFeatureStatusList);
        userId.setUserDetails(userDetails);
        vo.setUserId(userId);
        return vo;
    }

}
