package com.momo.deathban.helpers;

import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateTimeCalculator {

    public static Date getExpiryDate(int monthDuration, int dayDuration, int hourDuration, int minuteDuration) {
        return DateUtils.addMonths(
                DateUtils.addDays(
                        DateUtils.addHours(
                                DateUtils.addMinutes(
                                        new Date(), minuteDuration
                                ), hourDuration
                        ), dayDuration
                ), monthDuration
        );
    }

    public static String getTimeRemaining(LocalDateTime currentDate, LocalDateTime expireDate) {
        long days = currentDate.until(expireDate, ChronoUnit.DAYS);
        currentDate = currentDate.plusDays(days);
        long hours = currentDate.until(expireDate, ChronoUnit.HOURS);
        currentDate = currentDate.plusHours(hours);
        long minutes = currentDate.until(expireDate, ChronoUnit.MINUTES);
        currentDate = currentDate.plusMinutes(minutes);
        long seconds = currentDate.until(expireDate, ChronoUnit.SECONDS);

        Long[] concatenatedDate = {seconds, minutes, hours, days};
        String[] timeUnits = {" seconds", " minutes, ", " hours, ", " days, "};

        StringBuilder toReturn = new StringBuilder();
        for (int i = 0; i < concatenatedDate.length; i++) {
            Long time = concatenatedDate[i];
            if (i != 0 && time == 0) {
                break;
            }
            toReturn.insert(0, timeUnits[i]);
            toReturn.insert(0, concatenatedDate[i]);
        }

        return String.valueOf(toReturn);
    }
}
