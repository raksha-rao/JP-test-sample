package com.jptest.payments.fulfillment.testonia.model.ignorable.flags;

import java.util.List;
import java.util.Map;

/**
 * This class is POJO for flagsInfo in ignorable flags json.
 * 
 * @JP Inc.
 *
 */
public class FlagsInfo {

    String ancestorTag;
    Map<String, List<String>> flagsToIgnore;

    public String getAncestorTag() {
        return ancestorTag;
    }

    public void setAncestorTag(String ancestorTag) {
        this.ancestorTag = ancestorTag;
    }

    public Map<String, List<String>> getFlagsToIgnore() {
        return flagsToIgnore;
    }

    public void setFlagsToIgnore(Map<String, List<String>> flagsToIgnore) {
        this.flagsToIgnore = flagsToIgnore;
    }
    
    @Override
    public String toString() {
        return "AncestorTag : " + ancestorTag
                + "\nflagsToIgnore : " + flagsToIgnore;
    }
    
}
