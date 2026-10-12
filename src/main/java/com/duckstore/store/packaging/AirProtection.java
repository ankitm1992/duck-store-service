package com.duckstore.store.packaging;

import java.util.List;

import com.duckstore.store.order.enums.ShippingMode;
import com.duckstore.store.packaging.enums.PackageType;
import com.duckstore.store.packaging.enums.ProtectionType;
import org.springframework.stereotype.Component;


@Component
public class AirProtection implements ProtectionStrategy {
    public ShippingMode mode() { return ShippingMode.AIR; }

    public List<ProtectionType> getProtections(PackageType type) {
        return type == PackageType.PLASTIC
                ? List.of(ProtectionType.BUBBLE_WRAP_BAGS)
                : List.of(ProtectionType.POLYSTYRENE_BALLS);
    }
}
