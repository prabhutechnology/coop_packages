package com.prabhutech.coop.wallet.core.utils;

import android.os.Handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    public static String getCurrentDate() {
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String todayDate = currentDate.format(date);
        return todayDate;
    }

    public static Object setTimeout(Runnable runnable, long delay) {
        return new TimeoutEvent(runnable, delay);
    }

    public static void clearTimeout(Object timeoutEvent) {
        if (timeoutEvent instanceof TimeoutEvent) {
            ((TimeoutEvent) timeoutEvent).cancelTimeout();
        }
    }

    private static class TimeoutEvent {
        private static Handler handler = new Handler();
        private volatile Runnable runnable;

        private TimeoutEvent(Runnable task, long delay) {
            runnable = task;
            handler.postDelayed(() -> {
                if (runnable != null) {
                    runnable.run();
                }
            }, delay);
        }

        private void cancelTimeout() {
            runnable = null;
        }
    }

    /**
     * @param expireTime
     * @return
     */
    @Deprecated
    // TODO: 9/26/19 test needed
    public static boolean hasExpired2(String expireTime) {
        try {
            //Date format 8/22/2019 5:56:04 PM
            // time = "8/22/2019 8:23:04 PM";
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar c = Calendar.getInstance();
            int utcOffset = c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET);
            System.out.println(utcOffset);
            long currentTimestamp = c.getTimeInMillis() - utcOffset;
            System.out.println(currentTimestamp);
            long expiryTimestamp = sdf.parse(expireTime).getTime();
            if (currentTimestamp < expiryTimestamp) {
                return false;
            }
            // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Checks if given time in format MM/dd/yyyy hh:mm:ss aa is about to expire. Being 10 min close to
     * expire is considered to be expired.
     * @param expireTime
     * @return true if about to expire
     */
    public static boolean aboutToExpire(String expireTime) {
        // expireTime = expireTime.trim().replace(" ", "");
        // FIXME, some times are showing parse exception.
        try {
            // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            // Date date = sdf.parse(expireTime);

            // long expireTimestamp = date.getTime();
            long offsetExpireDiff = (5 * 60); // 5 minutes
            long currentTime = System.currentTimeMillis()/1000;
            long diff = Long.parseLong(expireTime) - currentTime;

            if (diff < 0) return true;
            return diff < offsetExpireDiff;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean hasExpired(String expireTime) {
        try {
            Date nDate = new Date();
            SimpleDateFormat utcsdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            utcsdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String current = utcsdf.format(nDate);
            // FIXME: 9/26/19 this line may cause problem
            return current.compareTo(expireTime) < 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Calculate duration of two time instances eg. calculate flight duration
     *
     * @param start start time in hh:mm format
     * @param end   end time in hh::min format
     * @return
     */
    public static String calculateTravelDuration(String start, String end) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            Date startTime = timeFormat.parse(start);
            Date endTime = timeFormat.parse(end);

            long diffMinutes = (int) ((endTime.getTime() - startTime.getTime()) / (60 * 1000));

            int hrs = (int) (diffMinutes / 60);
            int mins = (int) (diffMinutes % 60);

            String diff = hrs == 0f ? mins + " min" : hrs + " hr " + mins + " min";
            return diff;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Generate dates for seven days
     *
     * @param startDay starting date in "yyyy-MM-dd" format
     * @return
     */
    public static String[] getDateForNext7Days(String startDay) {

        String[] dates = new String[7];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(startDay));

            dates[0] = sdf.format(c.getTime());
            for (int i = 1; i < 7; i++) {
                c.add(Calendar.DATE, 1);
                dates[i] = sdf.format(c.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dates;
    }

    public static long getLocalToNepalTimeZone() {
        // ZonedDateTime.now(ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.of("Asia/Kathmandu"));
        // TimeZone tz = TimeZone.getTimeZone("GMT+05:45");
        TimeZone tz = TimeZone.getTimeZone("Asia/Kathmandu");
        Calendar c = Calendar.getInstance(tz);
        // System.out.println(c.getTimeInMillis());
        long timeIs = (c.getTimeInMillis() + (1000 * 60 * 60 * 5) + (1000 * 60 * 45));
        // System.out.println("Then in nepali should be " + timeIs);

        // String date = String.format("%2d-%2d-%2d %2d:%2d:%2d", c.get(Calendar.YEAR),
        //         c.get(Calendar.MONTH) + 1,
        //         c.get(Calendar.DAY_OF_MONTH),
        //         c.get(Calendar.HOUR_OF_DAY),
        //         c.get(Calendar.MINUTE),
        //         c.get(Calendar.SECOND));

        return timeIs;
    }

    public static long toNepaliTimestamp(long utc) {
        return utc + (1000 * 60 * 60 * 5) + (1000 * 60 * 45);
    }
}
