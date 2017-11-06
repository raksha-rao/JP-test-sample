package com.jptest.payments.fulfillment.testonia.model.group;

/**
 * @JP Inc.
 */

public enum PaymentFlowType {

    AUCTIONPAYFORjpincITEM("auction_pay_for_jpinc_item"),
    MARKET("market"),
    XCLICKBUYNOW("xclick_buy_now"),
    XCLICKSUBSCRIPTION("xclick_subscription"),
    XCLICKCART("xclick_cart"),
    XCLICKRECURRINGPAYMENTS("xclick_recurring_payments"),
    AUCTIONMSP("auction_msp"),
    WALLET("wallet"),
    WALLETWAX("wallet_wax"),
    VIRTUALTERMINAL("virtual_terminal"),
    SHIPPING("shipping"),
    MASSPAY("masspay"),
    MERCHANTPULL("merchant_pull"),
    CASH("cash"),
    GCPURCHASE("gc_purchase"),
    jpincAUTOPAY("jpinc_autopay"),
    SENDMONEY("send_money"),
    // ADAPTIVEPAYMENTS("adaptive_payments"),
    P2P("p2p"),
    EXPRESSXOONjpinc("express_xo_on_jpinc"),
    EXPRESSXOONjpincWAX("express_xo_on_jpinc_wax"),
    IXOONjpinc("ixo_on_jpinc"),
    IXOOFFjpinc("ixo_off_jpinc"),
    DCC("DCC"),
    UOME("uome"),
    ECAO("ecao"),
    GUEST_BANK_UNILATERAL("guest_bank_unilateral"),
    HERMES("hermes"),
    DORT("doRT"),
    LOAN_FUNDING("loan_funding"),
    LOAN_REPAYMENT("loan_repayment"),
    P2P_GIFT("p2p_gift");

    private final String type;

    private PaymentFlowType(final String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static PaymentFlowType getPaymentFlowType(final String type) {

        PaymentFlowType flowType = null;
        if (type != null) {
            for (final PaymentFlowType eachOperation : PaymentFlowType.values()) {
                if (eachOperation.getType().equals(type)) {
                    flowType = eachOperation;
                    break;
                }
            }
        }

        return flowType;

    }
}
