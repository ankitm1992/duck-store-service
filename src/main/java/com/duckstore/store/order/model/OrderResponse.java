package com.duckstore.store.order.model;

import java.math.BigDecimal;
import java.util.List;

import com.duckstore.store.packaging.PackageType;
import com.duckstore.store.packaging.Protection;
import com.duckstore.store.pricing.LineItem;

public record OrderResponse(PackageType packageType, List<Protection> protections,
                            BigDecimal unitPrice, BigDecimal totalToPay, List<LineItem> breakdown) {
}
