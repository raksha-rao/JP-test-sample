package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.common.ActorInfoVO;
import com.jptest.money.FulfillPaymentRequest;
import com.jptest.money.FulfillmentContextVO;
import com.jptest.money.FulfillmentPlanVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class FulfillPaymentRequestBuilder implements VOBuilder<FulfillPaymentRequest> {

    private VOBuilder<ActorInfoVO> actorInfoVoBuilder;
    private String idempotenceId;
    private VOBuilder<FulfillmentPlanVO> fulfillmentPlanVoBuilder;
    private VOBuilder<FulfillmentContextVO> fulfillmentContextVoBuilder = FulfillmentContextVOBuilder.newBuilder();
    
    public static FulfillPaymentRequestBuilder newBuilder() {
        return new FulfillPaymentRequestBuilder();
    }

    public FulfillPaymentRequestBuilder actorInfo(VOBuilder<ActorInfoVO> actorInfoVoBuilder) {
        this.actorInfoVoBuilder = actorInfoVoBuilder;
        return this;
    }

    public FulfillPaymentRequestBuilder idempotenceId(String idempotenceId) {
        this.idempotenceId = idempotenceId;
        return this;
    }

    public FulfillPaymentRequestBuilder fulfillmentPlan(VOBuilder<FulfillmentPlanVO> fulfillmentPlanVoBuilder) {
        this.fulfillmentPlanVoBuilder = fulfillmentPlanVoBuilder;
        return this;
    }

    public FulfillPaymentRequestBuilder fulfillmentContext(VOBuilder<FulfillmentContextVO> fulfillmentContextVoBuilder) {
        this.fulfillmentContextVoBuilder = fulfillmentContextVoBuilder;
        return this;
    }

    @Override
    public FulfillPaymentRequest build() {
        FulfillPaymentRequest vo = new FulfillPaymentRequest();
        vo.setActorInfo(actorInfoVoBuilder.build());
        vo.setIdempotenceId(idempotenceId);
        vo.setFulfillmentPlan(fulfillmentPlanVoBuilder.build());
        vo.setFulfillmentContext(fulfillmentContextVoBuilder.build());
        return vo;
    }

}
