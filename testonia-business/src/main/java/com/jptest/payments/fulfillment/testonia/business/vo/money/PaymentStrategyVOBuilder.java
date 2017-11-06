package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.util.Collections;

import com.jptest.common.OpaqueDataElementVO;
import com.jptest.money.*;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;

/**
 * Builds PaymentStrategyVO used in PlanPaymentV2Request
 * 
 * @JP Inc.
 */
public class PaymentStrategyVOBuilder extends ListWrappedVOBuilder<PaymentStrategyVO> {

    private String strategyId = "1";
    private ListVOBuilder<PaymentRecipientVO> recipient;
    private ListVOBuilder<PaymentSenderVO> sender;
    private ListVOBuilder<FacilitatorVO> facilitators;
    private ListVOBuilder<OpaqueDataElementVO> strategyExtensions = OpaqueDataElementVOBuilder.newBuilder();

    // Should we default these enums?
    private ProductFamilyType productFamilyType;
    private ProductType productType;
    private IntegrationType integrationType = IntegrationType.ONLINE;
    private String memoMessage = null;
    private ListVOBuilder<TransactionUnitVO> transaction;
    private VOBuilder<PartnerFeeVO> partnerFee;

    public static PaymentStrategyVOBuilder newBuilder() {
        return new PaymentStrategyVOBuilder();
    }

    public PaymentStrategyVOBuilder transaction(ListVOBuilder<TransactionUnitVO> transactionVoBuilder) {
        this.transaction = transactionVoBuilder;
        return this;
    }

    public PaymentStrategyVOBuilder strategyId(String id) {
        strategyId = id;
        return this;
    }

    public PaymentStrategyVOBuilder recipient(ListVOBuilder<PaymentRecipientVO> rBuilder) {
        recipient = rBuilder;
        return this;
    }

    public PaymentStrategyVOBuilder sender(ListVOBuilder<PaymentSenderVO> sBuilder) {
        sender = sBuilder;
        return this;
    }

    public PaymentStrategyVOBuilder memo(String memoMessage) {
        this.memoMessage = memoMessage;
        return this;
    }

    public PaymentStrategyVOBuilder facilitators(ListVOBuilder<FacilitatorVO> fBuilder) {
        facilitators = fBuilder;
        return this;
    }

    public PaymentStrategyVOBuilder strategyExtensions(ListVOBuilder<OpaqueDataElementVO> strategyExtensionsBuilder) {
        strategyExtensions = strategyExtensionsBuilder;
        return this;
    }

    public PaymentStrategyVOBuilder partnerFee(VOBuilder<PartnerFeeVO> partnerFeeVOBuilder) {
        partnerFee = partnerFeeVOBuilder;
        return this;
    }
    /**
     * Used to build PaymentContext and PlanningContext
     * 
     * @param productFamilyType
     * @param productType
     * @param integrationType
     * @return
     */
    public PaymentStrategyVOBuilder context(ProductFamilyType productFamilyType, ProductType productType,
            IntegrationType integrationType) {
        this.productFamilyType = productFamilyType;
        this.productType = productType;
        this.integrationType = integrationType;
        return this;
    }

    public PaymentStrategyVO build() {
        PaymentStrategyVO vo = new PaymentStrategyVO();
        vo.setStrategyId(strategyId);
        vo.setInitiationPolicy(InitiationPolicyType.INDEPENDENT);
        vo.setSenders(sender.buildList());
        vo.setRecipients(recipient.buildList());
        vo.setFacilitators(facilitators != null ? facilitators.buildList() : Collections.emptyList());

        PaymentContextVO paymentContextVo = new PaymentContextVO();
        paymentContextVo.setProductFamily(productFamilyType);
        paymentContextVo.setProduct(productType);
        paymentContextVo.setIntegrationType(integrationType);
        vo.setPaymentContext(paymentContextVo);

        PlanningContextVO planningContextVo = new PlanningContextVO();
        planningContextVo.setIntegrationType(integrationType);
        planningContextVo.setProduct(productType);
        planningContextVo.setProductFamily(productFamilyType);
        vo.setPlanningContext(planningContextVo);

        vo.setConstraints(Collections.emptyList());

        ClientIDVO clientId = new ClientIDVO();
        clientId.setId("1");
        clientId.setIdempotencyRequired(true);
        vo.setClientIds(Collections.singletonList(clientId));

        if (memoMessage != null) {
            PaymentMemoVO memo = new PaymentMemoVO();
            memo.setShortDescription(memoMessage);
            vo.setMessage(memo);
        }

        vo.setStrategyExtensions(strategyExtensions.buildList());
        FeePolicyVO feePolicyVO = new FeePolicyVO();
        feePolicyVO.setPayerPolicy(FeePayerPolicyType.RECIPIENTS);
        vo.setFeePolicy(feePolicyVO);

        vo.setPostbacks(Collections.emptyList());

        vo.setTransactionUnits(transaction.buildList());

        if(partnerFee != null) vo.setChargePartnerFee(partnerFee.build());

        return vo;
    }

}
