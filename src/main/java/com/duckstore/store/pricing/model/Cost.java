package com.duckstore.store.pricing.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Cost {
    private Cost() {
    }

    public static BigDecimal round(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal percent(BigDecimal base, int pct) {
        return round(base.multiply(BigDecimal.valueOf(pct)).divide(BigDecimal.valueOf(100)));
    }

    public static BigDecimal of(long units) {
        return round(BigDecimal.valueOf(units));
    }
}
