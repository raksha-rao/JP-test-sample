package com.jptest.payments.fulfillment.testonia.model.util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * A replica of WTransactionConstants.h from the C++ world.
 * <p/>
 * Path: biz/Money/TransactionRead/WTransactionConstants.h
 */
public final class WTransactionConstants {
    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.TYPE column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Type {
        ACHWITHDRAW(/*                */'A'),
        CARD(/*                       */'B'),
        INCENTIVE(/*                  */'C'),
        DEPOSIT(/*                    */'D'),
        AFFLUENCE(/*                  */'E'),
        FEE(/*                        */'F'),
        EXT_INIT_DEPOSIT(/*           */'G'),
        ACHDEPOSIT(/*                 */'H'),
        DIVIDEND(/*                   */'I'),
        ADJUSTMENT(/*                 */'J'),
        USPS_SETTLEMENT(/*            */'K'),
        DISPLAY_ONLY(/*               */'L'),
        MAINTENANCE(/*                */'M'),
        BONUS(/*                      */'N'),
        BUYER_CREDIT_CREDIT(/*        */'O'),
        BILLPAY(/*                    */'P'),
        CHARGE(/*                     */'R'),
        TEMP_HOLD(/*                  */'Q'),
        BUYER_CREDIT_CHARGE(/*        */'S'),
        CCCREDIT(/*                   */'T'),
        USERUSER(/*                   */'U'),
        REVERSAL(/*                   */'V'),
        WITHDRAW(/*                   */'W'),
        CURRENCY_CONVERSION(/*        */'X'),
        AFFINITY(/*                   */'Y'),
        CORRECTION(/*                 */'Z'),
        AUTHORIZATION(/*              */'@'),
        TRANSFER(/*                   */'2'),
        RECEIVABLES(/*                */'3'),
        BUYER_CREDIT_PAYMENT(/*       */'b'),
        HOLDING_BALANCE_TRANSFER(/*   */'4'),
        PAYABLE(/*                    */'5');

        private final char type;

        private Type(char type) {
            this.type = type;
        }

        public char getValue() {
            return type;
        }

        public byte getByte() {
            return (byte) type;
        }

        private static Map<Character, Type> s_map;

        static {
            s_map = new HashMap<Character, Type>();
            for (Type type : Type.values()) {
                s_map.put(type.getValue(), type);
            }
        }

        public static Type fromValue(char value) {
            return s_map.get(value);
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.SUBTYPE column.
     */
    public enum Subtype {
        ACHDEPOSIT_AFS('A'),
        ACHDEPOSIT('H'),
        ACHDEPOSIT_BALANCE_MANAGER('B'),
        ACHDEPOSIT_DC_OVERDRAFT('H'),
        ACHDEPOSIT_RECOVERY_ADD_FUNDS('L'),
        ACHDEPOSIT_RISK_SCORE('R'),
        ACHWITHDRAW_AUTOSWEEP('A'),
        ACHWITHDRAW_AUTOWITHDRAWAL('B'),
        ADJUSTMENT_CHARGEBACK_BUYER_CANCELLED('C'),
        ADJUSTMENT_CHARGEBACK_BUYER_WON_NO_REPRESENTMENT('B'),
        ADJUSTMENT_CHARGEBACK_CASE_CREATED('I'),
        ADJUSTMENT_CHARGEBACK_POST_CASE_CLOSURE_CORRECTION('Z'),
        ADJUSTMENT_CHARGEBACK_SELLER_REPRESENTED('R'),
        ADJUSTMENT_CHARGEBACK_SELLER_REPRESENTMENT_REJECTED('J'),
        AUTH_ADJUSTMENT('A'),
        AUTH_ADVICE('@'),
        BUYER_CREDIT_CHARGE_BML('M'),
        BUYER_CREDIT_CHARGE_BUYER_CREDIT('B'),
        BUYER_CREDIT_CHARGE_DC_OVERDRAFT('S'),
        BUYER_CREDIT_CHARGE_DEFAULT('0'),
        BUYER_CREDIT_CHARGE_jpinc_CARD('E'),
        BUYER_CREDIT_CHARGE_PAY_LATER('X'),
        BUYER_CREDIT_CHARGE_PLUS_CARD('D'),
        BUYER_CREDIT_CREDIT_BML('M'),
        BUYER_CREDIT_CREDIT_BUYER_CREDIT('B'),
        BUYER_CREDIT_CREDIT_DEFAULT('0'),
        BUYER_CREDIT_CREDIT_jpinc_CARD('E'),
        BUYER_CREDIT_CREDIT_PAY_LATER('X'),
        BUYER_CREDIT_CREDIT_PLUS_CARD('D'),
        BUYER_CREDIT_PAYMENT_BML('M'),
        BUYER_CREDIT_PAYMENT_BUYER_CREDIT('B'),
        BUYER_CREDIT_PAYMENT_DEFAULT('0'),
        BUYER_CREDIT_PAYMENT_jpinc_CARD('E'),
        BUYER_CREDIT_PAYMENT_PAY_LATER('X'),
        BUYER_CREDIT_PAYMENT_PLUS_CARD('D'),
        CARD_TEMP_HOLD('H'),
        CASH_ADVANCE_FEE('C'),
        CCCREDIT_NON_REFERENCED_CREDIT('N'),
        CCCREDIT_ORIGINAL_CREDIT('O'),
        CCONV_RATE_CUSTOMER('C'),
        CCONV_RATE_INTERBANK('B'),
        CCONV_RATE_INVERSE('I'),
        CC_FUNDING('F'),
        CC_FUNDING_NEGBAL('N'),
        CC_PINLESS('P'),
        DC_OVERDRAFT_AUTHORIZATION('D'),
        DISPLAY_ONLY_ACH_DEPOSIT_CLEARED('A'),
        DISPLAY_ONLY_AUTH_VOIDED('R'),
        DISPLAY_ONLY_DC('T'),
        DISPLAY_ONLY_DC_CASHBACK_BONUS('B'),
        DISPLAY_ONLY_ECHECK_CLEARED('E'),
        DISPLAY_ONLY_ECHECK_FAILED('F'),
        DISPLAY_ONLY_HELD_PAYMENT_ACCEPTED('H'),
        DISPLAY_ONLY_HELD_PAYMENT_DENIED('D'),
        DISPLAY_ONLY_PAYOUT('P'),
        DISPLAY_ONLY_PENDING_REVERSAL_CANCELED('C'),
        DISPLAY_ONLY_PENDING_REVERSAL_COMPLETED('V'),
        DISPLAY_ONLY_RECOUP('X'),
        EXT_INIT_DEPOSIT_CHINA_UNIONPAY('C'),
        EXT_INIT_DEPOSIT_REVERSAL('R'),
        HOLDING_BALANCE_TRANSFER_CC_FUNDED_BALANCE('A'),
        HOLDING_BALANCE_TRANSFER_GUEST_BALANCE('G'),
        INCENTIVE_COUPON('C'),
        INCENTIVE_GC('G'),
        INCENTIVE_PLUS_REWARD('R'),
        INCENTIVE_POINTS('P'),
        INCENTIVE_VOUCHER('E'),
        MAINTENANCE_GC('G'),
        PAYABLE_OFAC('T'),
        PAYABLES_INTERNAL('I'),
        PAYABLE_INTERNAL_SETTLEMENT('i'),
        PAYABLES_TXN_LEVEL_DISBURSEMENT('D'),
        RECEIVABLES_BACK_OFFICE_SETTLED('B'),
        RECEIVABLES_BACK_OFFICE_SETTLED_REVERSAL('V'),
        RECEIVABLES_SETTLED_AGGREGATED('A'),
        RECEIVABLES_SETTLED_AGGREGATED_REVERSAL('X'),
        RECEIVABLES_SETTLED_PER_TXN('T'),
        RECEIVABLES_SETTLED_PER_TXN_REVERSAL('W'),
        TEMP_HOLD_ACH_DEPOSIT('H'),
        TEMP_HOLD_AUTHORIZATION('A'),
        TEMP_HOLD_BALANCE('B'),
        TRANSFER_CONSOLIDATION('C'),
        TRANSFER_FAMILY('F'),
        USERUSER_AUCTION_jpinc('I'),
        USERUSER_AUCTION_NON_jpinc('A'),
        USERUSER_CASH('C'),
        USERUSER_DISPUTED_PAYMENT('X'),
        USERUSER_DONATIONS('D'),
        USERUSER_GC_PURCHASE('P'),
        USERUSER_GOODS('G'),
        USERUSER_NON_REFERENCED_CREDIT('N'),
        USERUSER_PERSONAL('H'),
        USERUSER_POSTAGE('U'),
        USERUSER_REBATE('R'),
        USERUSER_SERVICE('S'),
        USPS_SETTLEMENT_POSTAL_CARRIER('U'),
        WITHDRAW_WORLDLINK('K'),
        USERUSER_LOAN_DISBURSEMENT('B'),
        USERUSER_LOAN_REPAYMENT('Q');

        private final char subtype;

        private Subtype(char subtype) {
            this.subtype = subtype;
        }

        public char getValue() {
            return subtype;
        }

        public byte getByte() {
            return (byte) subtype;
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.REASON column.
     */
    public enum Reason {
        ACHDEPOSIT_DO_NOT_USE('\0'),
        ACHWITHDRAW_DO_NOT_USE('\0'),
        ADJ_CHARGEBACK_INTERNAL('P'),
        ADJ_CHARGEBACK('K'),
        ADJ_CHARGEOFF('O'),
        ADJ_INCENTIVE('I'),
        ADJ_REFUND('R'),
        ADJ_REIMBURSE_INTERNAL('N'),
        ADJ_REIMBURSE('M'),
        ADJ_REVERSAL('V'),
        AUTHORIZATION_EXPIRED('E'),
        AUTHORIZATION_TIMED_OUT('T'),
        AUTHORIZATION_VOIDED('V'),
        BONUS_ACH_RET_PSP_PAYOUT('J'),
        BONUS_ACH_RETURNS_PP_PAYOUT('V'),
        BONUS_ACH_REWARD('A'),
        BONUS_ACH_USER_INITIATED_RETURNS_PAYOUT('u'),
        BONUS_ADR_PP_PAYOUT('Z'),
        BONUS_AFR_PP_PAYOUT('K'),
        BONUS_AFR_PSP_PAYOUT('H'),
        BONUS_ATM_REBATE('C'),
        BONUS_AUTH_ADVICE('k'),
        BONUS_BALANCE_MANAGER('B'),
        BONUS_BC_PSP_PAYOUT('h'),
        BONUS_BILLING_PP_PAYOUT('l'),
        BONUS_BUYER_CREDIT_FIRST_USE_BONUS('U'),
        BONUS_CC_SECURITY('S'),
        BONUS_CC_SECURITY_SWITCH('W'),
        BONUS_CHARGEBACK_PAYOUT('Q'),
        BONUS_CHARGEOFF('L'),
        BONUS_DC_CASHBACK('D'),
        BONUS_jpincCARD_CASHBACK('b'),
        BONUS_FORCED_POST('f'),
        BONUS_FX_TOPOFF('X'),
        BONUS_MERCHANT_REFERRAL('M'),
        BONUS_MERCHANT_REFERRAL_REWARD('G'),
        BONUS_NON_REGE_PP_PAYOUT('o'),
        BONUS_NT_PP_PAYOUT('N'),
        BONUS_jptest_STANDIN_FOR_FUNDING_SOURCE_ERRORS('s'),
        BONUS_PBP_PP_PAYOUT('P'),
        BONUS_PBP_TIER2_PP_PAYOUT('F'),
        BONUS_PFS_CONSUMER('c'),
        BONUS_PFS_GENERAL('g'),
        BONUS_PFS_MARKET_PLACE('e'),
        BONUS_PFS_MERCHANT('m'),
        BONUS_PFS_PCMS_ALT('a'),
        BONUS_PFS_PCMS_BT('j'),
        BONUS_PFS_PCMS_DEF('p'),
        BONUS_PLUSCARD_CASHBACK('d'),
        BONUS_PPBC_PP_PAYOUT('R'),
        BONUS_REVENUE_SHARE('r'),
        BONUS_SPOOF_FX_TOPOFF('T'),
        BONUS_SPOOF_PP_PAYOUT('O'),
        BONUS_SPOOF_PSP_PAYOUT('I'),
        BONUS_SPPP_PP_PAYOUT('E'),
        BONUS_WARRANTY_CLAIM('Y'),
        BUYER_CREDIT('G'),
        CARD_INSUFFICIENT_FUNDS('y'),
        CARD_INVALID_STATUS('x'),
        CARD_RESTRICED_ACCOUNT('w'),
        CCONV_AUTO('A'),
        CCONV_CHARGE_OFF('O'),
        CCONV_CLOSE_ACCOUNT('C'),
        CCONV_NEG_BALANCE('N'),
        CCONV_TRANSFER('U'),
        COMPLIANCE_DECLINED('B'),
        CPN_TRANSACTION_DENIED('v'),
        CREDIT_CARD('R'),
        CREDITLIMIT('C'),
        DELAYED_DISBURSEMENT('d'),
        DISPUTE_PAYOUT('O'),
        EXPIREDCERT('E'),
        EXTERNAL('N'),
        FEE_ALLOWED_GAMING('H'),
        FEE_AMEX_PRO_VT('z'),
        FEE_ATM_WITHDRAW('T'),
        FEE_AUTOSWEEP('P'),
        FEE_BC_PROMO_OFFER('L'),
        FEE_BILLPAY('B'),
        FEE_CARD_INACTIVITY('S'),
        FEE_CARD_ISSUE('D'),
        FEE_CASH_ADVANCE('Z'),
        FEE_CHARGEBACK('K'),
        FEE_CROSS_BORDER('X'),
        FEE_DUNNING('d'),
        FEE_ELV_RETURN('G'),
        FEE_FEE_ADJUSTMENT('h'),
        FEE_FOREIGN_ACH('F'),
        FEE_FOREIGN_ACH_RETURNED('O'),
        FEE_FOREIGN_DEPOSIT('E'),
        FEE_FPS_WITHDRAWAL_RETURNED('f'),
        FEE_INTERCOMPANY('i'),
        FEE_INTL_CC_FUND('I'),
        FEE_INTL_CC_WITHDRAW('J'),
        FEE_INTL_PASSWORD_RECOVERY('A'),
        FEE_MASSPAY('M'),
        FEE_NON_USD('N'),
        FEE_ORIGINAL_CREDIT_WITHDRAWAL('t'),
        FEE_PAYMENT_RECEIVED('R'),
        FEE_PAYMENT_SENT('s'),
        FEE_PIE_GC_BREAKAGE('w'),
        FEE_POS_PAYMENT_RECEIVED('p'),
        FEE_POSTAGE_CONVENIENCE('C'),
        FEE_PRO_BILLING('Q'),
        FEE_REV_SHARE('@'),
        FEE_UNILATERAL_ADJUSTMENT('g'),
        FEE_WARRANTY('Y'),
        FEE_WIRE_WITHDRAWAL_RETURN('v'),
        FEE_WIRE_WITHDRAWAL('u'),
        FEE_WITHDRAWAL('W'),
        FEE_WITHDRAWAL_WORLDLINK('U'),
        FEE_WORLDLINK_CHECK_RETURNED('V'),
        FPSWITHDRAW_FINSYS_VALIDATION_ERROR('F'),
        INSUFFICIENT_FUNDS('F'),
        INVALIDTRANSACTION('I'),
        MAINTENANCE_GC('G'),
        NO_FAULT('N'),
        NONE('\0'),
        OFAC_DECLINED('O'),
        OFAC_PENDING('A'),
        PAYABLE_ACH_UNAUTH('U'),
        PAYABLE_CHARGEBACK_BUYER_CANCELLED('C'),
        PAYABLE_CHARGEBACK_BUYER_WON_NO_REPRESENTMENT('B'),
        PAYABLE_CHARGEBACK_CASE_CREATED('I'),
        PAYABLE_CHARGEBACK_SELLER_REPRESENTED('R'),
        PAYABLE_CHARGEBACK_SELLER_REPRESENTMENT_REJECTED('J'),
        PAYABLE_PARTNER_FEE('p'),
        PAYEE_INACTIVE_OR_LOCKED('P'),
        PAYER_INACTIVE_OR_LOCKED('K'),
        PAYER_RESTRICTED('X'),
        PP_SYSTEM_ERROR('M'),
        RECEIVABLES_CHARGEOFF('O'),
        RECEIVABLES_DELAYED_FUNDING('L'),
        RECEIVABLES_DUE_DATE_EXPIRED('P'),
        RECEIVABLES_DUNNING_FEE_FUNDING('E'),
        RECEIVABLES_PAYMENT_DISPUTED('D'),
        RECEIVABLES_PAYMENT_FUNDED('F'),
        RECEIVABLES_PAYMENT_REVERSAL('R'),
        RECEIVABLES_PAYOFF_FAILURE('A'),
        RECEIVABLES_PAYOFF('T'),
        RECOUP('U'),
        REFUND('r'),
        RETURN_SHIPMENT('h'),
        REV_ADMIN('A'),
        REV_BCPAYMENT_ACH_REVERSAL('B'),
        REV_BUYER_COMPLAINT('C'),
        REV_CASCADED_REVERSAL('O'),
        REV_CHARGEBACK('K'),
        REV_COMPLIANCE_REVERSAL('I'),
        REV_CREDIT_REIMBURSMENT_SYSTEM('m'),
        REV_jpinc_SPPP('E'),
        REV_FIXED_FEE('R'),
        REV_FRAUD_REVERSAL('F'),
        REV_NEGBAL_WITHDRAW_CANCEL('W'),
        REV_PAYMENT_TXN_REVERSED('P'),
        REV_REFUND('D'),
        REV_SPOOF_TXN('S'),
        REV_THIRDPARTY_DISPUTESYSTEM_TRIGGERED('X'),
        REV_VENDOR_INITIATED('V'),
        REV_WARRANTY('Y'),
        SELLER_FAULT('S'),
        SELLER_VOLUNTARY('V'),
        SERVERERROR('S'),
        SPENDINGLIMIT('L'),
        THIRDPARTY_LOGISTICS_FAULT('L'),
        THIRDPARTY_REFUND('W'),
        TRANSLIMIT('T'),
        WITHDRAWAL_FOR_P20_REVERSAL('j');

        private final char reason;

        private Reason(char reason) {
            this.reason = reason;
        }

        public char getValue() {
            return reason;
        }

        public byte getByte() {
            return (byte) reason;
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.STATUS column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Status {
        PENDING(/*             */'P'),
        PROCESSING(/*          */'R'),
        SUCCESS(/*             */'S'),
        DENIED(/*              */'D'),
        REVERSED(/*            */'V'),
        DISPLAY_ONLY(/*        */'L'),
        PARTIALLY_REFUNDED(/*  */'F'),
        CREATED(/*             */'C');

        private final char status;

        private Status(char status) {
            this.status = status;
        }

        public char getValue() {
            return status;
        }

        public byte getByte() {
            return (byte) status;
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.TRANSITION column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Transition {
        UNKNOWN(/*               */'\0'),
        jptest_LEDGER_BAL(/*      */'A'),
        jptest_BAL(/*             */'B'),
        jptest_LEDGER_REV(/*      */'K'),
        LEDGER_BAL(/*             */'L'),
        LEDGER_REV(/*             */'R'),
        UNAFFECTED(/*             */'U'),
        jptest_REV(/*             */'V'),
        UNAFFECTED_CORRECTION(/*  */'Z');

        private final char transition;

        private Transition(char transition) {
            this.transition = transition;
        }

        public char getValue() {
            return transition;
        }

        public byte getByte() {
            return (byte) transition;
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.ACCEPT_DENY_METHOD column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum AcceptDenyMethod {
        ADMIN(/*                   */'A'),
        AUTO_CANCEL(/*             */'B'),
        SENDER(/*                  */'S'),
        RECIPIENT(/*               */'R'),
        BOUNCED_ECHECK(/*          */'E'),
        BOUNCED_IACH(/*            */'I'),
        REFUND(/*                  */'F'),
        NEGBAL_WITHDRAW_CANCEL(/*  */'W'),
        COMPLIANCE_AUTOREVERSAL(/* */'C'),
        CLOSED_MARKET(/*           */'M'),
        NONRECEIVABLE_CURRENCY(/*  */'N');

        private final char acceptDenyMethod;

        private AcceptDenyMethod(char acceptDenyMethod) {
            this.acceptDenyMethod = acceptDenyMethod;
        }

        public char getValue() {
            return acceptDenyMethod;
        }

        public byte getByte() {
            return (byte) acceptDenyMethod;
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.FLAGS column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Flag1 {
        NO_DISPLAY(/*                            */0x00000001L),
        HELD_FOR_VERIFY(/*                       */0x00000002L),
        PAYMENTSERV_PROCESSED(/*                 */0x00000004L),
        HELD_FOR_RECIPIENT(/*                    */0x00000008L),
        FOR_AUCTION_ITEM(/*                      */0x00000010L),
        CORRECTION(/*                            */0x00000020L),
        BUYER_GUARANTEE(/*                       */0x00000040L),
        jpinc_GUEST_USER_TRANSACTION(/*           */0x00000080L),
        CARD_CASH_ADVANCE(/*                     */0x00000100L),
        UNILATERAL(/*                            */0x00000200L),
        RECENT_DELETED(/*                        */0x00000400L),
        CARD_EXPIRED(/*                          */0x00000800L),
        CARD_POS(/*                              */0x00001000L),
        CARD_PIN(/*                              */0x00002000L),
        DOWNLOADED(/*                            */0x00004000L),
        ACCOUNT_CLOSING(/*                       */0x00008000L),
        INTERNAL(/*                              */0x00010000L),
        MOBILE_TERMINAL(/*                       */0x00020000L),
        BUSINESS(/*                              */0x00040000L),
        HAS_FEE(/*                               */0x00080000L),
        FORCED(/*                                */0x00100000L),
        MASSPAY(/*                               */0x00200000L),
        XCLICK(/*                                */0x00400000L),
        FRAUD_QUEUE(/*                           */0x00800000L),
        CREDIT_LIMIT_EXEMPT(/*                   */0x01000000L),
        INSTANT_ACH(/*                           */0x02000000L),
        ECHECK(/*                                */0x04000000L),
        HELD_FOR_UPGRADE(/*                      */0x08000000L),
        INITIALLY_PENDING(/*                     */0x10000000L),
        CC_WITHDRAW(/*                           */0x20000000L),
        HELD_FOR_RECEIVING(/*                    */0x40000000L);

        private final long flag;

        private Flag1(long flag) {
            this.flag = flag;
        }

        public long getValue() {
            return flag;
        }

        public boolean isSet(long flags) {
            return (flag & flags) > 0;
        }

        public boolean isSet(BigInteger flags) {
            return flags != null && isSet(flags.longValue());
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.FLAGS2 column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Flag2 {
        INTERNATIONAL(/*                         */0x00000001L),
        TO_GAMING(/*                             */0x00000002L),
        HAS_BONUS(/*                             */0x00000004L),
        CHARGED_BACK(/*                          */0x00000008L),
        IS_WBN(/*                                */0x00000010L),
        HAS_CONFIRMED_ADDRESS(/*                 */0x00000020L),
        HAS_CART(/*                              */0x00000040L),
        FROM_GAMING(/*                           */0x00000080L),
        COUNTERPARTY_BUSINESS(/*                 */0x00000100L),
        SCHEDULED_PMT(/*                         */0x00000200L),
        SUBSCRIPTION_PMT(/*                      */0x00000400L),
        INST_ACH_UNSECURED(/*                    */0x00000800L),
        ACH_REPRESENTED(/*                       */0x00001000L),
        REVERSAL_PENDING(/*                      */0x00002000L),
        CARD_VIRTUAL(/*                          */0x00004000L),
        HAS_UNCLEARED_ECHECK(/*                  */0x00008000L),
        SHIPPED_VIA_US(/*                        */0x00010000L),
        INITIALLY_HELD_FOR_RECIPIENT(/*          */0x00020000L),
        INITIALLY_HELD_FOR_VERIFY(/*             */0x00040000L),
        INITIALLY_HELD_FOR_SHIPPING(/*           */0x00080000L),
        INITIALLY_HELD_FOR_UPGRADE(/*            */0x00100000L),
        FROM_EAN(/*                              */0x00200000L),
        REVERSAL_PAYEE_INITIATED(/*              */0x00400000L),
        FOR_FASTPAY(/*                           */0x00800000L),
        COMPOSITE_FEE(/*                         */0x01000000L),
        INITIALLY_HELD_FOR_CCRISK(/*             */0x02000000L),
        INITIALLY_HELD_FOR_CURRENCY(/*           */0x04000000L),
        DONT_SHOW_SHIP_BUTTON(/*                 */0x08000000L),
        CROSS_CURRENCY_FUNDED(/*                 */0x10000000L),
        INITIALLY_HELD_FOR_VOLUME(/*             */0x20000000L),
        CARD_HIDDEN(/*                           */0x40000000L);

        private final long flag;

        private Flag2(long flag) {
            this.flag = flag;
        }

        public long getValue() {
            return flag;
        }

        public boolean isSet(long flags) {
            return (flag & flags) > 0;
        }

        public boolean isSet(BigInteger flags) {
            return flags != null && isSet(flags.longValue());
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.FLAGS3 column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Flag3 {
        INITIALLY_INACTIVE_ECHECK(/*             */0x00000001L),
        INACTIVE_ECHECK(/*                       */0x00000002L),
        WARRANTY_APPLIES(/*                      */0x00000004L),
        WARRANTY_OFFERED(/*                      */0x00000008L),
        FROM_INVOICE(/*                          */0x00000010L),
        UNVERIFIED_INSTANT_ACH(/*                */0x00000020L),
        NO_DISPLAY_USER(/*                       */0x00000040L),
        REPORTED_AS_UNAUTHORIZED(/*              */0x00000080L),
        TO_jpinc_CAT(/*                           */0x00000100L),
        HAS_TEMP_HOLD(/*                         */0x00000200L),
        jpinc_MOTORS_DEPOSIT(/*                   */0x00000400L),
        INCENTIVE(/*                             */0x00000800L),
        PROMOTION(/*                             */0x00001000L),
        jpinc_AUTOPAY(/*                          */0x00002000L),
        REVERSAL_AMOUNT_IS_PARTIAL(/*            */0x00004000L),
        jptest_BUYER_PROTECTION(/*               */0x00008000L),
        WAX(/*                                   */0x00010000L),
        EXEMPT_NON_CC_PORTION_FROM_LIMIT(/*      */0x00020000L),
        MANUALLY_TINKERED_WITH(/*                */0x00040000L),
        SPP_ELIGIBLE(/*                          */0x00080000L),
        INITIALLY_HELD_FOR_AUCT_RECV_LIMIT(/*    */0x00100000L),
        HELD_FOR_PURSE_LIMIT(/*                  */0x00200000L),
        INITIALLY_HELD_FOR_PURSE_LIMIT(/*        */0x00400000L),
        MERCHANT_PULL(/*                         */0x00800000L),
        MANUAL_EFT(/*                            */0x01000000L),
        HAS_UNCLEARED_MANUAL_EFT(/*              */0x02000000L),
        MSP(/*                                   */0x04000000L),
        BUYER_CREDIT_FUNDED(/*                   */0x08000000L),
        HAS_BC_PROMO(/*                          */0x10000000L),
        INITIALLY_HELD_FOR_MEFT_LIMIT(/*         */0x20000000L),
        HAS_ATTACK_CASE(/*                       */0x40000000L);

        private final long flag;

        private Flag3(long flag) {
            this.flag = flag;
        }

        public long getValue() {
            return flag;
        }

        public boolean isSet(long flags) {
            return (flag & flags) > 0;
        }

        public boolean isSet(BigInteger flags) {
            return flags != null && isSet(flags.longValue());
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.FLAGS4 column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Flag4 {
        HAS_SELLER_OPTIONS(/*                    */0x00000001L),
        VIRTUAL_TERMINAL(/*                      */0x00000002L),
        WSP_SPP(/*                               */0x00000004L),
        ELV(/*                                   */0x00000010L),
        DCC(/*                                   */0x00000020L),
        jpinc_SPPP(/*                             */0x00000040L),
        DELAYED_SETTLEMENT(/*                    */0x00000080L),
        WALLET(/*                                */0x00000100L),
        RISK_FILTER_ALERTED(/*                   */0x00000200L),
        GXO(/*                                   */0x00000400L),
        IS_ORDER_RELATED(/*                      */0x00000800L),
        FROM_STANDIN(/*                          */0x00001000L),
        MARKET(/*                                */0x00002000L), // Deprecated; do not use/set!
        INTEGRATED_EFT(/*                        */0x00004000L),
        DDG(/*                                   */0x00008000L),
        PPPR(/*                                  */0x00010000L),
        SKYPE_INITIATED(/*                       */0x00020000L),
        HANDOUT_ACTIVITY_ID(/*                   */0x00040000L),
        DEBITCARD_INTERNATIONAL_FEE(/*           */0x00080000L),
        PSP_COVERED(/*                           */0x00100000L),
        HAS_SUBBALANCE_TRANSACTION(/*            */0x00200000L),
        HELD_FOR_RISK_CONTROLS(/*                */0x00400000L),
        HELD_FOR_RCV2(/*                         */0x00800000L),
        INITIALLY_HELD_FOR_PARENT_ACCEPT(/*      */0x01000000L),
        UNDER_RISK_REVIEW(/*                     */0x02000000L),
        COMPLETED_RISK_REVIEW(/*                 */0x04000000L),
        ADAPTIVE_PAYMENT(/*                      */0x08000000L),
        MARKTPLAATS(/*                           */0x10000000L),
        SEND_MONEY(/*                            */0x20000000L),
        EXTERNAL_SEND_MONEY(/*                   */0x40000000L);

        private final long flag;

        private Flag4(long flag) {
            this.flag = flag;
        }

        public long getValue() {
            return flag;
        }

        public boolean isSet(long flags) {
            return (flag & flags) > 0;
        }

        public boolean isSet(BigInteger flags) {
            return flags != null && isSet(flags.longValue());
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.FLAGS5 column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Flag5 {
        EXPRESS_CHECKOUT_FOR_jpinc(/*                       */"0000000000000001"),
        HAS_PAYMENT_EXTENSIONS(/*                          */"0000000000000002"),
        USES_RECEIVABLES(/*                                */"0000000000000004"),
        HSS(/*                                             */"0000000000000008"),
        ITEM_NOT_RECEIVED_SELLER_PROTECTION_ELIGIBLE(/*    */"0000000000000010"),
        UNAUTH_SELLER_PROTECTION_ELIGIBLE(/*               */"0000000000000020"),
        IS_UNAUTH_SELLER_PROTECTION_DUE_TO_HSS(/*          */"0000000000000040"),
        TRANSACTION_PENDING_USER_COMMITMENT(/*             */"0000000000000080"),
        INVOICE_PAYMENT(/*                                 */"0000000000000100"),
        DELAYED_FUNDING(/*                                 */"0000000000000200"),
        INITIATED_BY_EXTERNAL_APPLICATION(/*               */"0000000000000400"),
        THUNDERBIRD_PROCESSED(/*                           */"0000000000000800"),
        DIGITAL_GOODS(/*                                   */"0000000000001000"),
        MICROPAYMENT(/*                                    */"0000000000002000"),
        PREPAID_ADD_FUNDS(/*                               */"0000000000004000"),
        POSTPAID_RECEIVABLES_PAYOFF(/*                     */"0000000000008000"),
        HAS_EXTERNAL_PAYOUT(/*                             */"0000000000010000"),
        HAS_EXTERNAL_DISPUTE_RECOUPMENT(/*                 */"0000000000020000"),
        AUTOMATED_WITHDRAWAL(/*                            */"0000000000040000"),
        UNVERIFIED_ECHECK(/*                               */"0000000000080000"),
        IS_POINT_OF_SALE(/*                                */"0000000000100000"),
        DISALLOW_REFUND_AND_EXTERNAL_REVERSAL(/*           */"0000000000200000"),
        MASSPAY_2_0(/*                                     */"0000000000400000"),
        PAY_AFTER_DELIVERY(/*                              */"0000000000800000"),
        EXTERNAL_FX_SOLUTION(/*                            */"0000000001000000"),
        IS_CHAINED_REFUND(/*                               */"0000000002000000"),
        IS_USING_INLINE_CHECKOUT_FLOW(/*                   */"0000000004000000"),
        IS_MOBILE_PAYMENT_ACCEPTANCE_PROCESSED(/*          */"0000000008000000"),
        HAS_CC_AUTH_DEFERRED(/*                            */"0000000010000000"),
        HAS_DEFERRED_CC_AUTH_SUCCESS(/*                    */"0000000020000000"),
        HAS_DEFERRED_CC_AUTH_FAILURE(/*                    */"0000000040000000"),
        HAS_DELAYED_HOLDING_CREDIT(/*                      */"0000000080000000"),
        IS_PAY_AFTER_PURCHASE(/*                           */"0000000100000000"),
        IS_POSTPAID_MSB(/*                                 */"0000000200000000"),
        HAS_STORE_INFO(/*                                  */"0000000400000000"),
        IS_WEB_PAYMENT_STANDARD(/*                         */"0000000800000000"),
        RISK_DECISION_UNKNOWN(/*                           */"0000001000000000"),
        HAS_CARD_PRESENT(/*                                */"0000002000000000"),
        IS_CLOSING_BAD_RECEIVABLE(/*                       */"0000004000000000"),
        HAS_NO_FI_SEND_LIMIT_ALLOWANCE_APPLIED(/*          */"0000008000000000"),
        WAS_DISPUTED(/*                                    */"0000010000000000"),
        IS_FORCED_POST(/*                                  */"0000020000000000"),
        IS_NON_REFERENCED_REFUND(/*                        */"0000040000000000"),
        HAS_EXTERNALY_PROCESSED_REFUND(/*                  */"0000080000000000"),
        IS_COUPLED_PAYMENT(/*                              */"0000100000000000"),
        IS_SHIPPING_INTERMEDIATED(/*                       */"0000200000000000"),
        HAD_COMPLIANCE_HOLD(/*                             */"0000400000000000"),
        HAS_COMPLIANCE_HOLD(/*                             */"0000800000000000"),
        IS_FLEXIBLE_INCONTEXT_EXPRESS_CHECKOUT(/*          */"0001000000000000"),
        HAS_CORRECTION(/*                                  */"0002000000000000"),
        GENERIC_INSTRUMENT_FUNDED(/*                       */"0004000000000000"),
        HAS_COMPLIANCE_INFO(/*                             */"0008000000000000"),
        IS_FUNDED_BY_PP_SHORT_TERM_CREDIT(/*               */"0010000000000000"),
        HAS_TRANSACTION_ATTRIBUTE(/*                       */"0020000000000000"),
        CHEETAH_PROCESSED(/*                               */"0040000000000000"),
        IS_INSTALLMENT_FUNDED(/*                           */"0080000000000000"),
        HAS_REST_API_INTEGRATION(/*                        */"0100000000000000"),
        HAS_MERCHANT_PAYMENT_CONTEXT_INFO(/*               */"0200000000000000"),
        IS_BUY_ONLINE_PICKUP_IN_STORE(/*                   */"0400000000000000"),
        IS_PAY_UPON_INVOICE(/*                             */"0800000000000000"),
        USES_LOCAL_DELIVERY(/*                             */"1000000000000000"),
        HAS_UNAUTH_BANK_RETURN(/*                          */"2000000000000000"),
        IS_UNAUTH_PREMIUM_SELLER_PROTECTION_ELIGIBLE(/*    */"4000000000000000"),
        IS_INVOICE_QR_PAYMENT(/*                           */"8000000000000000");

        private final BigInteger flag;

        private Flag5(long flag) {
            this.flag = BigInteger.valueOf(flag);
        }

        private Flag5(String flag) {
            this.flag = new BigInteger(flag, 16);
        }

        private Flag5(BigInteger flag) {
            this.flag = flag;
        }

        public BigInteger getValue() {
            return flag;
        }

        public boolean isSet(long flags) {
            return isSet(BigInteger.valueOf(flags));
        }

        public boolean isSet(BigInteger flags) {
            return flags != null && flag.and(flags).compareTo(BigInteger.ZERO) > 0;
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.FLAGS6 column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Flag6 {
        IS_jptest_PLUS_ENABLED(/*                          */"0000000000000001"),
        IS_jptest_FUNDED_IF_EXTERNAL_VENDOR_FAILURE(/*     */"0000000000000002"),
        IS_PENDING_FOR_POSSIBLE_USER_CANCELLATION(/*       */"0000000000000004"),
        IS_SWITCH_INITIATED_MILLENNIUM_TRANSACTION(/*      */"0000000000000008"),
        IS_MILLENNIUM_STANDIN_REPLAY(/*                    */"0000000000000010"),
        HAS_PERSONAL_PAYMENTS_INFO(/*                      */"0000000000000020"),
        IS_MULTIPART_U2U(/*                                */"0000000000000040"),
        IS_CA_TRANSACTION(/*                               */"0000000000000080"),
        IS_NON_jptest_MERCHANT(/*                          */"0000000000000100"),
        HAS_DELAYED_DISBURSEMENT(/*                        */"0000000000000200"),
        IS_FUNDS_FULLY_DISBURSED(/*                        */"0000000000000400"),
        IS_MILLENNIUM_SETTLEMENT_PROCESSED(/*              */"0000000000000800"),
        IS_SHORTPAY_TRANSACTION(/*                         */"0000000000001000"),
        TOPUP_BALANCE(/*                                   */"0000000000002000"),
        PROGRESSIVE_ONBOARDING_SETUP_INCOMPLETE(/*         */"0000000000004000"),
        HELD_FOR_PENDING_DEPOSIT(/*                        */"0000000000008000"),
        IS_FLEX_PROCESSED(/*                               */"0000000000010000"),
        INCONTEXT_UPGRADED_MEMBER_ACCOUNT(/*               */"0000000000020000"),
        HAS_UNILATERAL_HOLD(/*                             */"0000000000040000");

        private final BigInteger flag;

        private Flag6(String flag) {
            this.flag = new BigInteger(flag, 16);
        }

        private Flag6(BigInteger flag) {
            this.flag = flag;
        }

        public BigInteger getValue() {
            return this.flag;
        }

        public boolean isSet(BigInteger flags) {
            return flags != null && this.flag.and(flags).compareTo(BigInteger.ZERO) > 0;
        }
    }

    /**
     * Enumeration to house all possible values that can be stored in the WTRANSACTION.COUNTERPARTY_ALIAS_TYPE column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum CounterpartyAliasType {
        EMAIL(/*      */'E'),
        PHONE(/*      */'P'),
        QB_AUTHID(/*  */'Q'),
        SKYPE(/*      */'S');

        private final char aliasType;

        private CounterpartyAliasType(char aliasType) {
            this.aliasType = aliasType;
        }

        public char getValue() {
            return aliasType;
        }

        public byte getByte() {
            return (byte) aliasType;
        }
    }
}
