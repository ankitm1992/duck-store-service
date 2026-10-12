package com.duckstore.store.pricing.model;

import java.math.BigDecimal;

/**
 * One entry of the itemized breakdown; negative amount = discount.
 */
public record LineItem(String description, BigDecimal amount) {
}
