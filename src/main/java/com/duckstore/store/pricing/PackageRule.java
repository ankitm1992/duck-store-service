package com.duckstore.store.pricing;

import java.util.List;

import com.duckstore.store.pricing.model.Cost;
import com.duckstore.store.pricing.model.LineItem;
import com.duckstore.store.pricing.model.PricingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
class PackageRule implements PricingRule {
    @Value("${price.wood.packaging.percent:5}")
    int woodPackagingPercent=5;

    @Value("${price.plastic.packaging.percent:10}")
    int plasticPackagingPercent=10;

    @Value("${price.cardboard.packaging.discount.percent:1}")
    int cardboardPackagingDiscountPercent=1;

    public List<LineItem> apply(PricingContext c) {
        return switch (c.packageType()) {
            case WOOD -> List.of(new LineItem("Wood package surcharge (5%)", Cost.percent(c.baseCost(), woodPackagingPercent)));
            case PLASTIC -> List.of(new LineItem("Plastic package surcharge (10%)", Cost.percent(c.baseCost(), plasticPackagingPercent)));
            case CARDBOARD -> List.of(new LineItem("Cardboard package discount (1%)",
                    Cost.percent(c.baseCost(), cardboardPackagingDiscountPercent).negate()));
        };
    }
}
