package com.wiley.cache.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Holds time duration in form "duration + unit".
 */
public class TimeDuration {
    
    private static final Pattern pattern = Pattern.compile("(\\d{1,8})(ms|s|m|h|d|sec|min)", Pattern.CASE_INSENSITIVE);
    private static final Map<String, TimeUnit> unitMap = new HashMap<String, TimeUnit>();
    private static final Map<TimeUnit, String> unitTextMap = new EnumMap<TimeUnit, String>(TimeUnit.class);

    static {
        unitMap.put( "ms", TimeUnit.MILLISECONDS);  unitTextMap.put(TimeUnit.MILLISECONDS, "ms");
        unitMap.put(  "s", TimeUnit.SECONDS);       unitTextMap.put(TimeUnit.SECONDS,       "s");
        unitMap.put(  "m", TimeUnit.MINUTES);       unitTextMap.put(TimeUnit.MINUTES,       "m");
        unitMap.put(  "h", TimeUnit.HOURS);         unitTextMap.put(TimeUnit.HOURS,         "h");
        unitMap.put(  "d", TimeUnit.DAYS);          unitTextMap.put(TimeUnit.DAYS,          "d");
        unitMap.put("sec", TimeUnit.SECONDS);
        unitMap.put("min", TimeUnit.MINUTES);
    }
    
    private final int duration;
    private final TimeUnit unit;
    
    private TimeDuration(int duration, TimeUnit unit) {
        this.duration = duration;
        this.unit = unit;
    }
    
    public int duration() {
        return duration;
    }

    public TimeUnit unit() {
        return unit;
    }

    public long toMilliseconds() {
        return unit().toMillis(duration());
    }

    /**
     * Parses duration in forms "1ms", "3000s", "30m", "2h", "1d", "5sec", "3min".
     * Allowed units are "ms", "s", "m", "h", "d" (case insensitive). Up to 8 digits in duration allowed.
     * Negative amounts are not allowed.
     * @param durationAsString text representation of time duration
     * @return time duration
     * @throws IllegalArgumentException if given string is invalid time duration representation
     */
    public static TimeDuration parse(String durationAsString) {
        Matcher matcher = pattern.matcher(durationAsString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Given string is not a valid time duration");
        }
        int amount = Integer.valueOf(matcher.group(1));
        String unitAsString = matcher.group(2);
        return new TimeDuration(amount, unitMap.get(unitAsString.toLowerCase()));
    }

    @Override
    public String toString() {
        return duration + unitTextMap.get(unit);
    }
}
