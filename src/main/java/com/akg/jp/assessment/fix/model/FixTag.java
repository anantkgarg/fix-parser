package com.akg.jp.assessment.fix.model;

import com.akg.jp.assessment.fix.parser.FIXTagInterface;

/**
 * This Enum defines all the fix tags. For the purpose of this assessment exercise only some of the tags are defined here
 */
public enum FixTag implements FIXTagInterface {
    UNKNOWN(0, DataType.Unknown),

    //Header tags
    BEGIN_STRING(8, DataType.String, TagType.MANDATORY),
    BODY_LENGTH(9, DataType.Int, TagType.MANDATORY),
    MSG_TYPE(35, DataType.Char, TagType.MANDATORY),
    SENDER_COMP_ID(49, DataType.String, TagType.MANDATORY),
    TARGET_COMP_ID(56, DataType.String, TagType.MANDATORY),
    MSG_SEQUENCE_NUM(34, DataType.Long, TagType.MANDATORY),
    SENDING_TIME(52, DataType.UTCTimeStamp, TagType.MANDATORY),

    //Single Order Tags
    CL_ORDER_ID(11, DataType.String, TagType.MANDATORY),
    HANDLE_INSTRUCTIONS(21, DataType.String, TagType.MANDATORY),
    SYMBOL(55, DataType.String, TagType.MANDATORY),
    SIDE(54, DataType.Char, TagType.MANDATORY),
    TRANSACT_TIME(60, DataType.UTCTimeStamp, TagType.MANDATORY),
    ORDER_TYPE(40, DataType.Char, TagType.MANDATORY),
    ACCOUNT_ID(1, DataType.String),
    //CLIENT_ID(109, DataType.String),
    ORDER_QTY(38, DataType.Long),
   // PRICE(44, DataType.Double),
    //Group repeating tag
    NO_ALLOCS(78, DataType.Int, true, 2),
    ALLOC_ACCOUNT(79, DataType.String, NO_ALLOCS),
    ALLOC_SHARES(80, DataType.Long, NO_ALLOCS),

    //Trailer Tags
    CHECKSUM(10, DataType.String, TagType.MANDATORY);

    private final int tag;
    private final DataType tagDataType;
    private final TagType tagType;
    private final FixTag parentTag;
    private final boolean isParentOfRepeatingTag;
    private final int numberOfRepeatingTags;

    FixTag(int tag, DataType tagDataType, TagType tagType, FixTag parentTag, boolean isParentOfRepeatingTag, int numberOfRepeatingTags) {
        this.tag = tag;
        this.tagDataType = tagDataType;
        this.tagType = tagType;
        this.parentTag = parentTag;
        this.isParentOfRepeatingTag = isParentOfRepeatingTag;
        this.numberOfRepeatingTags = numberOfRepeatingTags;
    }

    FixTag(int tag, DataType tagDataType, TagType tagType, FixTag parentTag, boolean isParentOfRepeatingTag) {
        this(tag, tagDataType, tagType, parentTag, isParentOfRepeatingTag, 0);
    }

    FixTag(int tag, DataType tagDataType, boolean isParentOfRepeatingTag, int numberOfRepeatingTags) {
        this(tag, tagDataType, TagType.OPTIONAL, null, isParentOfRepeatingTag, numberOfRepeatingTags);
    }

    FixTag(int tag, DataType tagDataType, FixTag parentTag) {
        this(tag, tagDataType, TagType.OPTIONAL, parentTag, false);
    }

    FixTag(int tag, DataType tagDataType, TagType tagType) {
        this(tag, tagDataType, tagType, null, false);
    }

    FixTag(int tag, DataType tagDataType) {
        this(tag, tagDataType, TagType.OPTIONAL);
    }

    /**
     * @return int representing the tag
     */
    public int getTag() {
        return tag;
    }

    @Override
    public boolean isParentOfRepeatingTag() {
        return isParentOfRepeatingTag;
    }

    @Override
    public FixTag getParentTag() {
        return parentTag;
    }

    @Override
    public boolean isInt() {
        return tagDataType == DataType.Int;
    }

    @Override
    public boolean isLong() {
        return tagDataType == DataType.Long;
    }

    @Override
    public boolean isFloat() {
        return tagDataType == DataType.Float;
    }

    @Override
    public boolean isDouble() {
        return tagDataType == DataType.Double;
    }

    @Override
    public boolean isChar() {
        return tagDataType == DataType.Char;
    }

    @Override
    public boolean isString() {
        return tagDataType == DataType.String;
    }

    @Override
    public boolean isBoolean() {
        return tagDataType == DataType.Boolean;
    }

    @Override
    public boolean isUTCDate() {
        return tagDataType == DataType.UTCDate;
    }

    @Override
    public boolean isUTCTime() {
        return tagDataType == DataType.UTCTime;
    }

    @Override
    public boolean isUTCTimeStamp() {
        return tagDataType == DataType.UTCTimeStamp;
    }

    @Override
    public boolean isMandatory() {
        return tagType == TagType.MANDATORY;
    }

    @Override
    public boolean isOptional() {
        return tagType == TagType.OPTIONAL;
    }

    @Override
    public boolean isRepeatingTag() {
        return parentTag != null;
    }

    @Override
    public String toString() {
        return "FixTag{" +
                "Name=" + name() +
                ", tag=" + tag +
                '}';
    }

    public static FixTag byValue(int tagId) {
        for (FixTag fixTag : values()) {
            if(fixTag.getTag() == tagId)
                return fixTag;
        }
        return FixTag.UNKNOWN;
    }
}
