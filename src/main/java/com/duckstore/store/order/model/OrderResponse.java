package com.duckstore.store.order.model;

import java.math.BigDecimal;
import java.util.List;

import com.duckstore.store.packaging.enums.PackageType;
import com.duckstore.store.packaging.enums.ProtectionType;
import com.duckstore.store.pricing.model.LineItem;

public record OrderResponse(PackageType packageType, List<ProtectionType> protections,
                            BigDecimal unitPrice, BigDecimal totalToPay, List<LineItem> breakdown) {
}
