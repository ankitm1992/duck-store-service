package com.duckstore;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.List;
import com.duckstore.store.order.enums.ShippingMode;
import org.junit.jupiter.api.Test;
import com.duckstore.store.packaging.PackageType;
import com.duckstore.store.pricing.*;

class PricingServiceTest {

    private final PricingService service = new PricingService(rules());

    private static List<PricingRule> rules() {
        try {
            String p = "com.duckstore.store.pricing.";
            return List.of(
                    (PricingRule) inst(p + "BulkDiscountRule"), (PricingRule) inst(p + "PackageRule"),
                    (PricingRule) inst(p + "DestinationRule"), (PricingRule) inst(p + "ShippingRule"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Object inst(String name) throws Exception {
        var ctor = Class.forName(name).getDeclaredConstructor();
        ctor.setAccessible(true);
        return ctor.newInstance();
    }

    private PriceQuote quote(String unit, int qty, PackageType pkg, String country, ShippingMode mode) {
        BigDecimal base = new BigDecimal(unit).multiply(BigDecimal.valueOf(qty));
        return service.quote(new PricingContext(base, qty, pkg, country, mode));
    }

    @Test
    void airToUsaWithCardboard() {
        // 50.00 - 0.50 (1%) + 9.00 (18%) + 300.00 (air)
        PriceQuote q = quote("5.00", 10, PackageType.CARDBOARD, "USA", ShippingMode.AIR);
        assertThat(q.total()).isEqualByComparingTo("358.50");
        assertThat(q.breakdown()).hasSize(4);
    }

    @Test
    void seaToIndiaWithWoodAndBulkDiscount() {
        // 2000 - 400 + 100 + 380 + 400
        PriceQuote q = quote("10.00", 200, PackageType.WOOD, "India", ShippingMode.SEA);
        assertThat(q.total()).isEqualByComparingTo("2480.00");
    }

    @Test
    void airOver1000UnitsGetsAirDiscountAndBulkDiscount() {
        // 1001 - 200.20 + 100.10 + 130.13 + 30030 - 4504.50
        PriceQuote q = quote("1.00", 1001, PackageType.PLASTIC, "Bolivia", ShippingMode.AIR);
        assertThat(q.total()).isEqualByComparingTo("26556.53");
    }

    @Test
    void exactly100UnitsHasNoBulkDiscountAndExactly1000NoAirDiscount() {
        assertThat(quote("1.00", 100, PackageType.CARDBOARD, "USA", ShippingMode.LAND).breakdown())
                .noneMatch(i -> i.description().startsWith("Bulk"));
        assertThat(quote("1.00", 1000, PackageType.CARDBOARD, "USA", ShippingMode.AIR).breakdown())
                .noneMatch(i -> i.description().startsWith("Air charge discount"));
    }

    @Test
    void otherDestinationAndLandShipping() {
        // 30.00 + 3.00 (plastic 10%) + 4.50 (other 15%) + 30.00 (land 10 x 3)
        PriceQuote q = quote("10.00", 3, PackageType.PLASTIC, "Narnia", ShippingMode.LAND);
        assertThat(q.total()).isEqualByComparingTo("67.50");
    }

    @Test
    void countryMatchingIsCaseInsensitiveAndAcceptsUsAlias() {
        PriceQuote a = quote("10.00", 1, PackageType.CARDBOARD, "  united states ", ShippingMode.SEA);
        PriceQuote b = quote("10.00", 1, PackageType.CARDBOARD, "USA", ShippingMode.SEA);
        assertThat(a.total()).isEqualByComparingTo(b.total());
    }

    @Test
    void roundsEachLineToCents() {
        PriceQuote q = quote("0.33", 7, PackageType.CARDBOARD, "USA", ShippingMode.LAND);
        assertThat(q.total()).isEqualByComparingTo("72.71"); // 2.31 - 0.02 + 0.42 + 70.00
    }
}
