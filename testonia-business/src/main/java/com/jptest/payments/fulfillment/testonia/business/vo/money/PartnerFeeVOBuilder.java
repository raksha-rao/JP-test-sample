package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.DisbursementComponentType;
import com.jptest.money.PartnerFeeVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.types.Currency;

import java.math.BigInteger;

/**
@JP Inc.
 */
public class PartnerFeeVOBuilder implements VOBuilder<PartnerFeeVO> {

    public static PartnerFeeVOBuilder newBuilder() {
        return new PartnerFeeVOBuilder();
    }

    public PartnerFeeVOBuilder partnerFeeAmount(Currency partnerFeeAmount) {
        this.partnerFeeAmount = partnerFeeAmount;
        return this;
    }

    public PartnerFeeVOBuilder settlementAccountNumber(BigInteger settlementAccountNumber) {
        this.settlementAccountNumber = settlementAccountNumber;
        return this;
    }

    public PartnerFeeVOBuilder disbursementType(DisbursementComponentType disbursementType) {
        this.disbursementType = disbursementType;
        return this;
    }

    Currency partnerFeeAmount;
    BigInteger settlementAccountNumber;
    DisbursementComponentType disbursementType;

    @Override
    public PartnerFeeVO build() {
        PartnerFeeVO vo = new PartnerFeeVO();
        vo.setSettlementAccountNumber(settlementAccountNumber);
        vo.setPartnerFeeAmount(partnerFeeAmount);
        vo.setDisbursementType(disbursementType);
        return vo;
    }
}
