package com.jptest.payments.fulfillment.testonia.model.ignorable.flags;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.model.util.WUserHoldingFlagsHelper;

/**
 * Helper class to unset wuser_holding flags
 * @JP Inc.
 *
 */
@Singleton
public class WUserHoldingIgnorableFlagsHelper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WUserHoldingIgnorableFlagsHelper.class);

    public Set<String> getAllExpandedFlags(Map<String, List<String>> allFlags) {

        Set<String> expandedFlags = new HashSet<>();
        for (Entry<String, List<String>> flagsEntry : allFlags.entrySet()) {
            for (String flagToIgnore : flagsEntry.getValue()) {
                expandedFlags.addAll(getExpandedFlags(flagToIgnore));
            }
        }
        LOGGER.info("Expanded Flags : {}", expandedFlags.toString());
        return expandedFlags;
    }
    
    private Set<String> getExpandedFlags(String flagToIgnore) {
        Long flags = Long.valueOf(flagToIgnore);
        Map<String, String> expandedFlags = WUserHoldingFlagsHelper.getFlags(flags);
        return expandedFlags.keySet();
    }
}
