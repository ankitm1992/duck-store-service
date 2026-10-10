package com.duckstore.store.order.model;


import com.duckstore.store.order.enums.ShippingMode;
import com.duckstore.warehouse.enums.DuckColor;
import com.duckstore.warehouse.enums.DuckSize;
import jakarta.validation.constraints.*;

public record OrderRequest(
        @NotNull(message = "color is required") DuckColor color,
        @NotNull(message = "size is required") DuckSize size,
        @NotNull(message = "quantity is required") @Min(value = 1, message = "quantity must be at least 1") Integer quantity,
        @NotBlank(message = "country is required") String country,
        @NotNull(message = "shippingMode is required") ShippingMode shippingMode) {
}
