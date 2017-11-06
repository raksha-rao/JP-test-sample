package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.util.List;

import com.jptest.money.FundingConstraintPolicyType;
import com.jptest.money.FundingConstraintType;
import com.jptest.money.FundingConstraintVO;
import com.jptest.money.FundingMethodType;
import com.jptest.money.InstrumentIdentifierVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;

public class FundingConstraintVOBuilder extends ListWrappedVOBuilder<FundingConstraintVO> {

    private FundingConstraintType type;
    private FundingConstraintPolicyType usagePolicy;
    private List<InstrumentIdentifierVO> instruments;
    private List<FundingMethodType> fundingMethods;

    public static FundingConstraintVOBuilder newBuilder() {
        return new FundingConstraintVOBuilder();
    }

    public FundingConstraintVOBuilder type(FundingConstraintType type) {
        this.type = type;
        return this;
    }

    public FundingConstraintVOBuilder usagePolicy(FundingConstraintPolicyType usagePolicy) {
        this.usagePolicy = usagePolicy;
        return this;
    }

    public FundingConstraintVOBuilder instruments(List<InstrumentIdentifierVO> instruments) {
        this.instruments = instruments;
        return this;
    }

    public FundingConstraintVOBuilder fundingMethods(List<FundingMethodType> fundingMethods) {
        this.fundingMethods = fundingMethods;
        return this;
    }

    @Override
    public FundingConstraintVO build() {
        FundingConstraintVO vo = new FundingConstraintVO();

        vo.setType(type);
        vo.setUsagePolicy(usagePolicy);
        vo.setInstruments(instruments);
        vo.setFundingMethodsAsEnum(fundingMethods);
        return vo;
    }

}
