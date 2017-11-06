package com.jptest.payments.fulfillment.testonia.model.util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

@Singleton
public class WTransactionFlagsHelper {

    public static Map<String, String> getFlags1(Long input) {
        Map<String, String> flags1Map = new HashMap<>();
        for (WTransactionConstants.Flag1 flag1 : WTransactionConstants.Flag1.values()) {
            if (flag1.isSet(input)) {
                flags1Map.put("flags1_" + flag1.name(), "1");
            }
        }
        return flags1Map;
    }

    public static Map<String, String> getFlags2(Long input) {
        Map<String, String> flags2Map = new HashMap<>();
        for (WTransactionConstants.Flag2 flag2 : WTransactionConstants.Flag2.values()) {
            if (flag2.isSet(input)) {
                flags2Map.put("flags2_" + flag2.name(), "1");
            }
        }
        return flags2Map;
    }

    public static Map<String, String> getFlags3(Long input) {
        Map<String, String> flags3Map = new HashMap<>();
        for (WTransactionConstants.Flag3 flag3 : WTransactionConstants.Flag3.values()) {
            if (flag3.isSet(input)) {
                flags3Map.put("flags3_" + flag3.name(), "1");
            }
        }
        return flags3Map;
    }

    public static Map<String, String> getFlags4(Long input) {
        Map<String, String> flags4Map = new HashMap<>();
        for (WTransactionConstants.Flag4 flag4 : WTransactionConstants.Flag4.values()) {
            if (flag4.isSet(input)) {
                flags4Map.put("flags4_" + flag4.name(), "1");
            }
        }
        return flags4Map;
    }

    public static Map<String, String> getFlags5(BigInteger input) {
        Map<String, String> flags5Map = new HashMap<>();
        for (WTransactionConstants.Flag5 flag5 : WTransactionConstants.Flag5.values()) {
            if (flag5.isSet(input)) {
                flags5Map.put("flags5_" + flag5.name(), "1");
            }
        }
        return flags5Map;
    }

    public static Map<String, String> getFlags6(BigInteger input) {
        Map<String, String> flags6Map = new HashMap<>();
        for (WTransactionConstants.Flag6 flag6 : WTransactionConstants.Flag6.values()) {
            if (flag6.isSet(input)) {
                flags6Map.put("flags6_" + flag6.name(), "1");
            }
        }
        return flags6Map;
    }

}
