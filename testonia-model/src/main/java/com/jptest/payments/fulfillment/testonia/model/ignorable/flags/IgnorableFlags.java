package com.jptest.payments.fulfillment.testonia.model.ignorable.flags;

import java.util.List;

/**
 * This class is POJO for ignorable flags json.
 * @JP Inc.
 *
 */
public class IgnorableFlags {

    List<FlagsInfo> flagsInfo;
    
    public List<FlagsInfo> getFlagsInfo() {
        return flagsInfo;
    }

    public void setFlagsInfo(List<FlagsInfo> flagsInfo) {
        this.flagsInfo = flagsInfo;
    }
    
    @Override
    public String toString() {
        return this.flagsInfo.toString();
    }
}
