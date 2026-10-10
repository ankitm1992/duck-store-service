package com.duckstore.store.pricing;

import java.math.BigDecimal;

import com.duckstore.store.order.enums.ShippingMode;
import com.duckstore.store.packaging.PackageType;

/**
 * baseCost = quantity x unit price. All percentage rules apply to this base amount.
 */
public record PricingContext(BigDecimal baseCost, int quantity, PackageType packageType,
                             String country, ShippingMode shippingMode) {
}
