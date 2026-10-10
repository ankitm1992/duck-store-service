package com.duckstore.store.pricing;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
class PackageRule implements PricingRule {

    public List<LineItem> apply(PricingContext c) {
        return switch (c.packageType()) {
            case WOOD -> List.of(new LineItem("Wood package surcharge (5%)", Money.percent(c.baseCost(), 5)));
            case PLASTIC -> List.of(new LineItem("Plastic package surcharge (10%)", Money.percent(c.baseCost(), 10)));
            case CARDBOARD -> List.of(new LineItem("Cardboard package discount (1%)",
                    Money.percent(c.baseCost(), 1).negate()));
        };
    }
}
