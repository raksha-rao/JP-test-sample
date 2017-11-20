package com.jptest.payments.fulfillment.testonia.core.util;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @JP Inc.
 *
 */

public class ActivityIdHelper {

    private static final ActivityIdHelper INSTANCE = new ActivityIdHelper();

    private static final AtomicLong usedActivityId = new AtomicLong(0L);

    public static final int ONE_DAY = 60 * 60 * 24 * 1000;

    public final static long reservedBitsMask = 0xFF00000000000000L;

    // Detail info, see https://jira.billmelater.com/browse/HS-269
    // left shift 40, following the the logic in C++
    // http://xotools.ca1.jptest.com:8080/source/xref/BWR_840_int/biz/Money/Engine/common/ActivityIDHelper.h
    public final static int DAY_SHIFT_FACTOR = 40;
    public final static long dayMask = 0x00FFFF0000000000L;
    public final static long numberOfDaysSinceEpoch2008 = (2008 - 1970) * 365; // approximation
    public final static long numberOfDaysSinceEpoch2100 = (2100 - 1970) * 365; // approximation

    // compute activityDay (or UnixDay): number of days since 1/1/1970.
    public final static long dayInSeconds = 86400; // 24 * 60 * 60;

    public final static int milliSecond = 1000;

    // used by Events DB and WTransaction_P2 DB to compose partition range
    public final static int partitionOffset = 1; // one day ahead or behind.
    public final static int activityIdLifetimeInDays = 31; // max number a days to offset an activity id in live

    private final static int NUM_DAYS_FOR_LOWER_RANGE = 30;

    private ActivityIdHelper() {
    }

    public static ActivityIdHelper instance() {
        return INSTANCE;
    }

    /**
     * Compose the activityId based on the given event time and the given offset.
     * 
     * @param eventTime , such as 1321031172
     * @param offset , offset the beginning or ending time by offset days, could be positive or negative
     */
    public static final long composeActivityId(long eventTime, int offset) {
        /*
         * Activity ID structure day | dbid | sequence 16 bits | 8 bits | 32 bits
         */
        return (eventTime / dayInSeconds) + offset << DAY_SHIFT_FACTOR;
    }

    /**
     * Given an activity Id, extract the day value out of it.
     * 
     * @param activityId
     * @return number of days since 1970
     */
    public static final long getActivityDay(long activityId) {
        return (activityId & dayMask) >> DAY_SHIFT_FACTOR;
    }

    /**
     * Given an Activity Id, returns the first possible activity of the day when the activity happened.
     * 
     * @param activityId Original Activity ID
     * @param offset Number of days to offset (-ve/+ve number)
     * @return First valid activity id of the day (offset by 'offset' days)
     */
    public static final long getFirstActivityOfDay(long activityId, long offset) {
        return getActivityDay(activityId) + offset << DAY_SHIFT_FACTOR;
    }

    /**
     * Return activityId range based on the given list of ctHandles
     * <p/>
     * activity_ids[0] will contain the minimum activity id value activity_ids[1] will contain the first activity id
     * from tomorrow or from the day after the largest activity id (if in the future)
     * <p/>
     * 
     * @param ctHandles
     * @return long[] activity_ids
     */
    // http://d-sjn-cmcgraw.corp.jpinc.com/wiki/index.php/CT_FT_HandleFormat
    public static final long[] getActivityIdRange(final List<String> ctHandles) {
        long[] activityIdRange = new long[2];
        if (ctHandles == null || ctHandles.size() < 1) {
            return activityIdRange;
        }

        // Get the smallest activity id and the biggest activity id from the
        // list of CT Handles (these usually look like the following:
        // 16824769877901454-CT00000000001)
        long t = 0;
        for (String ctHandle : ctHandles) {
            String[] ctHandleParts = ctHandle.split("-");
            t = Long.parseLong(ctHandleParts[0]);
            if (activityIdRange[0] > t || activityIdRange[0] == 0) {
                activityIdRange[0] = t;
            }
            if (activityIdRange[1] < t || activityIdRange[1] == 0) {
                activityIdRange[1] = t;
            }
        }
        // Pad the smallest activity id by 1 day in the past to allow for
        // multiple engines processing post-payment activities ending
        // up with activity id lesser than that of the payment
        {
            long numberOfDaysForStart = getActivityDay(activityIdRange[0]);
            activityIdRange[0] = composeActivityId(numberOfDaysForStart * dayInSeconds, -1);
        }

        // The ending range for the activity ids should be the first activity id
        // from
        // tomorrow [or] the first activity id from the day after the largest
        // activity
        // id value
        {
            long numberOfDaysSinceEpoch = (System.currentTimeMillis() / milliSecond) / dayInSeconds;
            long firstActivityIdTomorrow = composeActivityId(numberOfDaysSinceEpoch * dayInSeconds, partitionOffset);
            if (activityIdRange[1] < firstActivityIdTomorrow) {
                activityIdRange[1] = firstActivityIdTomorrow;
            } else {
                long firstSecondOnMaxActivityDay = getActivityDay(activityIdRange[1]) * dayInSeconds;
                activityIdRange[1] = composeActivityId(firstSecondOnMaxActivityDay, partitionOffset);
            }
        }

        return activityIdRange;
    }

    /**
     * Return activityId range based on the given date range from start to end.
     * <p/>
     * activity_ids[0] will contain the lowest activity id. activity_ids[1] will contain the possible highest activity
     * id based on the offsets passed.
     * 
     * @param start
     * @param end
     * @return
     */
    public static final long[] getActivityIdRange(long start, long end) {
        long[] activityIdRange = new long[2];
        activityIdRange[0] = composeActivityId(start, -partitionOffset);
        activityIdRange[1] = composeActivityId(end, partitionOffset);
        return activityIdRange;
    }

    /**
     * This method is used to check the validity of the ActivityID passed in.
     * 
     * The checks involved are - Is the ActivityID non-zero - The ActivityDay field should be non-zero - The ActivityDay
     * field should not be from the future.
     * 
     * Note: This method makes some basic checks, and it is not intended to provide iron-clad validation. If a client
     * wants to break rules, it is still possible. This method will however, prevent some common errors. This is ported
     * from the c++ world
     * 
     * @param activityId
     * @return boolean
     */
    public static boolean isActivityIdValid(Long activityId) {

        if (activityId == 0 || getActivityDay(activityId) == 0 || isActivityFromAFutureDate(activityId)) {
            return false;
        }
        return true;
    }

    /**
     * This method is used to check if the activity id passed in is from a future date
     * 
     * @param activityId
     * @return boolean
     */
    public static boolean isActivityFromAFutureDate(Long activityId) {
        long numberOfDaysSinceEpoch = System.currentTimeMillis() / milliSecond;
        if (getActivityDay(activityId) * dayInSeconds > numberOfDaysSinceEpoch) {
            return true;
        }
        return false;
    }

    /**
     * Returns time range based on the given date from the input activity id. activityIdRange[0] will contain the day
     * the activity was created-2. activityIdRange[1] will contain the day the activity was created+2.
     * 
     * @param activityId activity id
     * @return lower and upper time bound
     */
    public static final Long[] getTimeRangeFromActivityId(long activityId) {
        return getTimeRangeFromActivityId(activityId, 2l, 2l);
    }

    /**
     * Returns time range based on the given date from the input activity id. activityIdRange[0] will contain the day
     * the activity was created-daysBefore. activityIdRange[1] will contain the day the activity was created+daysAfter.
     * 
     * @param activityId activity id, the activity ID that should be processed
     * @param daysBefore number of days before the day of the activity ID
     * @param daysAfter number of days after the day of the activity ID
     * @return lower and upper time bound
     */
    public static final Long[] getTimeRangeFromActivityId(long activityId, long daysBefore, long daysAfter) {
        Long[] activityIdRange = new Long[2];
        // days since 1970 * secs per day
        activityIdRange[0] = (getActivityDay(activityId) - daysBefore) * dayInSeconds;
        activityIdRange[1] = (getActivityDay(activityId) + daysAfter) * dayInSeconds;
        return activityIdRange;
    }

    /**
     * Given an activity id and the number of days to pad (negative for past and positive for future), the function
     * returns the unixtime for the start of the day.
     * 
     * @param activityId Activity ID in question
     * @param daysToPad Number of days before/after activity (negative/zero/positive values)
     * @return
     */
    public static long getTimeCreated(BigInteger activityId, int daysToPad) {
        if (activityId == null) {
            return 0;
        }
        return (getActivityDay(activityId.longValue()) + daysToPad) * dayInSeconds;
    }

    /**
     * Returns true if the given activityId string corresponds to a valid activity Id with the reserved bits set to 0.
     * 
     * @param activityId Activity ID String to validate
     * @return true if valid activity id
     */
    public static boolean isValidActivityIdNumber(String activityId) {
        if (StringUtils.isNotEmpty(activityId) && StringUtils.isNumeric(activityId)) {
            BigInteger activityIdBigInteger = new BigInteger(activityId);
            if (BigInteger.valueOf(Long.MAX_VALUE).compareTo(activityIdBigInteger) > 0) {
                long activityIdLong = activityIdBigInteger.longValue();
                if ((activityIdLong & reservedBitsMask) == 0) {
                    long daysSinceEpoch = getActivityDay(activityIdLong);
                    if (daysSinceEpoch > numberOfDaysSinceEpoch2008 && daysSinceEpoch < numberOfDaysSinceEpoch2100) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method to compute the activityId for partition prunning using the unixDay part of the given referenceValue.
     * The 64 bit activityId is contains the following components : (56-63) ReservedBit+ (40-55) UnixDay + (32-39) DB
     * Host + 0-31( DB Sequence ) This method computes an activity Id that can be used for partition prunning, based
     * upon the assumption that the unixDay found in the given activityId will be same as the unixDay used while
     * generating the associated paymentSideId
     * 
     * @param referenceValue
     * @return an activityId computed for partition prunning;
     * @throws InvalidActivityIdException for invalid reference values
     */
    public static BigInteger computeActivityIdForPartitionPrunning(String referenceValue) throws Exception {

        if (!isValidActivityIdNumber(referenceValue)) {
            throw new Exception("Invalid ReferenceValue : " + referenceValue);
        }

        // extract the unix day from the given activityId
        Long unixDay = (Long.parseLong(referenceValue) >> DAY_SHIFT_FACTOR) - NUM_DAYS_FOR_LOWER_RANGE;

        // pow(2,40)
        Long paymentSideId = unixDay << DAY_SHIFT_FACTOR;

        return BigInteger.valueOf(paymentSideId);
    }

    /**
     * Return the time range based on the minimum and maximum dates from the input activity IDs.
     * 
     * @param activityIDs the activity ID list that should be processed
     * @param daysBefore number of days before the day of the minimum activity ID
     * @param daysAfter number of days after the day of the maximum activity ID
     * @return lower and upper time bound
     **/
    public static final Long[] getTimeRangeFromActivityIdList(Collection<BigInteger> activityIDs, int daysBefore,
            int daysAfter) {

        if (CollectionUtils.isEmpty(activityIDs)) {
            throw new IllegalArgumentException("List of activity ids must contain at least one item");
        }
        Long min = Long.MAX_VALUE;
        Long max = Long.valueOf(0);
        for (BigInteger activityId : activityIDs) {
            if (activityId.longValue() > max) {
                max = activityId.longValue();
            }
            if (activityId.longValue() > 0 && activityId.longValue() < min) {
                min = activityId.longValue();
            }
        }
        long daysBetweenMinAndMax = (getActivityDay(max) - getActivityDay(min) + 2);
        return getTimeRangeFromActivityId(min, daysBefore, daysBetweenMinAndMax + daysAfter);
    }

    /**
     * Given a list of CTHandles, returns the first activity id for the day of the oldest CTHandle in the list. Returns
     * 0 in case of failure to find valid CTHandles (in the format: ACTIVITY_ID-CT00000000001).
     *
     * @param ctHandles List of CTHandles
     * @return oldest possible activity id
     */
    public static long computeOldestPossibleActivityId(Collection<String> ctHandles) {
        // Find the least activity id from the list of CTHandles
        long oldestActivityId = 0L;
        if (ctHandles != null && !ctHandles.isEmpty()) {
            for (String ctHandle : ctHandles) {
                if (ctHandle.contains("-")) {
                    long activityId = Long.parseLong(ctHandle.split("-")[0]);
                    if (oldestActivityId == 0L || activityId < oldestActivityId) {
                        oldestActivityId = activityId;
                    }
                }
            }
        }

        // Return the activity id masked to show only the day portion of it
        return oldestActivityId & dayMask;
    }

    public long genActivityIdByTime(long timeInMills) {
        long newActivityId;
        newActivityId = (timeInMills / ONE_DAY) << DAY_SHIFT_FACTOR;
        newActivityId = newActivityId + millisSinceStartOfDay(timeInMills);
        return newActivityId;
    }

    private Long millisSinceStartOfDay(long timeInMills) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMills);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Long startOfDay = cal.getTimeInMillis();
        return (timeInMills - startOfDay);
    }

    public Long nextActivityId() {
        long lastActivityId, newActivityId;
        do {
            lastActivityId = usedActivityId.get();
            do {
                newActivityId = genActivityIdByTime(System.currentTimeMillis());
            } while (newActivityId == lastActivityId);
        } while (!usedActivityId.compareAndSet(lastActivityId, newActivityId));
        return newActivityId;
    }
}
