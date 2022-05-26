package com.momo.deathban.helpers;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

public class DateTimeCalculator {
    public static Date getExpiryDate(int weekDuration, int dayDuration, int hourDuration, int minuteDuration) {
        return DateUtils.addWeeks(
                DateUtils.addDays(
                        DateUtils.addHours(
                                DateUtils.addMinutes(
                                        new Date(), minuteDuration
                                ), hourDuration
                        ), dayDuration
                ), weekDuration
        );
    }

}
