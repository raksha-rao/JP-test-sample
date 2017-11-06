package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class OperationIdempotencyVOBuilder implements VOBuilder<OperationIdempotencyVO> {

    private BigInteger activityId;
    private String idempotencyString;

    public static OperationIdempotencyVOBuilder newBuilder() {
        return new OperationIdempotencyVOBuilder();
    }
    public OperationIdempotencyVOBuilder activityId(BigInteger activityId) {
        this.activityId = activityId;
        return this;

    }

    public OperationIdempotencyVOBuilder idempotencyString(String idempotencyString) {
        this.idempotencyString = idempotencyString;
        return this;

    }

    @Override
    public OperationIdempotencyVO build() {
          OperationIdempotencyVO vo = new OperationIdempotencyVO();
          vo.setActivityId(activityId);
          vo.setIdempotencyString(idempotencyString);
          return vo;
    }
}
