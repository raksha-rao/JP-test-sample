package com.jptest.payments.fulfillment.testonia.core.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @JP Inc.
 *
 */
public class ActivityIdHelperTest {
    
  private long activitId = 19111711571188493L;

  @Test
  public void composeActivityId() {
    long actId = ActivityIdHelper.composeActivityId(System.currentTimeMillis(), 1);
    Assert.assertTrue(actId > 0);
  }

  @Test
  public void computeActivityIdForPartitionPrunning() {
   BigInteger retActId = BigInteger.ZERO;   
      try {
          retActId = ActivityIdHelper.computeActivityIdForPartitionPrunning(String.valueOf(activitId));
    } catch (Exception e) {
    }
     Assert.assertTrue(retActId.longValue() > 0);
  }

  @Test
  public void computeOldestPossibleActivityId() {
    Collection<String> ctHandles = Arrays.asList("19111711571188493-ct","19111711571188496-ct"); 
    long oldId = ActivityIdHelper.computeOldestPossibleActivityId(ctHandles);
    Assert.assertTrue(oldId > 0);
  }

  @Test
  public void genActivityIdByTime() {
    ActivityIdHelper activityIDHelper = ActivityIdHelper.instance();  
    long actId = activityIDHelper.genActivityIdByTime(System.currentTimeMillis());
    Assert.assertTrue(actId > 0);
  }

  @Test
  public void getActivityDay() {
    long day = ActivityIdHelper.getActivityDay(activitId);
    Assert.assertTrue(day > 0);
  }

  @Test
  public void getFirstActivityOfDay() {
    long actId = ActivityIdHelper.getFirstActivityOfDay(activitId, 1);
    Assert.assertTrue(actId > 0);
  }

  @Test
  public void getTimeCreated() {
    long timeCreated = ActivityIdHelper.getTimeCreated(BigInteger.valueOf(activitId), 1);
    Assert.assertTrue(timeCreated > 0);
  }

  @Test
  public void getTimeRangeFromActivityIdList() {
    Collection<BigInteger> idList = Arrays.asList(BigInteger.valueOf(191117115711884930L),BigInteger.valueOf(19111711571188496L));
    Long[] timeRange = ActivityIdHelper.getTimeRangeFromActivityIdList(idList,1,1);
    Assert.assertTrue(timeRange.length > 0);
  }

  @Test
  public void isActivityFromAFutureDate() {
    ActivityIdHelper.isActivityFromAFutureDate(activitId);
  }

  @Test
  public void isActivityIdValid() {
      ActivityIdHelper.isActivityIdValid(activitId);
  }

  @Test
  public void isValidActivityIdNumber() {
      ActivityIdHelper.isValidActivityIdNumber(String.valueOf(activitId));
  }

  @Test
  public void nextActivityId() {
      ActivityIdHelper idHelper = ActivityIdHelper.instance();
      Assert.assertTrue(idHelper.nextActivityId() > 0);
  }
  
  @Test
  public void getActivityIdRange() {
      List<String> ctHandles = Arrays.asList("19111711571188493-ct","19111711571188496-ct"); 
      long[] actvityIdRange = ActivityIdHelper.getActivityIdRange(ctHandles);
      Assert.assertTrue(actvityIdRange.length > 0);
      actvityIdRange =  ActivityIdHelper.getActivityIdRange(System.currentTimeMillis() , System.currentTimeMillis() +1000);
      Assert.assertTrue(actvityIdRange.length > 0);
  }
  
  @Test
  public void getTimeRangeFromActivityId() {
      Long[] range = ActivityIdHelper.getTimeRangeFromActivityId(activitId);
      Assert.assertTrue(range.length > 0);
  }
}
