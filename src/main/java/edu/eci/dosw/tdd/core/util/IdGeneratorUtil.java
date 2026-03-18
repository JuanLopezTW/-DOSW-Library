package edu.eci.dosw.tdd.core.util;

import java.util.UUID;

public class IdGeneratorUtil {

    private IdGeneratorUtil() {}

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static Long generateNumericId() {
        return Math.abs(UUID.randomUUID().getMostSignificantBits());
    }
}
