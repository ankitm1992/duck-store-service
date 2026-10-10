package com.duckstore.store.packaging;

import java.util.List;

import com.duckstore.store.order.enums.ShippingMode;
import org.springframework.stereotype.Component;


@Component
public class AirProtection implements ProtectionStrategy {
    public ShippingMode mode() { return ShippingMode.AIR; }

    public List<Protection> getProtections(PackageType type) {
        return type == PackageType.PLASTIC
                ? List.of(Protection.BUBBLE_WRAP_BAGS)
                : List.of(Protection.POLYSTYRENE_BALLS);
    }
}
