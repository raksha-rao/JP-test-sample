package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.AgreementStrategyVO;
import com.jptest.money.OrderRequestVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;

/**
@JP Inc.
 *
 * For bulk order, individual orders have to be built for each order planning.
 */
public class OrderRequestVOBuilder implements VOBuilder<OrderRequestVO> {
    VOBuilder<AgreementStrategyVO> agreementStrategyVOBuilderVOBuilder;
    String purchaseUnitRefId;

    public static OrderRequestVOBuilder newBuilder() {
        return new OrderRequestVOBuilder();
    }

    public OrderRequestVOBuilder agreementStrategyVO(VOBuilder<AgreementStrategyVO>
                                                             agreementStrategyVOBuilderVOBuilder) {
        this.agreementStrategyVOBuilderVOBuilder = agreementStrategyVOBuilderVOBuilder;
        return this;
    }

    public OrderRequestVOBuilder purchaseUnitRefId(String purchaseUnitRefId) {
        this.purchaseUnitRefId = purchaseUnitRefId;
        return this;
    }

    @Override
    public OrderRequestVO build() {
        OrderRequestVO rq = new OrderRequestVO();
        if (agreementStrategyVOBuilderVOBuilder != null) {
            rq.setAgreementStrategy(agreementStrategyVOBuilderVOBuilder.build());
        }
        rq.setPurchaseUnitReferenceId(purchaseUnitRefId);
        return rq;
    }
}
