package com.akg.jp.assessment.fix.parser.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FIXMessageFactoryTest {

    @Test
    public void testCreateFIXMessage() {

        assertEquals("FixMessageWithPrimitiveArray fail", FIXMessageWithPrimitiveArrays.class.getSimpleName(),FIXMessageFactory.createFIXMessage(FIXMessageType.PRIMITIVE).getClass().getSimpleName());
        assertEquals("FIXMessageWithCollections fail", FIXMessageWithCollections.class.getSimpleName(),FIXMessageFactory.createFIXMessage(FIXMessageType.COLLECTION).getClass().getSimpleName());
    }
}