package com.duckstore.store.pricing;

import java.util.List;

/**
 * Each rule contributes zero or more line items. Adding a rule = adding a bean.
 */
public interface PricingRule {
    List<LineItem> apply(PricingContext context);
}
