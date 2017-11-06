package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.util.List;
import com.jptest.common.ActorInfoVO;
import com.jptest.money.AuthAttributeVO;
import com.jptest.money.FulfillAuthRequest;
import com.jptest.money.FulfillmentContextVO;
import com.jptest.money.FulfillmentPlanVO;
import com.jptest.money.StandinContextVO;
import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.vo.ValueObject;


public class FulfillAuthRequestBuilder implements VOBuilder<FulfillAuthRequest> {

    private VOBuilder<ActorInfoVO> actorInfo = new NullVOBuilder<>();
    private String idempotenceId;
    private VOBuilder<FulfillmentPlanVO> fulfillmentPlan = new NullVOBuilder<>();
    private VOBuilder<FulfillmentContextVO> fulfillmentContext = new NullVOBuilder<>();
    private VOBuilder<AuthAttributeVO> authAttributes = new NullVOBuilder<>();
    private VOBuilder<StandinContextVO> standinContext = new NullVOBuilder<>();
    private List<ValueObject> additionalLegacyVos;

    public static FulfillAuthRequestBuilder newBuilder() {
        return new FulfillAuthRequestBuilder();
    }

    public FulfillAuthRequestBuilder actorInfo(final VOBuilder<ActorInfoVO> actorInfo) {
        this.actorInfo = actorInfo;
        return this;

    }

    public FulfillAuthRequestBuilder idempotenceId(final String idempotenceId) {
        this.idempotenceId = idempotenceId;
        return this;

    }

    public FulfillAuthRequestBuilder fulfillmentPlan(final VOBuilder<FulfillmentPlanVO> fulfillmentPlan) {
        this.fulfillmentPlan = fulfillmentPlan;
        return this;

    }

    public FulfillAuthRequestBuilder fulfillmentContext(final VOBuilder<FulfillmentContextVO> fulfillmentContext) {
        this.fulfillmentContext = fulfillmentContext;
        return this;

    }

    public FulfillAuthRequestBuilder authAttributes(final VOBuilder<AuthAttributeVO> authAttributes) {
        this.authAttributes = authAttributes;
        return this;

    }

    public FulfillAuthRequestBuilder standinContext(final VOBuilder<StandinContextVO> standinContext) {
        this.standinContext = standinContext;
        return this;

    }

    public FulfillAuthRequestBuilder additionalLegacyVos(final List<ValueObject> additionalLegacyVos) {
        this.additionalLegacyVos = additionalLegacyVos;
        return this;

    }

    @Override
    public FulfillAuthRequest build() {
        final FulfillAuthRequest vo = new FulfillAuthRequest();
        vo.setActorInfo(this.actorInfo.build());
        vo.setIdempotenceId(this.idempotenceId);
        vo.setFulfillmentPlan(this.fulfillmentPlan.build());
        vo.setFulfillmentContext(this.fulfillmentContext.build());
        vo.setAuthAttributes(this.authAttributes.build());
        vo.setStandinContext(this.standinContext.build());
        vo.setAdditionalLegacyVos(this.additionalLegacyVos);
        return vo;
    }
}
