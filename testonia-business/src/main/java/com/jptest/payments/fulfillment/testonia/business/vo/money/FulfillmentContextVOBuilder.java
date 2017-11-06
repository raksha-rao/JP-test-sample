package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.jptest.common.OpaqueDataElementVO;
import com.jptest.money.AlternateIdentifierVO;
import com.jptest.money.FulfillmentContextVO;
import com.jptest.money.FulfillmentMemoVO;
import com.jptest.money.FulfillmentTransactionUnitVO;
import com.jptest.money.FulfillmentUniqueIdConstraintVO;
import com.jptest.money.PaymentMemoVO;
import com.jptest.money.TransactionAttributeVO;
import com.jptest.money.UniqueIdType;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class FulfillmentContextVOBuilder implements VOBuilder<FulfillmentContextVO> {

    private List<FulfillmentMemoVO> transactionUnitMemos;
    private BigInteger merchantAccountNumber;
    private String encryptedOrderId;
    private VOBuilder<FulfillmentUniqueIdConstraintVO> fulfillmentUniqueIdConstraintVoBuilder;
    private String paymentMemoMessage = "Default payment memo";
    private List<AlternateIdentifierVO> alternateIds;
    private List<TransactionAttributeVO> txnAttributes;
    private BigInteger billingAgreementID;
    public static FulfillmentContextVOBuilder newBuilder() {
        return new FulfillmentContextVOBuilder();
    }

    public void transactionUnitMemos(List<FulfillmentMemoVO> transactionUnitMemos) {
        this.transactionUnitMemos = transactionUnitMemos;
    }

    public void txnAttributes(List<TransactionAttributeVO> txnAttributes) {
        this.txnAttributes = txnAttributes;
    }

    public void alternateIds(List<AlternateIdentifierVO> alternateIds) {
        this.alternateIds = alternateIds;
    }

    public FulfillmentContextVOBuilder setMerchantAccountNumber(BigInteger merchantAccountNumber) {
        this.merchantAccountNumber = merchantAccountNumber;
        return this;
    }

    public FulfillmentContextVOBuilder fulfillmentContextVO(
            VOBuilder<FulfillmentUniqueIdConstraintVO> fulfillmentUniqueIdConstraintVoBuilder) {
        this.fulfillmentUniqueIdConstraintVoBuilder = fulfillmentUniqueIdConstraintVoBuilder;
        return this;
    }

    public FulfillmentContextVOBuilder encryptedOrderId(String encryptedOrderId) {
        this.encryptedOrderId = encryptedOrderId;
        return this;
    }

    public FulfillmentContextVOBuilder paymentMemoMessage(String message) {
        this.paymentMemoMessage = message;
        return this;
    }
    
    public FulfillmentContextVOBuilder setBillingAgreementID(BigInteger billingAgreementID) {
        this.billingAgreementID = billingAgreementID;
        return this;
    }

    @Override
    public FulfillmentContextVO build() {
        FulfillmentContextVO fulfillmentContextVO = new FulfillmentContextVO();
        fulfillmentContextVO.setTransactionUnitMemos(transactionUnitMemos);
        fulfillmentContextVO
                .setFulfillmentExtensions(new ArrayList<OpaqueDataElementVO>());
        fulfillmentContextVO.setAlternateIds(alternateIds);
        fulfillmentContextVO.setTxnAttributes(txnAttributes);
        FulfillmentMemoVO memoVo = new FulfillmentMemoVO();
        memoVo.setPaymentPlanStrategyId(BigInteger.ONE.toString());
        memoVo.setTransactionUnitId(BigInteger.ONE.toString());
        PaymentMemoVO paymentMemo = new PaymentMemoVO();
        paymentMemo.setDetailedDescription(paymentMemoMessage);
        memoVo.setMessage(paymentMemo);
        List<FulfillmentMemoVO> memos = Collections.singletonList(memoVo);
        fulfillmentContextVO.setTransactionUnitMemos(memos);

        FulfillmentTransactionUnitVO fulfillmentTransactionUnitVo = new FulfillmentTransactionUnitVO();
        fulfillmentTransactionUnitVo.setTransactionUnitId(BigInteger.ONE.toString());

        fulfillmentTransactionUnitVo.setUniqueIdConstraint(buildFulfillmentUniqueIdConstraintVO());
        List<FulfillmentTransactionUnitVO> transactionUnits = Collections.singletonList(fulfillmentTransactionUnitVo);
        fulfillmentContextVO.setTransactionUnits(transactionUnits);

        if (encryptedOrderId != null)
            fulfillmentContextVO.setEncryptedOrderId(encryptedOrderId);
        if(billingAgreementID != null)
        	fulfillmentContextVO.setBillingAgreementId(billingAgreementID);
        return fulfillmentContextVO;
    }

    private FulfillmentUniqueIdConstraintVO buildFulfillmentUniqueIdConstraintVO() {
        if (fulfillmentUniqueIdConstraintVoBuilder != null) {
            return fulfillmentUniqueIdConstraintVoBuilder.build();
        }

        FulfillmentUniqueIdConstraintVO fulfillmentUniqueIdConstraintVo = new FulfillmentUniqueIdConstraintVO();
        fulfillmentUniqueIdConstraintVo.setIdType(UniqueIdType.LEGACY_DATA_SET);

        if (merchantAccountNumber != null) {
            Random random = new Random();
            fulfillmentUniqueIdConstraintVo
                    .setUniqueId(String.valueOf(random.nextInt(10000)) + "_" + merchantAccountNumber.toString());
        }

        return fulfillmentUniqueIdConstraintVo;
    }
}
