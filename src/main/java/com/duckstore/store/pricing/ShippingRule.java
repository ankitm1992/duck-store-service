package com.duckstore.store.pricing;

import java.math.BigDecimal;
import java.util.List;

import com.duckstore.store.pricing.model.Cost;
import com.duckstore.store.pricing.model.LineItem;
import com.duckstore.store.pricing.model.PricingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
class ShippingRule implements PricingRule {

    @Value("${price.sea.shipment:400}")
    private int seaShipmentPrice=400;

    @Value("${price.land.shipment.perUnit:10}")
    private int landShipmentPricePerUnit=10;

    @Value("${price.air.shipment.perUnit:30}")
    private int airShipmentPricePerUnit=30;

    @Value("${price.air.shipment.threshold.unit:1000}")
    private int airShipmentThresholdUnit=1000;

    @Value("${price.air.shipment.discount.percent:15}")
    private int airShipmentDiscountPercent=15;

    public List<LineItem> apply(PricingContext c) {
        return switch (c.shippingMode()) {
            case SEA -> List.of(new LineItem("Sea shipping flat fee", Cost.of(seaShipmentPrice)));
            case LAND -> List.of(new LineItem("Land shipping (10.00 x " + c.quantity() + " units)",
                    Cost.of(landShipmentPricePerUnit * c.quantity())));
            case AIR -> air(c.quantity());
        };
    }

    private List<LineItem> air(int quantity) {
        BigDecimal charge = Cost.of(airShipmentPricePerUnit * quantity);
        LineItem base = new LineItem("Air shipping (30.00 x " + quantity + " units)", charge);
        if (quantity <= airShipmentThresholdUnit) {
            return List.of(base);
        }
        return List.of(base, new LineItem("Air charge discount (15% for more than 1000 units)",
                Cost.percent(charge, airShipmentDiscountPercent).negate()));
    }
}
