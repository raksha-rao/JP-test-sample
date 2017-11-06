package com.jptest.payments.fulfillment.testonia.business.util;

import java.text.SimpleDateFormat;
import org.apache.commons.lang.RandomStringUtils;


/*
 * Utility related to User creations
 */

public class UserUtility {

    /**
     * Creates Email for a non-existing user (Unilateral payments)
     */
    public static String createEmail() {
        String email = "unilateral-receiver"
                + (new SimpleDateFormat("yyyyMMdd-HHmmss-SSS")).format(System
                        .currentTimeMillis())
                + "@jptest.com";

        return email;
    }

    /**
     * Creates Phone No for a non-existing user (Unilateral payments)
     */
    public static String createPhoneNumber() {
        String phoneNo = RandomStringUtils.randomNumeric(10);
        return phoneNo;
    }
}
