package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.common.ActorInfoVO;
import com.jptest.common.OpaqueDataElementVO;
import com.jptest.money.*;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
@JP Inc.
 */
public class FulfillBulkOrderRequestBuilder implements VOBuilder<FulfillBulkOrderRequest> {

    private static final Logger LOG = Logger.getLogger(FulfillBulkOrderRequestBuilder.class);

    List<VOBuilder<OrderRequestVO>> orderRequestVOBuilders = new ArrayList<>();
    ListVOBuilder<OpaqueDataElementVO> opaqueDataVOBuilder;
    ActorInfoVO actorInfoVO;
    AgreementPlanVO uberAgreementPlan;

    public static FulfillBulkOrderRequestBuilder newBuilder() {
        return new FulfillBulkOrderRequestBuilder();
    }

    public FulfillBulkOrderRequestBuilder addOrderRequestVO(VOBuilder<OrderRequestVO> orderRequestVOBuilder) {
        this.orderRequestVOBuilders.add(orderRequestVOBuilder);
        return this;
    }

    public FulfillBulkOrderRequestBuilder actorInfo(ActorInfoVO actorInfoVO) {
        this.actorInfoVO = actorInfoVO;
        return this;
    }

    /**
     * For bulk order use case, uber agreement plan is one of the plans created for bulk order which has the highest
     * amount on which planning was done. This is in fulfill bulk order request.
     *
     * @param uberPlan
     * @return
     */
    public FulfillBulkOrderRequestBuilder uberAgreementPlan(AgreementPlanVO uberPlan) {
        this.uberAgreementPlan = uberPlan;
        return this;
    }

    public FulfillBulkOrderRequestBuilder extensions(ListVOBuilder<OpaqueDataElementVO> opaqueDataVOBuilder) {
        this.opaqueDataVOBuilder = opaqueDataVOBuilder;
        return this;
    }

    @Override
    public FulfillBulkOrderRequest build() {
        FulfillBulkOrderRequest rq = new FulfillBulkOrderRequest();
        List<OrderRequestVO> orderRequestVOS = new ArrayList<>();

        orderRequestVOBuilders.stream().forEach(each -> orderRequestVOS.add(each.build()));

        //individual order requests
        rq.setIndividualOrderRequests(orderRequestVOS);

        rq.setActorInfo(actorInfoVO);

        BigInteger idempotenceId = new BigInteger(RandomStringUtils.randomNumeric(15));
        rq.setIdempotenceId(idempotenceId.toString());

        rq.setAlternateIds(generateAlternateIds());

        //setting Uber plan
        rq.setUberAgreementPlan(uberAgreementPlan);

        if(opaqueDataVOBuilder!= null) rq.setExtensions(opaqueDataVOBuilder.buildList());

        return rq;
    }


    private List<AlternateIdentifierVO> generateAlternateIds() {
        AlternateIdentifierVO payId = new AlternateIdentifierVO();
        payId.setIssuerEnum(AlternateIDIssuer.EXTERNAL_ISSUER);
        payId.setReferenceType(PaymentReferenceTypeCode.PAY_ID);
        payId.setIdentifier(RandomStringUtils.randomNumeric(15));
        LOG.info("Setting PAY_ID as alternate Id : " + payId.getIdentifier());

        AlternateIdentifierVO cartId = new AlternateIdentifierVO();
        cartId.setIssuerEnum(AlternateIDIssuer.EXTERNAL_ISSUER);
        cartId.setReferenceType(PaymentReferenceTypeCode.CART_ID);
        cartId.setIdentifier(RandomStringUtils.randomNumeric(15));
        LOG.info("Setting CART_ID as alternate Id : " + cartId.getIdentifier());

        List<AlternateIdentifierVO> alternateIdentifierVOList = new ArrayList<AlternateIdentifierVO>();
        alternateIdentifierVOList.add(payId);
        alternateIdentifierVOList.add(cartId);
        return alternateIdentifierVOList;
    }
}
