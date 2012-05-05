package com.wiley.cache.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link TimeDuration}.
 */
@RunWith(Parameterized.class)
public class TimeDurationTest {

    private static final long MILLIS_PER_MINUTE = 1000 * 60;
    private static final long MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;
    private static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;
    
    private String text;
    private int duration;
    private long inMillis;
    private TimeUnit unit;
    private boolean success;

    public TimeDurationTest(String text, boolean success, int duration, long inMillis, TimeUnit unit) {
        this.text = text;
        this.duration = duration;
        this.unit = unit;
        this.inMillis = inMillis;
        this.success = success;
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(
                new Object[][] {
                        { "1ms",  true,  1,                     1L, TimeUnit.MILLISECONDS },
                        { "1MS",  true,  1,                     1L, TimeUnit.MILLISECONDS },
                        { "15s",  true, 15,                 15000L, TimeUnit.SECONDS },
                        { "18S",  true, 18,                 18000L, TimeUnit.SECONDS },
                        {"3sec",  true,  3,                  3000L, TimeUnit.SECONDS},
                        { "30m",  true, 30, 30 * MILLIS_PER_MINUTE, TimeUnit.MINUTES },
                        { "50M",  true, 50, 50 * MILLIS_PER_MINUTE, TimeUnit.MINUTES },
                        {"5min",  true,  5,  5 * MILLIS_PER_MINUTE, TimeUnit.MINUTES},
                        {  "1h",  true,  1,        MILLIS_PER_HOUR, TimeUnit.HOURS },
                        { "12d",  true, 12,    12 * MILLIS_PER_DAY, TimeUnit.DAYS},
                        {  "0d",  true,  0,                     0L, TimeUnit.DAYS},
                        { "123", false,  0,                     0L, null},
                        { "12y", false,  0,                     0L, null},
                        { "-1d", false,  0,                     0L, null},

                        {    "300000d", true, 300000, 300000 * MILLIS_PER_DAY, TimeUnit.DAYS},
                        {"3000000000d", false,     0,                       0, null}

                }
        );
    }

    @Test
    public void testParse() {
        try {
            TimeDuration duration = TimeDuration.parse(text);
            assertEquals(this.duration, duration.duration());
            assertEquals(unit, duration.unit());
            assertEquals(inMillis, duration.toMilliseconds());
        } catch (IllegalArgumentException e) {
            if (success) {
                fail();
            }
        }
    }
    
}
