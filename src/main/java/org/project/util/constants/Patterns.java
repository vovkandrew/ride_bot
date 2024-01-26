package org.project.util.constants;

import static java.lang.String.format;

public class Patterns {
    public static final String CAR_COLOR_PATTERN = "^[\\p{L}\\s-]{3,50}";
    public static final String CAR_MODEL_PATTERN = "^[\\p{L}\\s\\d]{5,50}";
    public static final String FIRST_NAME_PATTERN = "^[\\p{L}-\\']{3,50}";
    public static final String LAST_NAME_PATTERN = "^[\\p{L}-\\']{2,50}";
    public static final String CAR_PLATE_NUMBER_PATTERN = "^[\\p{L}\\d]{5,10}";
    public static final String CAR_SEATS_NUMBER_PATTERN = "^[1-9]{1,2}";
    public static final String SPLITERATOR_PATTERN = "\\|";
    public static final String DATE_PATTERN = "^\\s*(3[01]|[12][\\d]|0?[1-9])\\.(1[012]|0?[1-9])\\.(20[23-99]{2})\\s*$";
    public static final String TIME_PATTERN = "^(0[\\d]|1[\\d]|2[0-3]):[0-5][\\d]$";
    public static final String GENERAL_MESSAGE_PATTERN = "^[\\p{L}\\s\\d\\p{P}\\p{S}]{0,200}";
    public static final String PRICE_PATTERN = "[1-9][\\d]{1,3}";
    public static String getGeneralMessagePatternWithParameterizedStartLimit(int value) {
        return format("^[\\p{L}\\s\\d\\p{P}\\p{S}]{%d,200}", value);
    }
}
