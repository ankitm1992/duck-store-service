package com.duckstore.store.packaging;

import java.util.List;

import com.duckstore.store.order.enums.ShippingMode;
import org.springframework.stereotype.Component;


@Component
public class SeaProtection implements ProtectionStrategy {
    public ShippingMode mode() {
        return ShippingMode.SEA;
    }

    public List<Protection> getProtections(PackageType type) {
        return List.of(Protection.MOISTURE_ABSORBING_BEADS, Protection.BUBBLE_WRAP_BAGS);
    }
}
