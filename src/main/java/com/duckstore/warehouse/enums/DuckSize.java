package com.duckstore.warehouse.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DuckSize {
    XLARGE("XLarge"), LARGE("Large"), MEDIUM("Medium"), SMALL("Small"), XSMALL("XSmall");

    private final String label;

    DuckSize(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static DuckSize from(String value) {
        for (DuckSize c : values()) {
            if (c.label.equalsIgnoreCase(value.trim()) || c.name().equalsIgnoreCase(value.trim())) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid size: " + value);
    }
}
