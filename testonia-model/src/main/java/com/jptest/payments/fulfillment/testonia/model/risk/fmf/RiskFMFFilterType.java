package com.jptest.payments.fulfillment.testonia.model.risk.fmf;

/**
 * Represents the FMF filter types. 
 * There is no central Risk library which provides these types
 * so we will maintain it here till that point.
 * 
 */
public enum RiskFMFFilterType {

    AVS_No_Match(1),
    AVS_Patial_Match(2),
    AVS_Unavailable(3),
    CSC_Mismatch(4),
    Transaction_Amount(5),
    Confirmed_Address(6),
    Country_List(7),
    High_Order_Number(8),
    Billing_Shipping_Mismatch(9),
    High_Risk_Zip_Check(10),
    High_Risk_Feight_Check(11),
    Floor_Amount(12),
    IP_Address_Velocity(13),
    High_Risk_Email_Domain(14),
    High_Risk_Bin_Check(15),
    High_Risk_IP_Check(16),
    Fraud_Model(17),
    Billing_Address_Optional(18);

    private final int ruleType;

    private RiskFMFFilterType(int ruleType) {
        this.ruleType = ruleType;
    }

    public int getValue() {
        return this.ruleType;
    }

}
