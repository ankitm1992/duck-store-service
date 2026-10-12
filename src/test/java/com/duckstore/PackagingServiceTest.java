package com.duckstore;

import static org.assertj.core.api.Assertions.assertThat;

import com.duckstore.store.order.enums.ShippingMode;
import com.duckstore.store.packaging.enums.PackageType;
import com.duckstore.store.packaging.enums.ProtectionType;
import com.duckstore.store.packaging.service.PackagingService;
import com.duckstore.warehouse.enums.DuckSize;
import org.junit.jupiter.api.Test;
import java.util.List;
import com.duckstore.store.packaging.*;

class PackagingServiceTest {

    private final PackagingService service = new PackagingService(new PackageTypeResolver(),
            List.of(new AirProtection(), new LandProtection(), new SeaProtection()));

    @Test
    void packageTypeDependsOnSize() {
        assertThat(service.pack(DuckSize.XLARGE, ShippingMode.LAND).type()).isEqualTo(PackageType.WOOD);
        assertThat(service.pack(DuckSize.LARGE, ShippingMode.LAND).type()).isEqualTo(PackageType.WOOD);
        assertThat(service.pack(DuckSize.MEDIUM, ShippingMode.LAND).type()).isEqualTo(PackageType.CARDBOARD);
        assertThat(service.pack(DuckSize.SMALL, ShippingMode.LAND).type()).isEqualTo(PackageType.PLASTIC);
        assertThat(service.pack(DuckSize.XSMALL, ShippingMode.LAND).type()).isEqualTo(PackageType.PLASTIC);
    }

    @Test
    void airProtection() {
        assertThat(service.pack(DuckSize.LARGE, ShippingMode.AIR).protections())
                .containsExactly(ProtectionType.POLYSTYRENE_BALLS);
        assertThat(service.pack(DuckSize.MEDIUM, ShippingMode.AIR).protections())
                .containsExactly(ProtectionType.POLYSTYRENE_BALLS);
        assertThat(service.pack(DuckSize.SMALL, ShippingMode.AIR).protections())
                .containsExactly(ProtectionType.BUBBLE_WRAP_BAGS);
    }

    @Test
    void landAndSeaProtection() {
        assertThat(service.pack(DuckSize.SMALL, ShippingMode.LAND).protections())
                .containsExactly(ProtectionType.POLYSTYRENE_BALLS);
        assertThat(service.pack(DuckSize.XLARGE, ShippingMode.SEA).protections())
                .containsExactlyInAnyOrder(ProtectionType.MOISTURE_ABSORBING_BEADS, ProtectionType.BUBBLE_WRAP_BAGS);
    }
}
