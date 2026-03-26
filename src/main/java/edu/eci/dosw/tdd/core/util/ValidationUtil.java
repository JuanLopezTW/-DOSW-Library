package edu.eci.dosw.tdd.core.util;

public class ValidationUtil {

    private ValidationUtil() {}

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isPositive(Number value) {
        return value != null && value.doubleValue() > 0;
    }

    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }
}
