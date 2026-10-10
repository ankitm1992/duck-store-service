package com.duckstore.store.pricing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PricingService {

    private final List<PricingRule> rules;

    public PricingService(List<PricingRule> rules) {
        this.rules = rules;
    }

    public PriceQuote quote(PricingContext context) {
        List<LineItem> items = new ArrayList<>();
        items.add(new LineItem("Base cost (" + context.quantity() + " units)", Money.round(context.baseCost())));
        rules.forEach(rule -> items.addAll(rule.apply(context)));
        BigDecimal total = items.stream().map(LineItem::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PriceQuote(Money.round(total), List.copyOf(items));
    }
}
