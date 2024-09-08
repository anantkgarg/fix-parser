package com.akg.jp.assessment.fix.parser.impl;

import java.nio.ByteBuffer;

public final class BytesDataHelper {
    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
    public static float toFloat(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getFloat();
    }
    public static int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
}
