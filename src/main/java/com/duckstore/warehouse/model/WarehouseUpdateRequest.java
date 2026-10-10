package com.duckstore.warehouse.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

/**
 * Only price and quantity are editable; color and size are intentionally absent.
 */
public record WarehouseUpdateRequest(
        @NotNull(message = "price is required")
        @DecimalMin(value = "0.01", message = "price must be at least 0.01")
        @Digits(integer = 8, fraction = 2, message = "price must have at most 2 decimals")
        BigDecimal price,
        @NotNull(message = "quantity is required")
        @Min(value = 0, message = "quantity cannot be negative")
        Integer quantity) {
}
