package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.common.ActorInfoVO;
import com.jptest.money.PaymentStrategyVO;
import com.jptest.money.PlanPaymentV2Request;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


/**
 * Builds PlanPaymentV2Request for MPS
 * 
 * @JP Inc.
 */
public class PlanPaymentV2RequestBuilder implements VOBuilder<PlanPaymentV2Request> {

    private VOBuilder<PaymentStrategyVO> paymentStrategyVoBuilder = null;
    private VOBuilder<ActorInfoVO> actorInfoVoBuilder;

    public static PlanPaymentV2RequestBuilder newBuilder() {
        return new PlanPaymentV2RequestBuilder();
    }

    public PlanPaymentV2RequestBuilder strategy(VOBuilder<PaymentStrategyVO> strategyBuilder) {
        paymentStrategyVoBuilder = strategyBuilder;
        return this;
    }

    public PlanPaymentV2RequestBuilder clientActor(VOBuilder<ActorInfoVO> actorInfoVoBuilder) {
        this.actorInfoVoBuilder = actorInfoVoBuilder;
        return this;
    }

    @Override
    public PlanPaymentV2Request build() {
        PlanPaymentV2Request rq = new PlanPaymentV2Request();
        rq.setClientInfo("SYMPHONY");
        rq.setStrategy(paymentStrategyVoBuilder.build());
        rq.setClientActor(actorInfoVoBuilder.build());
        return rq;
    }

}
