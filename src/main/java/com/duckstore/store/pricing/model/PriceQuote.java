package com.duckstore.store.pricing.model;

import java.math.BigDecimal;
import java.util.List;

public record PriceQuote(BigDecimal total, List<LineItem> breakdown) {
}
