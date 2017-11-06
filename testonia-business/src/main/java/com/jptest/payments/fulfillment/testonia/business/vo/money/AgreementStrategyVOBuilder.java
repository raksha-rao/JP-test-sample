package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.*;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.types.Currency;

import java.util.Collections;

/**
@JP Inc.
 */

public class AgreementStrategyVOBuilder implements VOBuilder<AgreementStrategyVO> {

    private VOBuilder<PaymentRecipientVO> recipient;
    private VOBuilder<PaymentSenderVO> sender;

    private ProductFamilyType productFamilyType;
    private ProductType productType;
    private FulfillmentType fulfillmentType = null;
    private Currency txnAmount = null;
    private VOBuilder<PurchaseContextVO> purchaseContextVOBuilder;
    private VOBuilder<TransactionContextVO> transactionContextVOBuilder;

    public static AgreementStrategyVOBuilder newBuilder() {
        return new AgreementStrategyVOBuilder();
    }

    public AgreementStrategyVOBuilder amount(Currency amount) {
        this.txnAmount = amount;
        return this;
    }

    public AgreementStrategyVOBuilder recipient(ListVOBuilder<PaymentRecipientVO> rBuilder) {
        recipient = rBuilder;
        return this;
    }

    public AgreementStrategyVOBuilder sender(ListVOBuilder<PaymentSenderVO> sBuilder) {
        sender = sBuilder;
        return this;
    }


    public AgreementStrategyVOBuilder purchaseContext(VOBuilder<PurchaseContextVO> purchaseContextVOBuilder) {
        this.purchaseContextVOBuilder = purchaseContextVOBuilder;
        return this;
    }

    public AgreementStrategyVOBuilder context(ProductFamilyType productFamilyType, ProductType productType,
                                              FulfillmentType fulfillmentType) {
        this.productFamilyType = productFamilyType;
        this.productType = productType;
        this.fulfillmentType = fulfillmentType;
        return this;
    }

    public AgreementStrategyVOBuilder transactionContext(VOBuilder<TransactionContextVO> txnContextBuilder) {
        this.transactionContextVOBuilder = txnContextBuilder;
        return this;
    }

    @Override
    public AgreementStrategyVO build() {
        AgreementStrategyVO vo = new AgreementStrategyVO();

        vo.setAgreementType(fulfillmentType);
        vo.setSender(sender.build());
        vo.setRecipient(recipient.build());
        vo.setTotal(txnAmount);

        PaymentContextVO paymentContextVo = new PaymentContextVO();
        paymentContextVo.setProductFamily(productFamilyType);
        paymentContextVo.setProduct(productType);

        PlanningContextVO planningContextVo = new PlanningContextVO();
        planningContextVo.setProduct(productType);
        planningContextVo.setProductFamily(productFamilyType);
        vo.setPlanningContext(planningContextVo);

        vo.setConstraints(Collections.emptyList());

        ClientIDVO clientId = new ClientIDVO();
        clientId.setId("1");
        clientId.setIdempotencyRequired(true);
        vo.setClientIds(Collections.singletonList(clientId));

        FeePolicyVO feePolicyVO = new FeePolicyVO();
        feePolicyVO.setPayerPolicy(FeePayerPolicyType.RECIPIENTS);

        ClientIDVO clientIdVo = new ClientIDVO();
        OpaqueDataElementVOBuilder opaqueClientData = OpaqueDataElementVOBuilder.newBuilder().vo(clientIdVo);
        vo.setStrategyExtensions(opaqueClientData.buildList());

        vo.setPurchaseContext(purchaseContextVOBuilder.build());
        vo.setTransactionContext(transactionContextVOBuilder.build());
        return vo;
    }
}
