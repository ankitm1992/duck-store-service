package com.duckstore.warehouse.model;

import java.math.BigDecimal;

import com.duckstore.warehouse.enums.DuckColor;
import com.duckstore.warehouse.enums.DuckSize;
import jakarta.validation.constraints.*;

public record WarehouseRequest(
        @NotNull(message = "color is required") DuckColor color,
        @NotNull(message = "size is required") DuckSize size,
        @NotNull(message = "price is required")
        @DecimalMin(value = "0.01", message = "price must be at least 0.01")
        @Digits(integer = 8, fraction = 2, message = "price must have at most 2 decimals") BigDecimal price,
        @NotNull(message = "quantity is required")
        @Min(value = 1, message = "quantity must be at least 1") Integer quantity) {
}
