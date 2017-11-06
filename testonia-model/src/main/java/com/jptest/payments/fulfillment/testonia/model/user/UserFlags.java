package com.jptest.payments.fulfillment.testonia.model.user;

/**
 * Enum to maintain userflag name , value and group data.
 */
public enum UserFlags {

    WUSER_AUTH_ACH_RISK_EXEMPT_SENDER(33554432L, 1005L),
    WUSER_AUTH_CC_RISK_EXEMPT_SENDER(1073741824, 1005L), // If CC is being rejected by risk for the user, add this flag
    WUSER_AUTH_DC_ELIGIBLE_INTL(256, 1005L),
    WUSER_FLAG_IPN_ENABLED(4096, 1001L),
    WUSER_FLAG2_RISK_FILTER_ACCEPTED(32768, 1002L),
    WUSER_FLAG2_RISK_FILTER_ENABLED(16384, 1002L),
    WUSER_FLAG4_GUEST_ACCOUNT(33554432L, 1004L),
    WUSER_FLAG4_EXCEPTION(1073741824L, 1004L),
    WUSER_FLAG2_IS_VERIFIED(536870912, 1002L),
    WUSER_FLAG_HOLD_RECEIVE_CC(524288, 1001L),
    WUSER_FLAGS_GROUP_LARGE_MERCHANT_MAY_REFUND_USING_BALANCE_CONVERSION(256, 1001L),
    WUSER_FLAG4_COMMERCIAL_ENTITY(32,1004L),
    WUSER_FLAG4_COMMERCIAL_ENTITY_TOU(64,1004L);

    UserFlags(long val, long group) {
        this.val = val;
        this.group = group;
    }

    public long getVal() {
        return val;
    }

    public long getGroup() {
        return group;
    }

    private long val;
    private long group;
}
