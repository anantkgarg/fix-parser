package com.akg.jp.assessment.fix.parser.impl;

import com.akg.jp.assessment.fix.exception.InvalidMessageException;
import com.akg.jp.assessment.fix.model.FixTag;

import java.util.*;

public class FIXMessageWithCollections extends FIXMessageWithPrimitiveArrays {


    private Map<Integer, List<RepeatGroupDetails>> repeatingGroupsIndexAndLength;
//    private int tagIndex, repeatingGroupsIndex;

    /**
     * Inner class to contain following info.
     * 1. start index of tag
     * 2. start index of value
     * 3. length of value
     */
    class RepeatGroupDetails {
        int tagStartIdx, valueStartIdx, valueLength;
        RepeatGroupDetails() {
            tagStartIdx=valueStartIdx=valueLength=-1;
        }

        public int getTagStartIdx() {
            return tagStartIdx;
        }

        public void setTagStartIdx(int tagStartIdx) {
            this.tagStartIdx = tagStartIdx;
        }

        public int getValueStartIdx() {
            return valueStartIdx;
        }

        public void setValueStartIdx(int valueStartIdx) {
            this.valueStartIdx = valueStartIdx;
        }

        public int getValueLength() {
            return valueLength;
        }

        public void setValueLength(int valueLength) {
            this.valueLength = valueLength;
        }
    }

    public FIXMessageWithCollections() {
        repeatingGroupsIndexAndLength = new HashMap<>();
    }

    public int getRepeatFixTagStartIndex(FixTag fixTag) {
        return repeatingGroupsIndexAndLength.get(fixTag.getTag()).get(repeatingGroupsIndexAndLength.get(fixTag.getTag()).size()-1).getTagStartIdx();
    }

    public int getRepeatFixValueLength(FixTag fixTag) {
        return repeatingGroupsIndexAndLength.get(fixTag.getTag()).get(repeatingGroupsIndexAndLength.get(fixTag.getTag()).size()-1).getValueLength();
    }

    public int getRepeatFixValueStartIndex(FixTag fixTag) {
        return repeatingGroupsIndexAndLength.get(fixTag.getTag()).get(repeatingGroupsIndexAndLength.get(fixTag.getTag()).size()-1).getValueStartIdx();
    }

    @Override
    public void updateRepeatingGroupIndexes(FixTag fixTag, int index, int length, boolean updateTags) throws InvalidMessageException {
        if (fixTag.isParentOfRepeatingTag() && getFixTagStartIndex(fixTag) != -1 && updateTags)
            throw new InvalidMessageException("Parent Repeating group cannot be repeated " + fixTag);

        if (updateTags) {
            updateFixTagIndex(fixTag, index);
            populateRepeatingGroupsTagsIndexAndLength(fixTag, index);
        } else {
            if (getFixTagStartIndex(fixTag) < 0)
                throw new InvalidMessageException("Tag value cannot be accepted WITHOUT tag" + fixTag);

            populateRepeatingGroupsValuesIndexAndLength(fixTag, index, length);
        }
    }

    private void populateRepeatingGroupsValuesIndexAndLength(FixTag fixTag, int index, int length) {
        List<RepeatGroupDetails> repeatingGroupsList = repeatingGroupsIndexAndLength.get(fixTag.getTag());
        repeatingGroupsList.get(repeatingGroupsList.size()-1).setValueStartIdx(index);
        repeatingGroupsList.get(repeatingGroupsList.size()-1).setValueLength(length);

    }

    private void populateRepeatingGroupsTagsIndexAndLength(FixTag fixTag, int index) {
        RepeatGroupDetails repeatGroupDetails = new RepeatGroupDetails();
        List<RepeatGroupDetails> repeatGroupDetailsList = repeatingGroupsIndexAndLength.computeIfAbsent(fixTag.getTag(), k -> new ArrayList<>());//[currentIndex][REPEATING_TAG_ID] = fixTag.getTag();
        repeatGroupDetailsList.add(repeatGroupDetails);
        repeatGroupDetails.setTagStartIdx(index);
    }
}
