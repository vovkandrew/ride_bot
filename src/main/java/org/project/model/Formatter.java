package org.project.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.project.util.constants.Constants.EMPTY_STRING;

public interface Formatter {
    Object[] getFormattedData();

    default Object[] getFormattedData(Object... objects) {
        return stream(objects).map(object -> Objects.toString(object, EMPTY_STRING)).toArray();
    }

    List<Object> getFormattedDataAsList();

    default List<Object> getFormattedDataAsList(Object... objects) {
        return stream(objects).collect(Collectors.toList());
    }
}
