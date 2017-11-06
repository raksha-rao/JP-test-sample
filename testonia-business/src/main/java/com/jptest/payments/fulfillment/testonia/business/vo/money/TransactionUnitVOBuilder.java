package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import com.jptest.common.OpaqueDataElementVO;
import com.jptest.money.ClientIDVO;
import com.jptest.money.DisbursementPolicyType;
import com.jptest.money.FulfillmentType;
import com.jptest.money.FundingConstraintVO;
import com.jptest.money.PurchaseContextVO;
import com.jptest.money.SenderComponentVO;
import com.jptest.money.TransactionAttributeVO;
import com.jptest.money.TransactionUnitContextVO;
import com.jptest.money.TransactionUnitVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.types.Currency;
import com.jptest.user.AddressVO;

public class TransactionUnitVOBuilder extends ListWrappedVOBuilder<TransactionUnitVO> {

    private String transactionUnitId = "1";

    private VOBuilder<TransactionUnitContextVO> context = TransactionUnitContextVOBuilder.newBuilder();

    private FulfillmentType fulfillmentType = FulfillmentType.PAYMENT;

    private Currency total;

    private Currency shipping;

    private Currency taxTotal;

    private DisbursementPolicyType disbursementPolicyType = DisbursementPolicyType.UNRESTRICTED;

    private BigInteger senderAccountNumber;

    private BigInteger recipientAccountNumber;

    private String recipientEmail;

    private ListVOBuilder<OpaqueDataElementVO> extensionsBuilder = NullVOBuilder.newBuilder();

    private VOBuilder<PurchaseContextVO> purchaseContextBuilder = NullVOBuilder.newBuilder();

    private ListVOBuilder<FundingConstraintVO> constraintsBuilder = FundingConstraintVOBuilder.newBuilder();

    private ListVOBuilder<TransactionAttributeVO> txnAttributesNvp;

    public static TransactionUnitVOBuilder newBuilder() {
        return new TransactionUnitVOBuilder();
    }

    public TransactionUnitVOBuilder transactionUnitId(String id) {
        transactionUnitId = id;
        return this;
    }

    public TransactionUnitVOBuilder context(VOBuilder<TransactionUnitContextVO> builder) {
        context = builder;
        return this;
    }

    public TransactionUnitVOBuilder fulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
        return this;
    }

    /**
     * sets total(s) for the transaction units, treats totals and item_totals as same variable.
     */
    public TransactionUnitVOBuilder total(Currency total) {
        this.total = total;
        return this;
    }

    public TransactionUnitVOBuilder shipping(Currency shipping) {
        this.shipping = shipping;
        return this;
    }

    public TransactionUnitVOBuilder taxTotal(Currency taxTotal) {
        this.taxTotal = taxTotal;
        return this;
    }

    public TransactionUnitVOBuilder disbursementPolicyType(DisbursementPolicyType disbursementPolicyType) {
        this.disbursementPolicyType = disbursementPolicyType;
        return this;
    }

    public TransactionUnitVOBuilder sender(BigInteger senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
        return this;
    }

    public TransactionUnitVOBuilder recipient(BigInteger recipientAccountNumber, String email) {
        this.recipientAccountNumber = recipientAccountNumber;
        this.recipientEmail = email;
        return this;
    }

    public TransactionUnitVOBuilder transactionUnitExtensions(ListVOBuilder<OpaqueDataElementVO> extensionsBuilder) {
        this.extensionsBuilder = extensionsBuilder;
        return this;
    }

    public TransactionUnitVOBuilder purchaseContext(VOBuilder<PurchaseContextVO> purchaseContextBuilder) {
        this.purchaseContextBuilder = purchaseContextBuilder;
        return this;
    }

    public TransactionUnitVOBuilder constraints(ListVOBuilder<FundingConstraintVO> constraintsBuilder) {
        this.constraintsBuilder = constraintsBuilder;
        return this;
    }

    public TransactionUnitVOBuilder txnAttributesNvp(ListVOBuilder<TransactionAttributeVO> txnAttributesNvp) {
        this.txnAttributesNvp = txnAttributesNvp;
        return this;
    }

    public TransactionUnitVO build() {
        TransactionUnitVO vo = new TransactionUnitVO();

        vo.setTransactionUnitId(transactionUnitId);

        vo.setTransactionUnitContext(context.build());

        vo.setFulfillmentType(fulfillmentType);

        if (total != null) {
            vo.setTotals(Collections.singletonList(total));
            vo.setItemTotals(Collections.singletonList(total));
        } else {
            throw new IllegalStateException("Total has not been set.");
        }

        vo.setShippingTotals(listCurrency(shipping));
        vo.setTaxTotals(listCurrency(taxTotal));

        vo.setDisbursementPolicy(disbursementPolicyType);

        ClientIDVO clientId = new ClientIDVO();
        clientId.setId("1");
        clientId.setIdempotencyRequired(true);
        vo.setClientIds(Collections.singletonList(clientId));

        vo.setConstraints(constraintsBuilder.buildList());

        SenderComponentVO sender = new SenderComponentVO();
        sender.setAmount(total);
        sender.setSender(PartyVOsBuilder.sender(senderAccountNumber).build());
        vo.setSenders(Collections.singletonList(sender));

        vo.setRecipient(PartyVOsBuilder.recipient(recipientAccountNumber, recipientEmail).build());

        vo.setItems(Collections.emptyList());
        vo.setShipping(new AddressVO());
        vo.setReferrers(Collections.emptyList());
        vo.setChainLinks(Collections.emptyList());
        vo.setTxnUnitExtensions(extensionsBuilder.buildList());
        vo.setPurchaseContext(purchaseContextBuilder.build());
        vo.setTxnAttributesNvp(txnAttributesNvp != null ? txnAttributesNvp.buildList() : null);
        return vo;
    }

    private List<Currency> listCurrency(Currency currency) {
        if (currency == null) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(currency);
        }
    }

}
