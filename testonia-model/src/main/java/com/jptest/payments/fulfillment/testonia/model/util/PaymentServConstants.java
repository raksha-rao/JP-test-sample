package com.jptest.payments.fulfillment.testonia.model.util;

/**
 * Class to hold enum values from paymentservconstants.h
 * 
 * @JP Inc.
 *
 */

public final class PaymentServConstants {

    /*
     * Bank Disposition
     * http://xotoolslvs01.qa.jptest.com/gitsource/xref/Core-R__code__master/github/biz/Money/service/payment/PaymentServConstants.h#207
     */
    public enum Disposition {
        DISPOSITION_COMPLETED(/*                            */100),
        DISPOSITION_REPRESENTED(/*                          */200),
        DISPOSITION_FAILED(/*                               */300),
        DISPOSITION_RANDOM_DEPOSIT_FAILED(/*                */400),
        DISPOSITION_BANK_NOTICE_OF_CORRECTION(/*            */500),
        DISPOSITION_BANK_DDI_CANCELLATION_OR_AMMENDMENT(/*  */600),
        DISPOSITION_RANDOM_DEPOSIT_RECOVERY_FAILED(/*       */700);

        private final int disposition;

        private Disposition(int disposition) {
            this.disposition = disposition;
        }

        public int getValue() {
            return disposition;
        };
    }
}
