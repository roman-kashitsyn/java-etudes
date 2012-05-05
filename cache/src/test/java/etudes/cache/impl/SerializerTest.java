package com.wiley.cache.impl;

import com.wiley.cache.impl.BinarySerializer;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class SerializerTest {

    private BinarySerializer serializer = new BinarySerializer();

    @Test
    public void testRoundTrip() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serialize("Some text", baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals("Some text", serializer.deserialize(bais));
    }

}
