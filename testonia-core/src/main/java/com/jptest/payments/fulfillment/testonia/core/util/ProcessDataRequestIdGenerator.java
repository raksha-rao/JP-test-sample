package com.jptest.payments.fulfillment.testonia.core.util;

import java.math.BigInteger;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.inject.Singleton;

/**
 * This class provides functions to generate different IDs. Generated IDs can be used replacing in the place holders for
 * tests.
 * 
 * @JP Inc.
 *
 */
@Singleton
public class ProcessDataRequestIdGenerator {
    
    private static ActivityIdHelper activityIdHelper = ActivityIdHelper.instance();
    
    public static BigInteger generateAccountNumber() {
        return generateNumericId();
    }

    public static BigInteger generateActivityId() {
        return BigInteger.valueOf(activityIdHelper.nextActivityId());
    }

    public static String generateCustomerTransactionId() {
        return generateHandle("CT");
    }

    public static String generateEventActivityId() {
        return generateHandle("EA");
    }

    public static String generateEventId() {
        return generateHandle("ET");
    }

    public static String generateFinancialTransactionId() {
        return generateHandle("FT");
    }

    public static String generateFeeHandle() {
        return generateHandle("FE");
    }

    public static String generateFeeRefHandle() {
        return generateHandle("FR");
    }

    public static String generateFeeFundingHandle() {
        return generateHandle("FF");
    }

    public static BigInteger generateJournalAid() {
        return generateNumericId();
    }

    public static String generateFinancialTransactionPartyHandle() {
        return generateHandle("PH");
    }

    public static String generateTransactionCurrencyConversionVOHandle() {
        return generateHandle("CC");
    }

    public static BigInteger generateInstrumentId() {
        return generateNumericId();
    }

    public static String generateFundsOutComponentHandle() {
        return generateHandle("FO");
    }

    public static String generateFundsInComponentHandle() {
        return generateHandle("FI");
    }

    public static String generateAlternativeIdentifier() {
        return generateHandle("AI");
    }

    public static BigInteger generateAdjustmentId() {
        return generateNumericId();
    }

    public static BigInteger generateBaseFeeId() {
        return generateNumericId();
    }

    public static BigInteger generateGenericId() {
        return generateNumericId();
    }

    public static String generateGenericIdString() {
        return "" + generateNumericId();
    }

    public static BigInteger generateMapId() {
        return generateNumericId();
    }

    public static BigInteger generateTransactionId() {
        return generateNumericId();
    }

    public static BigInteger generateVolumeTierBaseFeeId() {
        return generateNumericId();
    }

    public static BigInteger generateActivityIdFromEventId(String eventId) {
        StringTokenizer st = new StringTokenizer(eventId, "-");
        return new BigInteger(st.nextToken());
    }

    private static BigInteger generateNumericId() {
        return BigInteger.valueOf(((Double) Math.abs(Math.random() * Long.MAX_VALUE)).longValue());
    }

    private static String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private static String generateHandle(String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(activityIdHelper.nextActivityId());
        sb.append("-");
        sb.append(prefix);
        sb.append("-");
        sb.append(generateUuid());
        return sb.toString();
    }
}
