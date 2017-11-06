package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;

import com.jptest.merchant.InvoiceTransactionDetailsVO;
import com.jptest.merchant.InvoiceXClickParametersVO;
import com.jptest.merchant.TransactionSellerOptionsVO;
import com.jptest.money.PaymentInvoiceParametersVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class PaymentInvoiceParametersVOBuilder implements VOBuilder<PaymentInvoiceParametersVO> {

    private VOBuilder<InvoiceXClickParametersVO> xclickParameters = new NullVOBuilder<>();
    private VOBuilder<InvoiceTransactionDetailsVO> txnDetails = new NullVOBuilder<>();
    private ListVOBuilder<TransactionSellerOptionsVO> sellerOptions = new NullVOBuilder<>();
    private BigInteger numberOfItems;
    private BigInteger waxTransactionId;
    private String guestFirstName;
    private String guestLastName;
    private BigInteger guestAliasId;
    private BigInteger guestReceiptId;
    private BigInteger guestPhoneId;
    private BigInteger ccLoginAccountNumber;
    private BigInteger emailLoginAccountNumber;
    private BigInteger dccBuyerNonPrimaryAliasId;
    private Long gxoExperience;
    private BigInteger uomeId;
    private BigInteger uomeTargetAliasId;
    private BigInteger uomeNeedsCompletion;
    private String mspId;

    public static PaymentInvoiceParametersVOBuilder newBuilder() {
        return new PaymentInvoiceParametersVOBuilder();
    }
    public PaymentInvoiceParametersVOBuilder xclickParameters(VOBuilder<InvoiceXClickParametersVO> xclickParameters) {
        this.xclickParameters = xclickParameters;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder txnDetails(VOBuilder<InvoiceTransactionDetailsVO> txnDetails) {
        this.txnDetails = txnDetails;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder sellerOptions(ListVOBuilder<TransactionSellerOptionsVO> sellerOptions) {
        this.sellerOptions = sellerOptions;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder numberOfItems(BigInteger numberOfItems) {
        this.numberOfItems = numberOfItems;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder waxTransactionId(BigInteger waxTransactionId) {
        this.waxTransactionId = waxTransactionId;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder guestFirstName(String guestFirstName) {
        this.guestFirstName = guestFirstName;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder guestLastName(String guestLastName) {
        this.guestLastName = guestLastName;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder guestAliasId(BigInteger guestAliasId) {
        this.guestAliasId = guestAliasId;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder guestReceiptId(BigInteger guestReceiptId) {
        this.guestReceiptId = guestReceiptId;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder guestPhoneId(BigInteger guestPhoneId) {
        this.guestPhoneId = guestPhoneId;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder ccLoginAccountNumber(BigInteger ccLoginAccountNumber) {
        this.ccLoginAccountNumber = ccLoginAccountNumber;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder emailLoginAccountNumber(BigInteger emailLoginAccountNumber) {
        this.emailLoginAccountNumber = emailLoginAccountNumber;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder dccBuyerNonPrimaryAliasId(BigInteger dccBuyerNonPrimaryAliasId) {
        this.dccBuyerNonPrimaryAliasId = dccBuyerNonPrimaryAliasId;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder gxoExperience(Long gxoExperience) {
        this.gxoExperience = gxoExperience;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder uomeId(BigInteger uomeId) {
        this.uomeId = uomeId;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder uomeTargetAliasId(BigInteger uomeTargetAliasId) {
        this.uomeTargetAliasId = uomeTargetAliasId;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder uomeNeedsCompletion(BigInteger uomeNeedsCompletion) {
        this.uomeNeedsCompletion = uomeNeedsCompletion;
        return this;

    }

    public PaymentInvoiceParametersVOBuilder mspId(String mspId) {
        this.mspId = mspId;
        return this;

    }

    @Override
    public PaymentInvoiceParametersVO build() {
          PaymentInvoiceParametersVO vo = new PaymentInvoiceParametersVO();
          vo.setXclickParameters(xclickParameters.build());
          vo.setTxnDetails(txnDetails.build());
          vo.setSellerOptions(sellerOptions.buildList());
          vo.setNumberOfItems(numberOfItems);
          vo.setWaxTransactionId(waxTransactionId);
          vo.setGuestFirstName(guestFirstName);
          vo.setGuestLastName(guestLastName);
          vo.setGuestAliasId(guestAliasId);
          vo.setGuestReceiptId(guestReceiptId);
          vo.setGuestPhoneId(guestPhoneId);
          vo.setCcLoginAccountNumber(ccLoginAccountNumber);
          vo.setEmailLoginAccountNumber(emailLoginAccountNumber);
          vo.setDccBuyerNonPrimaryAliasId(dccBuyerNonPrimaryAliasId);
          vo.setGxoExperience(gxoExperience);
          vo.setUomeId(uomeId);
          vo.setUomeTargetAliasId(uomeTargetAliasId);
          vo.setUomeNeedsCompletion(uomeNeedsCompletion);
          vo.setMspId(mspId);
          return vo;
    }
}
