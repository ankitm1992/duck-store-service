package com.duckstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DuckSize {
    XLARGE("XLarge"), LARGE("Large"), MEDIUM("Medium"), SMALL("Small"), XSMALL("XSmall");

    private final String label;

    DuckSize(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
