package com.wiley.cache.impl;

import com.wiley.cache.impl.Util;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testSha1() {
        assertEquals("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3", Util.sha1("test"));
        assertEquals("0a0a9f2a6772942557ab5355d76af442f8f65e01", Util.sha1("Hello, World!"));
    }
}
