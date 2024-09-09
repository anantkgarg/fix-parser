package com.akg.jp.assessment.fix.benchmark;

import com.akg.jp.assessment.fix.exception.InvalidMessageException;
import com.akg.jp.assessment.fix.parser.FIXParser;
import com.akg.jp.assessment.fix.parser.impl.FIXMessageFactory;
import com.akg.jp.assessment.fix.parser.impl.FIXMessageType;
import com.akg.jp.assessment.fix.parser.impl.FIXParserImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Benchmarking {
    private final int maxMessagesCount = 1_000_000;
    private final String fixMessage = "8=FIX.4.2\u00019=193\u000135=D\u000149=SenderCompId\u000156=TargetCompId\u0001" +
            "34=59\u000152=20240908-21:23:01.588\u000111=TESTORDER1\u000121=3\u000155=0005.HK\u0001" +
            "54=2\u000160=20240908-21:23:01.589\u000140=1\u00011=TESTCLIENT\u000138=1000000\u0001" +
            "78=2\u0001" +
                "79=ACC1\u000180=500000\u0001" +
                "79=ACC2\u000180=500000\u0001" +
            "10=099\u0001";

    private FIXParser fixParser;
    private Runtime runtime;

    private static final String statsFormatString = " %35s | %20s | %20s ms | %30.2f Micro/msg| %15.2f Mb | %9.2f Gb ";
    private static final String headerFormatString = " %35s | %20s | %22s  | %39s | %18s | %12s";

    private final List<Integer> list = Arrays.asList((maxMessagesCount * 2) / 100, (maxMessagesCount * 4) / 100, (maxMessagesCount * 8) / 100,
                                                     (maxMessagesCount * 16 )/ 100, (maxMessagesCount * 32) / 100, (maxMessagesCount * 5) / 10,
                                                     (maxMessagesCount * 8) / 10, maxMessagesCount);
    @Before
    public void setUp() throws Exception {
        runtime = Runtime.getRuntime();
    }

    @Test
    public void runBenchMark() {
        System.out.println("_____________________________________________________________________________________________________________________________________________________________________");
        runIteration(FIXMessageType.PRIMITIVE);
        System.out.println("_____________________________________________________________________________________________________________________________________________________________________");
        System.gc();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
        runIteration(FIXMessageType.COLLECTION);
    }

    private void runIteration(FIXMessageType fixMessageType) {
        fixParser = new FIXParserImpl(fixMessageType);
        ArrayList<byte[]> data = prepareDate(fixMessage.getBytes());
        System.out.println(String.format(headerFormatString, "FIX Message Class", "Message Count", "Time Taken", "Average /msg", "Memory Used", "Total Memory"));
        System.out.println("_____________________________________________________________________________________________________________________________________________________________________");
        list.stream().forEach(msgCount -> {
            long startTime = System.currentTimeMillis();
            long memoryB4 = runtime.totalMemory() - runtime.freeMemory();
            try {
                for(int i=0; i < msgCount; i++)
                    fixParser.parse(data.get(i));
            } catch (InvalidMessageException e) {
                throw new RuntimeException(e);
            }
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long endTime = System.currentTimeMillis();
            System.out.println(String.format(statsFormatString, FIXMessageFactory.createFIXMessage(fixMessageType).getClass().getSimpleName(), msgCount, (endTime - startTime), ((endTime-startTime)*1000.0/msgCount), ((memoryAfter-memoryB4)/(1024.0f*1024.0f)),  Runtime.getRuntime().totalMemory()/(1024.0*1024.0*1024.0)));
        });
    }

    private ArrayList<byte[]> prepareDate(byte[] bytes) {
        ArrayList<byte[]> result = new ArrayList<>(maxMessagesCount);
        for(int i=0; i<maxMessagesCount; i++)
            result.add(bytes);
        return result;
    }
}
