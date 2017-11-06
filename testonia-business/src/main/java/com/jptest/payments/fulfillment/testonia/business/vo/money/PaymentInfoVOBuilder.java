package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.money.PaymentInfoVO;
import com.jptest.money.AlternateIdentifierVO;
import com.jptest.money.CartAggregateVO;
import com.jptest.money.CoupledTxnInfoVO;
import com.jptest.money.TransactionAttributeVO;
import com.jptest.types.Currency;

import java.math.BigInteger;


public class PaymentInfoVOBuilder implements VOBuilder<PaymentInfoVO> {

    private Currency paymentAmount;
    private Currency chargeFeeForAmount;
    private Byte type;
    private Byte subtype;
    private Byte personalPaymentSubtype;
    private BigInteger plFlags;
    private Long transactionFlags1;
    private Long transactionFlags2;
    private Long transactionFlags3;
    private Long transactionFlags4;
    private Long transactionFlags5;
    private BigInteger transactionFlags5Ext;
    private BigInteger transactionFlags6;
    private BigInteger transactionFlags7;
    private Boolean forPayShipping = Boolean.FALSE;
    private Boolean forRefund = Boolean.FALSE;
    private Boolean forReturnShipment = Boolean.FALSE;
    private BigInteger parentId;
    private BigInteger billingAgreementId;
    private String billingAgreementHandle;
    private String jpincCheckoutMspId;
    private Boolean isMicropaymentTxn = Boolean.FALSE;
    private Currency usdPaymentAmount;
    private Boolean nonReferencedRefund = Boolean.FALSE;
    private VOBuilder<CoupledTxnInfoVO> coupledTxnInfo = new NullVOBuilder<>();
    private ListVOBuilder<TransactionAttributeVO> txnAttributes = new NullVOBuilder<>();
    private VOBuilder<CartAggregateVO> cartAggregateInfo = new NullVOBuilder<>();
    private ListVOBuilder<AlternateIdentifierVO> alternateIds = new NullVOBuilder<>();
    private String networkIdentifier;

    public static PaymentInfoVOBuilder newBuilder() {
        return new PaymentInfoVOBuilder();
    }
    public PaymentInfoVOBuilder paymentAmount(Currency paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;

    }

    public PaymentInfoVOBuilder chargeFeeForAmount(Currency chargeFeeForAmount) {
        this.chargeFeeForAmount = chargeFeeForAmount;
        return this;

    }

    public PaymentInfoVOBuilder type(Byte type) {
        this.type = type;
        return this;

    }

    public PaymentInfoVOBuilder subtype(Byte subtype) {
        this.subtype = subtype;
        return this;

    }

    public PaymentInfoVOBuilder personalPaymentSubtype(Byte personalPaymentSubtype) {
        this.personalPaymentSubtype = personalPaymentSubtype;
        return this;

    }

    public PaymentInfoVOBuilder plFlags(BigInteger plFlags) {
        this.plFlags = plFlags;
        return this;

    }

    public PaymentInfoVOBuilder transactionFlags1(Long transactionFlags1) {
        this.transactionFlags1 = transactionFlags1;
        return this;

    }

    public PaymentInfoVOBuilder transactionFlags2(Long transactionFlags2) {
        this.transactionFlags2 = transactionFlags2;
        return this;

    }

    public PaymentInfoVOBuilder transactionFlags3(Long transactionFlags3) {
        this.transactionFlags3 = transactionFlags3;
        return this;

    }

    public PaymentInfoVOBuilder transactionFlags4(Long transactionFlags4) {
        this.transactionFlags4 = transactionFlags4;
        return this;

    }

    public PaymentInfoVOBuilder transactionFlags5(Long transactionFlags5) {
        this.transactionFlags5 = transactionFlags5;
        return this;

    }

    public PaymentInfoVOBuilder transactionFlags5Ext(BigInteger transactionFlags5Ext) {
        this.transactionFlags5Ext = transactionFlags5Ext;
        return this;

    }

    public PaymentInfoVOBuilder transactionFlags6(BigInteger transactionFlags6) {
        this.transactionFlags6 = transactionFlags6;
        return this;

    }

    public PaymentInfoVOBuilder transactionFlags7(BigInteger transactionFlags7) {
        this.transactionFlags7 = transactionFlags7;
        return this;

    }

    public PaymentInfoVOBuilder forPayShipping(Boolean forPayShipping) {
        this.forPayShipping = forPayShipping;
        return this;

    }

    public PaymentInfoVOBuilder forRefund(Boolean forRefund) {
        this.forRefund = forRefund;
        return this;

    }

    public PaymentInfoVOBuilder forReturnShipment(Boolean forReturnShipment) {
        this.forReturnShipment = forReturnShipment;
        return this;

    }

    public PaymentInfoVOBuilder parentId(BigInteger parentId) {
        this.parentId = parentId;
        return this;

    }

    public PaymentInfoVOBuilder billingAgreementId(BigInteger billingAgreementId) {
        this.billingAgreementId = billingAgreementId;
        return this;

    }

    public PaymentInfoVOBuilder billingAgreementHandle(String billingAgreementHandle) {
        this.billingAgreementHandle = billingAgreementHandle;
        return this;

    }

    public PaymentInfoVOBuilder jpincCheckoutMspId(String jpincCheckoutMspId) {
        this.jpincCheckoutMspId = jpincCheckoutMspId;
        return this;

    }

    public PaymentInfoVOBuilder isMicropaymentTxn(Boolean isMicropaymentTxn) {
        this.isMicropaymentTxn = isMicropaymentTxn;
        return this;

    }

    public PaymentInfoVOBuilder usdPaymentAmount(Currency usdPaymentAmount) {
        this.usdPaymentAmount = usdPaymentAmount;
        return this;

    }

    public PaymentInfoVOBuilder nonReferencedRefund(Boolean nonReferencedRefund) {
        this.nonReferencedRefund = nonReferencedRefund;
        return this;

    }

    public PaymentInfoVOBuilder coupledTxnInfo(VOBuilder<CoupledTxnInfoVO> coupledTxnInfo) {
        this.coupledTxnInfo = coupledTxnInfo;
        return this;

    }

    public PaymentInfoVOBuilder txnAttributes(ListVOBuilder<TransactionAttributeVO> txnAttributes) {
        this.txnAttributes = txnAttributes;
        return this;

    }

    public PaymentInfoVOBuilder cartAggregateInfo(VOBuilder<CartAggregateVO> cartAggregateInfo) {
        this.cartAggregateInfo = cartAggregateInfo;
        return this;

    }

    public PaymentInfoVOBuilder alternateIds(ListVOBuilder<AlternateIdentifierVO> alternateIds) {
        this.alternateIds = alternateIds;
        return this;

    }

    public PaymentInfoVOBuilder networkIdentifier(String networkIdentifier) {
        this.networkIdentifier = networkIdentifier;
        return this;

    }

    @Override
    public PaymentInfoVO build() {
          PaymentInfoVO vo = new PaymentInfoVO();
          vo.setPaymentAmount(paymentAmount);
          vo.setChargeFeeForAmount(chargeFeeForAmount);
          vo.setType(type);
          vo.setSubtype(subtype);
          vo.setPersonalPaymentSubtype(personalPaymentSubtype);
          vo.setPlFlags(plFlags);
          vo.setTransactionFlags1(transactionFlags1);
          vo.setTransactionFlags2(transactionFlags2);
          vo.setTransactionFlags3(transactionFlags3);
          vo.setTransactionFlags4(transactionFlags4);
          vo.setTransactionFlags5(transactionFlags5);
          vo.setTransactionFlags5Ext(transactionFlags5Ext);
          vo.setTransactionFlags6(transactionFlags6);
          vo.setTransactionFlags7(transactionFlags7);
          vo.setForPayShipping(forPayShipping);
          vo.setForRefund(forRefund);
          vo.setForReturnShipment(forReturnShipment);
          vo.setParentId(parentId);
          vo.setBillingAgreementId(billingAgreementId);
          vo.setBillingAgreementHandle(billingAgreementHandle);
          vo.setjpincCheckoutMspId(jpincCheckoutMspId);
          vo.setIsMicropaymentTxn(isMicropaymentTxn);
          vo.setUsdPaymentAmount(usdPaymentAmount);
          vo.setNonReferencedRefund(nonReferencedRefund);
          vo.setCoupledTxnInfo(coupledTxnInfo.build());
          vo.setTxnAttributes(txnAttributes.buildList());
          vo.setCartAggregateInfo(cartAggregateInfo.build());
          vo.setAlternateIds(alternateIds.buildList());
          vo.setNetworkIdentifier(networkIdentifier);
          return vo;
    }
}
