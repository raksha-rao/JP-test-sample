package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.common.ActorInfoVO;
import com.jptest.money.DisbursementStrategyVO;
import com.jptest.money.PlanDisbursementRequest;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class PlanDisbursementRequestBuilder extends ListWrappedVOBuilder<PlanDisbursementRequest> {

    private VOBuilder<DisbursementStrategyVO> disbursementStrategyBuilder = DisbursementStrategyVOBuilder.newBuilder();

    private VOBuilder<ActorInfoVO> actorInfoVObuilder;
        
    public static PlanDisbursementRequestBuilder newBuilder() {
        return new PlanDisbursementRequestBuilder();
    }

    public PlanDisbursementRequestBuilder disbursementStrategyBuilder(VOBuilder<DisbursementStrategyVO> disbursementStrategyBuilder) {
    	this.disbursementStrategyBuilder = disbursementStrategyBuilder;
        return this;
    }

    public PlanDisbursementRequestBuilder actorInfoVObuilder(VOBuilder<ActorInfoVO> actorInfoVObuilder) {
        this.actorInfoVObuilder = actorInfoVObuilder;
        return this;
    }

    public PlanDisbursementRequest build() {
    	PlanDisbursementRequest vo = new PlanDisbursementRequest();

    	vo.setStrategy(disbursementStrategyBuilder.build());
    	
    	vo.setClientActor(actorInfoVObuilder.build());

        return vo;
    }

}
