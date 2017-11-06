package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.PaymentFlagsVO;
import com.jptest.money.TransactionUnitContextVO;
import com.jptest.money.TransactionUnitReasonType;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;

public class TransactionUnitContextVOBuilder extends ListWrappedVOBuilder<TransactionUnitContextVO> {

    private TransactionUnitReasonType reason = TransactionUnitReasonType.jpinc_ITEMS;
    private PaymentFlagsVO paymentFlags;

    public static TransactionUnitContextVOBuilder newBuilder() {
        return new TransactionUnitContextVOBuilder();
    }

    public TransactionUnitContextVOBuilder reason(TransactionUnitReasonType reason) {
        this.reason = reason;
        return this;
    }

    public TransactionUnitContextVOBuilder paymentFlags(PaymentFlagsVO paymentFlags) {
        this.paymentFlags = paymentFlags;
        return this;
    }

    public TransactionUnitContextVO build() {

        TransactionUnitContextVO vo = new TransactionUnitContextVO();

        vo.setReason(reason);
        if (paymentFlags != null) {
            vo.setPaymentFlags(paymentFlags);
        }

        return vo;
    }

}
