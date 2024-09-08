package com.akg.jp.assessment.fix.parser.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class BytesDataHelperTest {

    private byte[] intArray, floatArray, doubleArray;
    @Before
    public void setUp() throws Exception {
        intArray = ByteBuffer.allocate(4).putInt(234).array();
        floatArray = ByteBuffer.allocate(4).putFloat(23.4f).array();
        doubleArray = ByteBuffer.allocate(8).putDouble(2345.67d).array();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testToDouble() {
        assertEquals("Double value mismatch", 2345.67d, BytesDataHelper.toDouble(doubleArray), 0.00d);
    }

    @Test
    public void testToFloat() {
        assertEquals("Float value mismatch", 23.4f, BytesDataHelper.toFloat(floatArray), 0.00f);
    }

    @Test
    public void testToInt() {
        assertEquals("Int value mismatch", 234, BytesDataHelper.toInt(intArray));
    }
}