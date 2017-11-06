package com.jptest.payments.fulfillment.testonia.model.util;

import java.util.HashMap;
import java.util.Map;

public class WUserHoldingFlagsHelper {

    public static Map<String, String> getFlags(Long input) {
        Map<String, String> flags1Map = new HashMap<>();
        for (WUserHoldingConstants.Flags flags : WUserHoldingConstants.Flags.values()) {
            if (flags.isSet(input)) {
                flags1Map.put("flags_" + flags.name(), "1");
            }
        }
        return flags1Map;
    }
}
