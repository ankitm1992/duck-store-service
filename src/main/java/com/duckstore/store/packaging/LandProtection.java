package com.duckstore.store.packaging;

import java.util.List;

import com.duckstore.store.order.enums.ShippingMode;
import org.springframework.stereotype.Component;


@Component
public class LandProtection implements ProtectionStrategy {
    public ShippingMode mode() { return ShippingMode.LAND; }

    public List<Protection> getProtections(PackageType type) {

        return List.of(Protection.POLYSTYRENE_BALLS);
    }
}
