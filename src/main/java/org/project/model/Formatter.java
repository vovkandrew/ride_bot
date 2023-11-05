package org.project.model;

import java.util.Arrays;
import java.util.Objects;

import static org.project.util.constants.Constants.EMPTY_STRING;

public interface Formatter {
    Object[] getFormattedData();

    default Object[] getFormattedData(Object... objects) {
        return Arrays.stream(objects).map(object -> Objects.toString(object, EMPTY_STRING)).toArray();
    }
}
