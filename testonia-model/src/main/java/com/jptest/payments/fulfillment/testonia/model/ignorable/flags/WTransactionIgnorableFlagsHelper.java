package com.jptest.payments.fulfillment.testonia.model.ignorable.flags;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionFlagsHelper;


/**
 * Helper class to fetch wtransaction expanded flags from flags values
 * 
 * @JP Inc.
 */
@Singleton
public class WTransactionIgnorableFlagsHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(WTransactionIgnorableFlagsHelper.class);

    public Set<String> getAllExpandedFlags(Map<String, List<String>> allFlags) {

        Set<String> expandedFlags = new HashSet<>();
        for (Entry<String, List<String>> flagsEntry : allFlags.entrySet()) {
            for (String flagToIgnore : flagsEntry.getValue()) {
                expandedFlags.addAll(getExpandedFlags(flagsEntry.getKey(), flagToIgnore));
            }
        }
        LOGGER.info("Expanded Flags : {}", expandedFlags.toString());
        return expandedFlags;
    }

    protected Set<String> getExpandedFlags(String flagType, String flagValue) {
        WtransactionFlagType wtransactionFlagType = WtransactionFlagType.getEnumFromValue(flagType);
        if(wtransactionFlagType == null) {
            LOGGER.warn("Unsupported Flag Type : {}", flagType);
            return new HashSet<String>();
        }
        switch (wtransactionFlagType) {
            case FLAGS1: {
                Long flags = Long.parseLong(flagValue);
                Map<String, String> expandedFlagsMap = WTransactionFlagsHelper.getFlags1(flags);
                return expandedFlagsMap.keySet();
            }

            case FLAGS2: {
                Long flags = Long.parseLong(flagValue);
                Map<String, String> expandedFlagsMap = WTransactionFlagsHelper.getFlags2(flags);
                return expandedFlagsMap.keySet();
            }

            case FLAGS3: {
                Long flags = Long.parseLong(flagValue);
                Map<String, String> expandedFlagsMap = WTransactionFlagsHelper.getFlags3(flags);
                return expandedFlagsMap.keySet();
            }

            case FLAGS4: {
                Long flags = Long.parseLong(flagValue);
                Map<String, String> expandedFlagsMap = WTransactionFlagsHelper.getFlags4(flags);
                return expandedFlagsMap.keySet();
            }
            case FLAGS5: {
                BigInteger flags = new BigInteger(flagValue);
                Map<String, String> expandedFlagsMap = WTransactionFlagsHelper.getFlags5(flags);
                return expandedFlagsMap.keySet();
            }
            case FLAGS6: {
                BigInteger flags = new BigInteger(flagValue);
                Map<String, String> expandedFlagsMap = WTransactionFlagsHelper.getFlags6(flags);
                return expandedFlagsMap.keySet();
            }
            default:
                LOGGER.warn("Unsupported Flag Type {}", flagType);
                return new HashSet<String>();
        }
    }

    enum WtransactionFlagType {
        FLAGS1("flags1"),
        FLAGS2("flags2"),
        FLAGS3("flags3"),
        FLAGS4("flags4"),
        FLAGS5("flags5"),
        FLAGS6("flags6");
        
        String value;

        private WtransactionFlagType(String value) {
            this.value = value;
        }

        public static WtransactionFlagType getEnumFromValue(String value) {

            for (WtransactionFlagType flagType : WtransactionFlagType.values()) {
                if (flagType.value.equals(value)) {
                    return flagType;
                }
            }
            LOGGER.warn("Unsupported Flag Type : {}", value);
            return null;
        }

    }

}
