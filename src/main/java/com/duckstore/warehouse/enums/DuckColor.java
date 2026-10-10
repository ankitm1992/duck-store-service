package com.duckstore.warehouse.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DuckColor {
    RED("Red"), GREEN("Green"), YELLOW("Yellow"), BLACK("Black");

    private final String label;

    DuckColor(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static DuckColor from(String value) {
        for (DuckColor c : values()) {
            if (c.label.equalsIgnoreCase(value.trim()) || c.name().equalsIgnoreCase(value.trim())) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid color: " + value);
    }
}
