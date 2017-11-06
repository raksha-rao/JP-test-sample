package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.TransactionAttributeType;
import com.jptest.money.TransactionAttributeVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;

public class TransactionAttributeVOBuilder extends ListWrappedVOBuilder<TransactionAttributeVO> {

    private TransactionAttributeType attributeName;
    private String value;

    public static TransactionAttributeVOBuilder newBuilder() {
        return new TransactionAttributeVOBuilder();
    }

    public TransactionAttributeVOBuilder attributeName(TransactionAttributeType attributeName) {
        this.attributeName = attributeName;
        return this;
    }

    public TransactionAttributeVOBuilder value(String value) {
        this.value = value;
        return this;
    }

    @Override
    public TransactionAttributeVO build() {
        TransactionAttributeVO vo = null;
        if (attributeName != null) {
            vo = new TransactionAttributeVO();
            vo.setAttributeName(attributeName);
            vo.setAttributeValue(value);
        }
        return vo;
    }

}
