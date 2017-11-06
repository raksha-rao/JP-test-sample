package com.jptest.payments.fulfillment.testonia.core.util;

import java.math.BigInteger;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @JP Inc.
 *
 */
public class ProcessDataRequestIdGeneratorTest {

  @Test
  public void generateAccountNumber() {
    BigInteger accountNumber = ProcessDataRequestIdGenerator.generateAccountNumber();
    Assert.assertTrue(accountNumber.longValue() > 0);
  }

  @Test
  public void generateActivityId() {
      BigInteger id = ProcessDataRequestIdGenerator.generateActivityId();
      Assert.assertTrue(id.longValue() > 0);
  }

  @Test
  public void generateActivityIdFromEventId() {
      BigInteger id = ProcessDataRequestIdGenerator.generateActivityIdFromEventId("19111711571188493-19111711571188493");
      Assert.assertTrue(id.longValue() > 0);
  }

  @Test
  public void generateAdjustmentId() {
      BigInteger id = ProcessDataRequestIdGenerator.generateAdjustmentId();
      Assert.assertTrue(id.longValue() > 0);
  }

  @Test
  public void generateAlternativeIdentifier() {
      String id = ProcessDataRequestIdGenerator.generateAlternativeIdentifier();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateBaseFeeId() {
      BigInteger id = ProcessDataRequestIdGenerator.generateBaseFeeId();
      Assert.assertTrue(id.longValue() > 0);
  }

  @Test
  public void generateCustomerTransactionId() {
      String id = ProcessDataRequestIdGenerator.generateCustomerTransactionId();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateEventActivityId() {
      String id = ProcessDataRequestIdGenerator.generateEventActivityId();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateEventId() {
      String id = ProcessDataRequestIdGenerator.generateEventId();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateFeeFundingHandle() {
      String id = ProcessDataRequestIdGenerator.generateFeeFundingHandle();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateFeeHandle() {
      String id = ProcessDataRequestIdGenerator.generateFeeHandle();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateFeeRefHandle() {
      String id = ProcessDataRequestIdGenerator.generateFeeRefHandle();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateFinancialTransactionId() {
      String id = ProcessDataRequestIdGenerator.generateFinancialTransactionId();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateFinancialTransactionPartyHandle() {
      String id = ProcessDataRequestIdGenerator.generateFinancialTransactionPartyHandle();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateFundsInComponentHandle() {
      String id = ProcessDataRequestIdGenerator.generateFundsInComponentHandle();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateFundsOutComponentHandle() {
      String id = ProcessDataRequestIdGenerator.generateFundsOutComponentHandle();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateGenericId() {
      BigInteger id = ProcessDataRequestIdGenerator.generateGenericId();
      Assert.assertTrue(id.longValue() > 0);
  }

  @Test
  public void generateGenericIdString() {
      String id = ProcessDataRequestIdGenerator.generateGenericIdString();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateInstrumentId() {
      BigInteger id = ProcessDataRequestIdGenerator.generateInstrumentId();
      Assert.assertTrue(id.longValue() > 0);
  }

  @Test
  public void generateJournalAid() {
      BigInteger id = ProcessDataRequestIdGenerator.generateJournalAid();
      Assert.assertTrue(id.longValue() > 0);
  }

  @Test
  public void generateMapId() {
      BigInteger id = ProcessDataRequestIdGenerator.generateMapId();
      Assert.assertTrue(id.longValue() > 0);
  }

  @Test
  public void generateTransactionCurrencyConversionVOHandle() {
      String id = ProcessDataRequestIdGenerator.generateTransactionCurrencyConversionVOHandle();
      Assert.assertTrue(id != null);
  }

  @Test
  public void generateTransactionId() {
      BigInteger id = ProcessDataRequestIdGenerator.generateTransactionId();
      Assert.assertTrue(id.longValue() > 0);
  }

  @Test
  public void generateVolumeTierBaseFeeId() {
      BigInteger id = ProcessDataRequestIdGenerator.generateVolumeTierBaseFeeId();
      Assert.assertTrue(id.longValue() > 0);
  }
}
