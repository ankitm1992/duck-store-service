package com.duckstore.store.order.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ShippingMode {
    LAND("Land"), AIR("Air"), SEA("Sea");

    private final String label;

    ShippingMode(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static ShippingMode from(String value) {
        for (ShippingMode s : values()) {
            if (s.label.equalsIgnoreCase(value.trim()) || s.name().equalsIgnoreCase(value.trim())) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid ShippingMode: " + value);
    }
}
