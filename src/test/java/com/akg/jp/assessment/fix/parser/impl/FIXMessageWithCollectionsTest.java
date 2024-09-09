package com.akg.jp.assessment.fix.parser.impl;

import com.akg.jp.assessment.fix.exception.InvalidMessageException;
import com.akg.jp.assessment.fix.model.FixTag;
import com.akg.jp.assessment.fix.parser.FIXMessage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FIXMessageWithCollectionsTest {

    FIXMessageWithCollections fixMessage;
    String s = "8=FIX.4.2\u00019=193\u000135=D\u000149=SenderCompId\u000156=TargetCompId\u0001" +
            "34=59\u000152=20240908-21:23:01.588\u000111=TESTORDER1\u000121=3\u000155=0005.HK\u0001" +
            "54=2\u000160=20240908-21:23:01.589\u000140=1\u00011=TESTCLIENT\u000138=1000000\u0001" +
            "78=2\u000179=ACC1\u000180=500000\u000179=ACC2\u000180=500000\u000110=099\u0001";

    @Before
    public void setUp() throws Exception {
        fixMessage = new FIXMessageWithCollections();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetRepeatFixTagStartIndex() throws InvalidMessageException {
        fixMessage.updateTagIndex(FixTag.BEGIN_STRING, 2, 5);

        Assert.assertEquals("Fix Tag Index failed", 0, fixMessage.getFixTagIndex(FixTag.BEGIN_STRING));
        Assert.assertEquals("Fix Tag Start Index failed", 2, fixMessage.getFixTagStartIndex(FixTag.BEGIN_STRING));

        fixMessage.updateTagIndex(FixTag.NO_ALLOCS, 50, 2);
        Assert.assertEquals("Repeating Fix Tag Index failed", 1, fixMessage.getFixTagIndex(FixTag.NO_ALLOCS));
        Assert.assertEquals("Repeating Fix Tag Start Index failed", 50, fixMessage.getRepeatFixTagStartIndex(FixTag.NO_ALLOCS));

        fixMessage.updateTagIndex(FixTag.ALLOC_ACCOUNT, 54, 2);
        Assert.assertEquals("Repeating Fix Tag Index failed", 2, fixMessage.getFixTagIndex(FixTag.ALLOC_ACCOUNT));
        Assert.assertEquals("Repeating Fix Tag Start Index failed", 54, fixMessage.getRepeatFixTagStartIndex(FixTag.ALLOC_ACCOUNT));
    }

    @Test
    public void testGetRepeatFixValueLength() throws InvalidMessageException {
        fixMessage.updateTagValueIndexAndLength(FixTag.BEGIN_STRING, 2, 5);

        Assert.assertEquals("Fix Tag Value Start Index failed", 2, fixMessage.getFixTagValueStartIndex(FixTag.BEGIN_STRING));
        Assert.assertEquals("Fix Tag Value Length failed", 5, fixMessage.getFixTagValueLength(FixTag.BEGIN_STRING));

        fixMessage.updateTagIndex(FixTag.NO_ALLOCS, 50, 2);
        fixMessage.updateTagValueIndexAndLength(FixTag.NO_ALLOCS, 50, 2);
        Assert.assertEquals("Repeating Fix Value Tag Index failed", 0, fixMessage.getFixTagIndex(FixTag.NO_ALLOCS));
        Assert.assertEquals("Repeating Fix Value Tag Start Index failed", 50, fixMessage.getRepeatFixValueStartIndex(FixTag.NO_ALLOCS));
        Assert.assertEquals("Repeating Fix Value Tag Length failed", 2, fixMessage.getRepeatFixValueLength(FixTag.NO_ALLOCS));

        fixMessage.updateTagIndex(FixTag.ALLOC_ACCOUNT, 52, 2);
        fixMessage.updateTagValueIndexAndLength(FixTag.ALLOC_ACCOUNT, 54, 2);
        Assert.assertEquals("Repeating Fix Value Tag Index failed", 1, fixMessage.getFixTagIndex(FixTag.ALLOC_ACCOUNT));
        Assert.assertEquals("Repeating Fix Value Tag Start Index failed", 54, fixMessage.getRepeatFixValueStartIndex(FixTag.ALLOC_ACCOUNT));
        Assert.assertEquals("Repeating Fix Value Tag Length failed", 2, fixMessage.getRepeatFixValueLength(FixTag.ALLOC_ACCOUNT));

        fixMessage.updateTagIndex(FixTag.ALLOC_ACCOUNT, 64, 2);
        fixMessage.updateTagValueIndexAndLength(FixTag.ALLOC_ACCOUNT, 68, 2);
        Assert.assertEquals("Repeating Fix Value Tag Index failed", 1, fixMessage.getFixTagIndex(FixTag.ALLOC_ACCOUNT));
        Assert.assertEquals("Repeating Fix Value Tag Start Index failed", 68, fixMessage.getRepeatFixValueStartIndex(FixTag.ALLOC_ACCOUNT));
        Assert.assertEquals("Repeating Fix Value Tag Length failed", 2, fixMessage.getRepeatFixValueLength(FixTag.ALLOC_ACCOUNT));
    }

    @Test(expected = InvalidMessageException.class)
    public void testExceptionsAddingDuplicateRepeatingGroupParent() throws InvalidMessageException {
        fixMessage.updateTagIndex(FixTag.NO_ALLOCS, 50, 2);
        fixMessage.updateTagIndex(FixTag.NO_ALLOCS, 55, 2);
    }

    @Test(expected = InvalidMessageException.class)
    public void testExceptionsAddingWithoutRepeatingGroupParent() throws InvalidMessageException {
        fixMessage.updateTagValueIndexAndLength(FixTag.ALLOC_ACCOUNT, 54, 2);
    }
}