package com.jptest.payments.fulfillment.testonia.model.user;

/**
 * Enum to maintain merchant preferences for FTs.
 */
public enum UserPreferences {

	//Accept payments from users who do NOT have a domestic account
    DOMESTIC("PAYMENT", "RECEIVING_PREFERENCES", true),
    //Accept payments initiated from the Send Money tab
    NO_SEND_MONEY("PAYMENT", "RECEIVING_PREFERENCES",true),
    //Require the sender to use a currency that the recipient holds a balance in
    AUTO_ACCEPT_CURRENCY("PAYMENT", "RECEIVING_PREFERENCES",true),
    //Allows a user to block payment if the buyer shipping address is non domestic
    SHIPPING_LIMITED_TO_DOMESTIC("PAYMENT", "RECEIVING_PREFERENCES",true),
    //Accept payments from users without a confirmed address
    SHIPPING_ADDRESS("PAYMENT", "RECEIVING_PREFERENCES",true),
	//Payment decision on transaction in case of compliance hit:
    OVERRIDE_COMPLIANCE_PEND("PAYMENT", "RECEIVING_PREFERENCES",false);
	


    UserPreferences(
            String category, String group, boolean isSubGroup) {
        this.category = category;
        this.group = group;
        this.isSubGroup=isSubGroup;

    }

    public String getCategory() {
        return category;
    }

    public String getGroup() {
        return group;
    }

	public boolean isSubGroup() {
		return isSubGroup;
	}
	
    private String category;
    private String group;
    private boolean isSubGroup;

}
