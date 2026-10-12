package com.duckstore.store.packaging;

import java.util.List;

import com.duckstore.store.order.enums.ShippingMode;
import com.duckstore.store.packaging.enums.PackageType;
import com.duckstore.store.packaging.enums.ProtectionType;
import org.springframework.stereotype.Component;


@Component
public class LandProtection implements ProtectionStrategy {
    public ShippingMode mode() { return ShippingMode.LAND; }

    public List<ProtectionType> getProtections(PackageType type) {

        return List.of(ProtectionType.POLYSTYRENE_BALLS);
    }
}
