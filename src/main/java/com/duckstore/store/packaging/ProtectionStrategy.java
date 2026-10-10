package com.duckstore.store.packaging;

import com.duckstore.store.order.enums.ShippingMode;

import java.util.List;


/** Strategy: one implementation per shipping mode (rules 4-7). */
public interface ProtectionStrategy {
    ShippingMode mode();

    List<Protection> protectionFor(PackageType packageType);
}
