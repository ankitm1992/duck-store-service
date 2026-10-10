package com.duckstore.store.packaging;

import com.duckstore.warehouse.enums.DuckColor;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PackageType {
    WOOD("Wood"), CARDBOARD("Cardboard"), PLASTIC("Plastic");

    private final String label;

    PackageType(String label) { this.label = label; }

    @JsonValue
    public String label() { return label; }

    @JsonCreator
    public static PackageType from(String value) {
        for (PackageType c : values()) {
            if (c.label.equalsIgnoreCase(value.trim()) || c.name().equalsIgnoreCase(value.trim())) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid PackageType: " + value);
    }
}
