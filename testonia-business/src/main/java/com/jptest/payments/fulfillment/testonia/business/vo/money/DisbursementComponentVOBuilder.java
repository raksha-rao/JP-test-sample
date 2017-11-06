package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.*;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.types.Currency;

import java.util.List;

/*
 * biz/Money/service/payment/value_object/DisbursementComponentVO.oml
 * Details of the amount that will be credited to a funding sink as a component
 * of each TransactionUnitPlan
 */
public class DisbursementComponentVOBuilder extends ListWrappedVOBuilder<DisbursementComponentVO> {

    private VOBuilder<FundingSinkVO> fundingSink = FundingSinkVOBuilder.newBuilder();

    private VOBuilder<EquivalentAmountVO> equivalentAmount = EquivalentAmountVOBuilder.newBuilder();

    private Currency fundsOut;

    private List<FulfillmentContingencyVO> fulfillmentContingency;

    private DisbursementComponentType type;

    public static DisbursementComponentVOBuilder newBuilder() {
        return new DisbursementComponentVOBuilder();
    }

    public DisbursementComponentVOBuilder fundsOut(Currency fundsOut) {
        this.fundsOut = fundsOut;
        return this;
    }

    public DisbursementComponentVOBuilder fulfillmentContingency(List<FulfillmentContingencyVO>
                                                                         fulfillmentContingency) {
        this.fulfillmentContingency = fulfillmentContingency;
        return this;
    }

    public DisbursementComponentVOBuilder fundingSink(VOBuilder<FundingSinkVO> builder) {
        fundingSink = builder;
        return this;
    }

    public DisbursementComponentVOBuilder equivalentAmount(VOBuilder<EquivalentAmountVO> builder) {
        equivalentAmount = builder;
        return this;
    }

    public DisbursementComponentVOBuilder type(DisbursementComponentType type) {
        this.type = type;
        return this;
    }

    public DisbursementComponentVO build() {
        DisbursementComponentVO vo = new DisbursementComponentVO();

        vo.setFundsOut(fundsOut);

        vo.setContingencies(fulfillmentContingency);

        vo.setFundingSink(fundingSink.build());

        vo.setFundsOutAmount(equivalentAmount.build());

        if (type != null) vo.setType(type);

        return vo;
    }

}
