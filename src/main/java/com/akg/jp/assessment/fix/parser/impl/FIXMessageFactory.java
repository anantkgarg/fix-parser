package com.akg.jp.assessment.fix.parser.impl;

public final class FIXMessageFactory {
    public static FIXMessageWithPrimitiveArrays createFIXMessage(FIXMessageType type) {
        switch (type) {
            case COLLECTION:
                return new FIXMessageWithCollections();
            default:
                return new FIXMessageWithPrimitiveArrays();
        }
    }
}
