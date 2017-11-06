package com.jptest.payments.fulfillment.testonia.model.util;

public final class WUserHoldingConstants {

    /**
     * Enumeration to house all possible values that can be stored in the WUSER_HOLDING.FLAGS column.
     * 
     * Keep this sorted based on values (no duplicates allowed).
     */
    public enum Flags {
        PRIMARY(/*                          */0x00000001L),
        AUTO_RECEIVE(/*                     */0x00000004L),
        CLOSED(/*                           */0x00000008L),
        MONEY_MARKET(/*                     */0x00000010L),
        CONVERT_EXEMPT(/*                   */0x00000020L),
        HOLDING_BALANCE_LOAD_REQUIRED(/*    */0x00000040L),
        MULTI_CURRENCY_WITHDRAW_ENABLED(/*  */0x00000080L),
        INVALID_HOLDING(/*                  */0x00000100L);

        private final long flag;

        private Flags(long flag) {
            this.flag = flag;
        }

        public long getValue() {
            return flag;
        }

        public boolean isSet(long flags) {
            return (flag & flags) > 0;
        }

    }
}
