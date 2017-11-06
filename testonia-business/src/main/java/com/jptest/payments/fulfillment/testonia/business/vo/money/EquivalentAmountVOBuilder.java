package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.EquivalentAmountVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.types.Currency;

/*
 * biz/Money/TransactionRead/value_object/EquivalentAmountVO.oml
 * A compound type to carry a monetary value and its USD equivalent
 * USD Equiv is typically used for Risk and Compliance checking.
 */
public class EquivalentAmountVOBuilder extends ListWrappedVOBuilder<EquivalentAmountVO> {

    private Currency actualAmount;

    private Currency usdEquivalent;

    public static EquivalentAmountVOBuilder newBuilder() {
        return new EquivalentAmountVOBuilder();
    }

    public EquivalentAmountVOBuilder actualAmount(Currency actualAmount) {
        this.actualAmount = actualAmount;
        return this;
    }
    
    public EquivalentAmountVOBuilder usdEquivalent(Currency usdEquivalent) {
        this.usdEquivalent = usdEquivalent;
        return this;
    }

    public EquivalentAmountVO build() {
    	EquivalentAmountVO vo = new EquivalentAmountVO();

        vo.setActualAmount(actualAmount);
        
        vo.setUsdEquivalent(usdEquivalent);
        
        return vo;
    }

}
