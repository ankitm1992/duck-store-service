package com.duckstore.model;

import com.duckstore.entity.Duck;
import com.duckstore.enums.DuckColor;
import com.duckstore.enums.DuckSize;

import java.math.BigDecimal;

public record WarehouseResponse(Long id, DuckColor color, DuckSize size, BigDecimal price, Integer quantity) {
    public static WarehouseResponse from(Duck d) {
        return new WarehouseResponse(d.getId(), d.getColor(), d.getSize(), d.getPrice(), d.getQuantity());
    }
    public static WarehouseResponse from(UpsertResult d) {
        return new WarehouseResponse(d.id(), DuckColor.valueOf(d.color()), DuckSize.valueOf(d.size()), d.price(), d.quantity());
    }
}
