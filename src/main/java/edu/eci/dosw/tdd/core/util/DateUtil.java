package edu.eci.dosw.tdd.core.util;


import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {

    private DateUtil() {}

    public static long daysBetween(Date start, Date end) {
        LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static boolean isOverdue(Date returnDate) {
        return returnDate != null && returnDate.before(new Date());
    }

    public static Date today() {
        return new Date();
    }
}
