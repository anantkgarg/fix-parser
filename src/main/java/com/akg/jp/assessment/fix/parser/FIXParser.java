package com.akg.jp.assessment.fix.parser;

import com.akg.jp.assessment.fix.exception.InvalidMessageException;
import com.akg.jp.assessment.fix.parser.impl.FIXMessageWithPrimitiveArrays;

public interface FIXParser {
    char DELIMITER = '\u0001';
    char EQUALS = '=';
    FIXMessage parse(byte[] msg) throws InvalidMessageException;
}
