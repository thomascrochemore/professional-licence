package org.acrobatt.project.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * A set of useful functions for Dates
 */
public class DateUtils {

    private DateUtils() {}

    /**
     * Determines if the expected forecast date corresponds to the given date plus a delay
     * @param expectedDate the expected date
     * @param delayExpected the delay
     * @param previsionDate the forecast date
     * @return true if the date are "the same"
     */
    public static boolean dateOnSameTime(Date expectedDate, int delayExpected, Date previsionDate){
        Calendar expected = Calendar.getInstance();
        expected.setTime(expectedDate);
        expected.add(Calendar.HOUR_OF_DAY,delayExpected);
        Calendar prevision = Calendar.getInstance();
        prevision.setTime(previsionDate);
        if(expected.get(Calendar.YEAR) != prevision.get(Calendar.YEAR)){
            return false;
        }
        if(expected.get(Calendar.MONTH) != prevision.get(Calendar.MONTH)){
            return false;
        }
        if(expected.get(Calendar.DAY_OF_MONTH) != prevision.get(Calendar.DAY_OF_MONTH)){
            return false;
        }
        if(Math.abs(expected.get(Calendar.HOUR_OF_DAY) - prevision.get(Calendar.HOUR_OF_DAY) ) > 3){
            return false;
        }
        return true;
    }

    /**
     * Truncates a date to the current day
     * @param date the date to truncate
     * @return the truncated date
     */
    public static Date truncateDateToDay(Date date) {
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Truncates a date to the current hour
     * @param date the date to truncate
     * @return the truncated date
     */
    public static Date truncateDateToHour(Date date) {
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(date);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Adds a delay to a date
     * @param date the date
     * @param delayInHours the delay in hours
     * @return the new date
     */
    public static Date addDelay(Date date, int delayInHours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, delayInHours);
        return cal.getTime();
    }
}
