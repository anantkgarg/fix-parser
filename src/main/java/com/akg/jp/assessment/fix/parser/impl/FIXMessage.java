package com.akg.jp.assessment.fix.parser.impl;

import com.akg.jp.assessment.fix.exception.InvalidMessageException;
import com.akg.jp.assessment.fix.model.FixTag;

import java.util.Arrays;

import static com.akg.jp.assessment.fix.parser.FIXParserConstants.*;

public class FIXMessage {

    private byte[] rawMessage;

    private int[] fixTags;
    private int[] fixTagIndex;
    private int[][] fixTagValueIndexAndLength;
    private int[] repeatingFixTags;
    /**
     * 2nd dimension of following matrix should contains following info.
     * 1. tagId
     * 2. start index of tag
     * 3. length of tag
     * 4. start index of value
     * 5. length of value
     * 6. next index within this matrix for next tag
     */
    private int[][] repeatingGroupsIndexAndLength;
    private int tagIndex, repeatingGroupsIndex;


    public FIXMessage() {
        fixTags = new int[MAX_TAGS_ALLOWED];
        repeatingFixTags = new int[MAX_TAGS_ALLOWED];
        fixTagIndex = new int[MAX_TAGS_ALLOWED];
        fixTagValueIndexAndLength = new int[MAX_TAGS_ALLOWED][2];
        repeatingGroupsIndexAndLength = new int[MAX_TAGS_ALLOWED][6];
        tagIndex = repeatingGroupsIndex = 0;
        Arrays.fill(fixTags, -1);
        Arrays.fill(repeatingFixTags, -1);
        Arrays.fill(fixTagIndex, -1);
        fill(fixTagValueIndexAndLength, -1);
        fill(repeatingGroupsIndexAndLength, -1);
    }

    public void copyMessage(byte[] msg) {
        this.rawMessage = new byte[msg.length];
        System.arraycopy(msg, 0, this.rawMessage, 0, msg.length);
    }

    public byte[] getRawMessage() {
        return rawMessage;
    }

    public void updateTagIndex(FixTag fixTag, int index, int length) throws InvalidMessageException {
        fixTags[tagIndex++] = fixTag.getTag();
        if (fixTag.isParentOfRepeatingTag() || fixTag.isRepeatingTag()) {
            updateRepeatingGroupIndexes(fixTag, index, length, true);
        } else {
            fixTagIndex[fixTag.getTag()] = index;
        }
    }

    public void updateTagValueIndexAndLength(FixTag fixTag, int index, int length) throws InvalidMessageException {
        if (fixTag.isParentOfRepeatingTag() || fixTag.isRepeatingTag()) {
            updateRepeatingGroupIndexes(fixTag, index, length, false);
        } else {
            fixTagValueIndexAndLength[fixTag.getTag()][INDEX_POSITION] = index;
            fixTagValueIndexAndLength[fixTag.getTag()][LENGTH_POSITION] = length;
        }
    }

    public int getFixTagIndex(FixTag fixTag) {
        int index = -1;
        boolean found = false;
        for (; index <= fixTags.length; ) {
            if (fixTags[++index] == fixTag.getTag()) {
                found = true;
                break;
            }
        }
        return found ? index : -1;
    }

    public int getFixTagValueStartIndex(FixTag fixTag) {
        return fixTagValueIndexAndLength[fixTag.getTag()][INDEX_POSITION];
    }

    public int getFixTagValueLength(FixTag fixTag) {
        return fixTagValueIndexAndLength[fixTag.getTag()][LENGTH_POSITION];
    }

    public int getFixTagStartIndex(FixTag fixTag) {
        return fixTagIndex[fixTag.getTag()];
    }

    public int getRepeatFixTagIndex(FixTag fixTag) {
        return repeatingFixTags[fixTag.getTag()];
    }

    public int getRepeatFixTagStartIndex(FixTag fixTag) {
        return repeatingGroupsIndexAndLength[repeatingFixTags[fixTag.getTag()]][REPEATING_TAG_INDEX];
    }

    public int getRepeatFixTagLength(FixTag fixTag) {
        return repeatingGroupsIndexAndLength[repeatingFixTags[fixTag.getTag()]][REPEATING_TAG_LENGTH];
    }

    public int getRepeatFixValueLength(FixTag fixTag) {
        return repeatingGroupsIndexAndLength[repeatingFixTags[fixTag.getTag()]][REPEATING_TAG_VALUE_LENGTH];
    }

    public int getRepeatFixValueStartIndex(FixTag fixTag) {
        return repeatingGroupsIndexAndLength[repeatingFixTags[fixTag.getTag()]][REPEATING_TAG_VALUE_INDEX];
    }

    private void updateRepeatingGroupIndexes(FixTag fixTag, int index, int length, boolean updateTags) throws InvalidMessageException {
        int fixTagIndex = fixTag.getTag();
        if (fixTag.isParentOfRepeatingTag() && repeatingFixTags[fixTagIndex] != -1 && updateTags)
            throw new InvalidMessageException("Parent Repeating group cannot be repeated " + fixTag);

        int oldIndex = getPreviousTagIndex(fixTag);
        int currentIndex = updateTags ? repeatingGroupsIndex++ : oldIndex;
        if (updateTags) {
            if (fixTag.isParentOfRepeatingTag()) {
                repeatingFixTags[fixTagIndex] = currentIndex;
            } else if (repeatingFixTags[fixTagIndex] != -1) {
                repeatingGroupsIndexAndLength[oldIndex][REPEATING_TAG_NEXT_TAG_INDEX] = currentIndex;
            } else {
                repeatingFixTags[fixTagIndex] = currentIndex;
            }
            populateRepeatingGroupsTagsIndexAndLength(fixTag, index, length, currentIndex);
        } else {
            if (currentIndex < 0)
                throw new InvalidMessageException("Tag value cannot be accepted WITHOUT tag" + fixTag);

            populateRepeatingGroupsValuesIndexAndLength(index, length, currentIndex);
        }
    }

    private void populateRepeatingGroupsValuesIndexAndLength(int index, int length, int currentIndex) {
        repeatingGroupsIndexAndLength[currentIndex][REPEATING_TAG_VALUE_INDEX] = index;
        repeatingGroupsIndexAndLength[currentIndex][REPEATING_TAG_VALUE_LENGTH] = length;
    }

    private void populateRepeatingGroupsTagsIndexAndLength(FixTag fixTag, int index, int length, int currentIndex) {
        repeatingGroupsIndexAndLength[currentIndex][REPEATING_TAG_ID] = fixTag.getTag();
        repeatingGroupsIndexAndLength[currentIndex][REPEATING_TAG_INDEX] = index;
        repeatingGroupsIndexAndLength[currentIndex][REPEATING_TAG_LENGTH] = length;
        repeatingGroupsIndexAndLength[currentIndex][REPEATING_TAG_NEXT_TAG_INDEX] = -1;
    }

    private int getPreviousTagIndex(FixTag fixTag) {
        int index = repeatingFixTags[fixTag.getTag()];
        if (index != -1) {
            while (repeatingGroupsIndexAndLength[index][REPEATING_TAG_ID] == fixTag.getTag() &&
                    repeatingGroupsIndexAndLength[index][REPEATING_TAG_NEXT_TAG_INDEX] != -1) {
                index = repeatingGroupsIndexAndLength[index][REPEATING_TAG_NEXT_TAG_INDEX];
            }
        }
        return index;
    }

    private void fill(int[][] array, int value) {
        for (int i = 0; i < array.length; i++) {
            Arrays.fill(array[i], value);
        }
    }
}
