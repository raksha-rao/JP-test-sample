package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import com.jptest.money.PaymentMessageVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class PaymentMessageVOBuilder implements VOBuilder<PaymentMessageVO> {

    private String transactionMemo;
    private String additionalNotes;
    private BigInteger messageId;
    private String body;

    public static PaymentMessageVOBuilder newBuilder() {
        return new PaymentMessageVOBuilder();
    }

    public PaymentMessageVOBuilder transactionMemo(final String transactionMemo) {
        this.transactionMemo = transactionMemo;
        return this;

    }

    public PaymentMessageVOBuilder additionalNotes(final String additionalNotes) {
        this.additionalNotes = additionalNotes;
        return this;

    }

    public PaymentMessageVOBuilder messageId(final BigInteger messageId) {
        this.messageId = messageId;
        return this;

    }

    public PaymentMessageVOBuilder body(final String body) {
        this.body = body;
        return this;

    }

    @Override
    public PaymentMessageVO build() {
        final PaymentMessageVO vo = new PaymentMessageVO();
        vo.setTransactionMemo(this.transactionMemo);
        vo.setAdditionalNotes(this.additionalNotes);
        vo.setMessageId(this.messageId);
        vo.setBody(this.body);
        return vo;
    }
}
