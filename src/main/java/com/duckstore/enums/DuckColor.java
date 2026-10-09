package com.duckstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DuckColor {
    RED("Red"), GREEN("Green"), YELLOW("Yellow"), BLACK("Black");

    private final String label;

    DuckColor(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
