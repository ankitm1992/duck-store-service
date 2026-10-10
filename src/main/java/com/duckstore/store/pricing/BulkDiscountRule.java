package com.duckstore.store.pricing;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
class BulkDiscountRule implements PricingRule {
    static final int THRESHOLD = 100;

    public List<LineItem> apply(PricingContext c) {
        if (c.quantity() <= THRESHOLD) {
            return List.of();
        }
        return List.of(new LineItem("Bulk discount (20% for more than 100 units)",
                Money.percent(c.baseCost(), 20).negate()));
    }
}
