package com.jptest.payments.fulfillment.testonia.model.util;

import java.math.BigInteger;

/**
@JP Inc.
 */
public enum WASOrderFlags {
    WAS_ORDER_FLAG_USES_AUTO_CAPTURE(0x0000000200000000L); //hex value autoboxed to dec

    private Long longValue;
    private BigInteger bigIntegerValue;

    WASOrderFlags(Long val) {
        this.longValue = val;
        this.bigIntegerValue = new BigInteger(String.valueOf(val));
    }

    public Long longValue() {
        return longValue;
    }
    public BigInteger bigIntegerValue() {
        return bigIntegerValue;
    }
}
