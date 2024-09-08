package com.akg.jp.assessment.fix.parser.impl;

import com.akg.jp.assessment.fix.exception.InvalidMessageException;
import com.akg.jp.assessment.fix.model.FixTag;
import com.akg.jp.assessment.fix.parser.FIXParser;


public class FIXParserImpl implements FIXParser {


    private boolean isProcessingTag, isProcessingValue, isParentTag, isRepeatingGroup;
    private FixTag currentTag;

    @Override
    public FIXMessage parse(byte[] msg) throws InvalidMessageException {
        FIXMessage fixMessage = null;
        if (msg != null) {
            fixMessage = new FIXMessage();
            //Copy message to fix message
            fixMessage.copyMessage(msg);
            isProcessingTag = true;
            isProcessingValue = false;
            isParentTag = false;
            isRepeatingGroup = false;
            currentTag = FixTag.UNKNOWN;

            for (int i = 0 ; i < msg.length; ) {
                i = processBytesFrom(fixMessage, i);
            }
        }
        return fixMessage;
    }

    private int processBytesFrom(FIXMessage fixMessage, int index) throws InvalidMessageException {
        int result = -1;
        if (fixMessage.getRawMessage()[index] == EQUALS) {
            isProcessingTag = false;
            isProcessingValue = true;
            result = index + 1;
        } else if (fixMessage.getRawMessage()[index] == DELIMITER) {
            isProcessingTag = true;
            isProcessingValue = false;
            result = index + 1;
        } else if (isProcessingTag) {
            result = processTag(fixMessage, index);
        } else if (isProcessingValue){
            result = processValue(fixMessage, index);
        }
        return result;
    }

    private int processValue(FIXMessage fixMessage, int index) throws InvalidMessageException {

        if(currentTag == FixTag.UNKNOWN) {
            throw new InvalidMessageException("Invalid FIX message with invalid Tags ");
        }

        int result = index;
        int length = 0;
        for(; result < fixMessage.getRawMessage().length; result++) {
            if ( fixMessage.getRawMessage()[result] == DELIMITER) {
                break;
            }
            length++;
        }
        fixMessage.updateTagValueIndexAndLength(currentTag, index, length);
        currentTag = FixTag.UNKNOWN;
        return result;
    }

    private int processTag(FIXMessage fixMessage, int index) throws InvalidMessageException {
        int result = index;
        int tag = 0;
        for(; result < fixMessage.getRawMessage().length; result++) {
            byte b = fixMessage.getRawMessage()[result];
            if ( b == EQUALS) {
                break;
            }
            tag = (tag * 10) + (b - '0');
        }
        currentTag = FixTag.byValue(tag);
        if (currentTag.isParentOfRepeatingTag()) {
            isParentTag = true;
        } else if(currentTag.isRepeatingTag()) {
            isRepeatingGroup = true;
        } else {
            isParentTag = isRepeatingGroup = false;
        }
        fixMessage.updateTagIndex(currentTag, index, (result-index));
        return result;
    }
}
