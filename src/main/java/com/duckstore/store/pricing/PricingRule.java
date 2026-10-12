package com.duckstore.store.pricing;

import com.duckstore.store.pricing.model.LineItem;
import com.duckstore.store.pricing.model.PricingContext;

import java.util.List;


public interface PricingRule {
    List<LineItem> apply(PricingContext context);
}
