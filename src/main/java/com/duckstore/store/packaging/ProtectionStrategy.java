package com.duckstore.store.packaging;

import com.duckstore.store.order.enums.ShippingMode;
import com.duckstore.store.packaging.enums.PackageType;
import com.duckstore.store.packaging.enums.ProtectionType;

import java.util.List;


/** Strategy: one implementation per shipping mode (rules 4-7). */
public interface ProtectionStrategy {
    ShippingMode mode();

    List<ProtectionType> getProtections(PackageType packageType);
}
