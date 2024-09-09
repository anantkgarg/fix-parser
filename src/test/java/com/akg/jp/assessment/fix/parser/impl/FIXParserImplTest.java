package com.akg.jp.assessment.fix.parser.impl;

import com.akg.jp.assessment.fix.exception.InvalidMessageException;
import com.akg.jp.assessment.fix.parser.FIXParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class FIXParserImplTest {

    FIXParser parser;
    String strMsg = "8=FIX.4.2\u00019=193\u000135=D\u000149=SenderCompId\u000156=TargetCompId\u0001" +
            "34=7\u000152=20240908-21:23:01.588\u000111=TESTORDER1\u000121=3\u000155=0005.HK\u0001" +
            "54=2\u000160=20240908-21:23:01.589\u000140=1\u00011=TESTCLIENT\u000138=1000000\u0001" +
            "78=2\u000179=ACC1\u000180=500000\u000179=ACC2\u000180=500000\u000110=099\u0001";

    @Before
    public void setUp() throws Exception {
        parser = new FIXParserImpl(FIXMessageType.PRIMITIVE);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParse() throws InvalidMessageException {
        parser.parse(strMsg.getBytes());
    }

    @Test(expected = InvalidMessageException.class)
    public void testExceptionForMissingTag() throws InvalidMessageException {
        parser.parse("8=FIX.4.4\u0001=D\u000110=092\u0001".getBytes());
        fail("Should have thrown an exception");
    }

    @Test(expected = InvalidMessageException.class)
    public void testExceptionForUnknownTag() throws InvalidMessageException {
        parser.parse("8=FIX.4.4\u00011000=D\u000110=092\u0001".getBytes());
        fail("Should have thrown an exception");
    }
}