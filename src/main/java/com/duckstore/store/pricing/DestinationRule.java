package com.duckstore.store.pricing;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
class DestinationRule implements PricingRule {

    private static final int DEFAULT_RATE = 15;
    private static final Map<String, String> ALIASES = Map.of(
            "US", "USA", "UNITED STATES", "USA", "UNITED STATES OF AMERICA", "USA");
    private static final Map<String, Integer> RATES = Map.of("USA", 18, "BOLIVIA", 13, "INDIA", 19);

    public List<LineItem> apply(PricingContext c) {
        String key = normalize(c.country());
        int rate = RATES.getOrDefault(key, DEFAULT_RATE);
        String label = RATES.containsKey(key) ? c.country().trim() : "other destination: " + c.country().trim();
        return List.of(new LineItem("Destination surcharge (" + label + ", " + rate + "%)",
                Money.percent(c.baseCost(), rate)));
    }

    /**
     * Case/whitespace-insensitive; accepts a few common names for the USA.
     */
    static String normalize(String country) {
        String upper = country.trim().replaceAll("\\s+", " ").replace(".", "").toUpperCase(Locale.ROOT);
        return ALIASES.getOrDefault(upper, upper);
    }
}
