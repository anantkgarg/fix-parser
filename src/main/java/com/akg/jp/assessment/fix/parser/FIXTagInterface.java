package com.akg.jp.assessment.fix.parser;

import com.akg.jp.assessment.fix.model.FixTag;

/**
 * Interface to identify the corresponding FIX Tag type to java primitive data types
 */
public interface FIXTagInterface {
    /**
     * @return - true if field is an Integer
     */
    boolean isInt();

    /**
     * @return- true if field is a Long
     */
    boolean isLong();

    /**
     * @return- true if field is a Float
     */
    boolean isFloat();

    /**
     * @return- true if field is a double
     */
    boolean isDouble();

    /**
     * @return- true if field is a Character
     */
    boolean isChar();

    /**
     * @return- true if field is a String
     */
    boolean isString();

    /**
     * @return- true if field is a boolean
     */
    boolean isBoolean();

    /**
     * @return- true if field is a UTCDate
     */
    boolean isUTCDate();

    /**
     * @return- true if field is a UTCTime
     */
    boolean isUTCTime();

    /**
     * @return- true if field is a UTCTimeStamp
     */
    boolean isUTCTimeStamp();

    /**
     * @return- true if tag is mandatory
     */
    boolean isMandatory();

    /**
     * @return- true if tag is optional
     */
    boolean isOptional();

    /**
     * @return- true if tag is a part of repeating group
     */
    boolean isRepeatingTag();

    /**
     * @return- true if tag is a part of repeating group
     */
    boolean isParentOfRepeatingTag();

    /**
     * @return- FixTag representing the parent tag. Only in case of repeating tags.
     */
    FixTag getParentTag();

}
