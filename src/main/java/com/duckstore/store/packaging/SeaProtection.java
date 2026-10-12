package com.duckstore.store.packaging;

import java.util.List;

import com.duckstore.store.order.enums.ShippingMode;
import com.duckstore.store.packaging.enums.PackageType;
import com.duckstore.store.packaging.enums.ProtectionType;
import org.springframework.stereotype.Component;


@Component
public class SeaProtection implements ProtectionStrategy {
    public ShippingMode mode() {
        return ShippingMode.SEA;
    }

    public List<ProtectionType> getProtections(PackageType type) {
        return List.of(ProtectionType.MOISTURE_ABSORBING_BEADS, ProtectionType.BUBBLE_WRAP_BAGS);
    }
}
