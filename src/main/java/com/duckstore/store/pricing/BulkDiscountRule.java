package com.duckstore.store.pricing;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
class BulkDiscountRule implements PricingRule {
    @Value("${price.bulk.discount.threshold:100}")
    int threshold;

    @Value("${price.bulk.discount.percent:20}")
    int percentage;

    public List<LineItem> apply(PricingContext c) {
        if (c.quantity() <= threshold) {
            return List.of();
        }
        return List.of(new LineItem("Bulk discount (20% for more than 100 units)",
                Cost.percent(c.baseCost(), percentage).negate()));
    }
}
