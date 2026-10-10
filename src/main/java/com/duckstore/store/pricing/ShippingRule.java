package com.duckstore.store.pricing;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
class ShippingRule implements PricingRule {
    static final int AIR_DISCOUNT_THRESHOLD = 1000;

    public List<LineItem> apply(PricingContext c) {
        return switch (c.shippingMode()) {
            case SEA -> List.of(new LineItem("Sea shipping flat fee", Money.of(400)));
            case LAND -> List.of(new LineItem("Land shipping (10.00 x " + c.quantity() + " units)",
                    Money.of(10L * c.quantity())));
            case AIR -> air(c.quantity());
        };
    }

    private List<LineItem> air(int quantity) {
        BigDecimal charge = Money.of(30L * quantity);
        LineItem base = new LineItem("Air shipping (30.00 x " + quantity + " units)", charge);
        if (quantity <= AIR_DISCOUNT_THRESHOLD) {
            return List.of(base);
        }
        return List.of(base, new LineItem("Air charge discount (15% for more than 1000 units)",
                Money.percent(charge, 15).negate()));
    }
}
