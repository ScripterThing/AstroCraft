package com.cubikore.astro.util;

public class AstroCraftUtil {
    public static float timePassed(long start) {
        return timePassed(start, System.currentTimeMillis());
    }

    public static float timePassed(long start, long end) {
        return (float)(end - start) / 1000;
    }
}
