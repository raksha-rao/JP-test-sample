package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;

import com.jptest.money.PaymentFlagsVO;
import com.jptest.money.TransactionContextVO;
import com.jptest.money.TransactionUnitReasonType;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;

/**
@JP Inc.
 */
public class TransactionContextVOBuilder implements VOBuilder<TransactionContextVO> {
    private TransactionUnitReasonType reason = TransactionUnitReasonType.GOODS;
    private PaymentFlagsVO paymentFlags;
    private BigInteger billingAgreementId;

    public static TransactionContextVOBuilder newBuilder() {
        return new TransactionContextVOBuilder();
    }

    public TransactionContextVOBuilder reason(TransactionUnitReasonType reason) {
        this.reason = reason;
        return this;
    }

    public TransactionContextVOBuilder paymentFlags(PaymentFlagsVO paymentFlags) {
        this.paymentFlags = paymentFlags;
        return this;
    }
    
    public TransactionContextVOBuilder billingAgreementId(BigInteger billingAgreementId) {
        this.billingAgreementId = billingAgreementId;
        return this;
    }

    public TransactionContextVO build() {

		TransactionContextVO vo = new TransactionContextVO();

		vo.setReason(reason);
		if (paymentFlags != null) {
			vo.setPaymentFlags(paymentFlags);
		}
		if (billingAgreementId != null) {
			vo.setBillingAgreementId(billingAgreementId);
		}
		return vo;
	}
}
