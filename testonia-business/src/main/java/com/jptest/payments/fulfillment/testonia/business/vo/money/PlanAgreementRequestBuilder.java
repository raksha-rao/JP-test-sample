package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.common.ActorInfoVO;
import com.jptest.money.AgreementStrategyVO;
import com.jptest.money.PlanAgreementRequest;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;

/**
@JP Inc.
 *
 * Builder to create planning agreement request (order planning)
 */
public class PlanAgreementRequestBuilder implements VOBuilder<PlanAgreementRequest> {

    private VOBuilder<AgreementStrategyVO> agreementStrategyVoBuilder = null;
    private VOBuilder<ActorInfoVO> actorInfoVoBuilder;

    public static PlanAgreementRequestBuilder newBuilder() {
        return new PlanAgreementRequestBuilder();
    }

    public PlanAgreementRequestBuilder strategy(VOBuilder<AgreementStrategyVO> strategyBuilder) {
        this.agreementStrategyVoBuilder = strategyBuilder;
        return this;
    }

    public PlanAgreementRequestBuilder clientActor(VOBuilder<ActorInfoVO> actorInfoVoBuilder) {
        this.actorInfoVoBuilder = actorInfoVoBuilder;
        return this;
    }

    @Override
    public PlanAgreementRequest build() {
        PlanAgreementRequest planAgreementRequest = new PlanAgreementRequest();
        planAgreementRequest.setStrategy(agreementStrategyVoBuilder.build());
        planAgreementRequest.setClientActor(actorInfoVoBuilder.build());
        return planAgreementRequest;

    }
}
