package com.duckstore.warehouse.model;

import com.duckstore.warehouse.entity.Duck;
import com.duckstore.warehouse.enums.DuckColor;
import com.duckstore.warehouse.enums.DuckSize;

import java.math.BigDecimal;

public record WarehouseResponse(Long id, DuckColor color, DuckSize size, BigDecimal price, Integer quantity) {
    public static WarehouseResponse from(Duck d) {
        return new WarehouseResponse(d.getId(), d.getColor(), d.getSize(), d.getPrice(), d.getQuantity());
    }
    public static WarehouseResponse from(UpsertResult d) {
        return new WarehouseResponse(d.id(), DuckColor.valueOf(d.color()), DuckSize.valueOf(d.size()), d.price(), d.quantity());
    }
}
