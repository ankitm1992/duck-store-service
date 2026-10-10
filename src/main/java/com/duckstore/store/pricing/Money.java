package com.duckstore.store.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class Money {
    private Money() {
    }

    static BigDecimal round(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * pct% of base, rounded to cents.
     */
    static BigDecimal percent(BigDecimal base, int pct) {
        return round(base.multiply(BigDecimal.valueOf(pct)).divide(BigDecimal.valueOf(100)));
    }

    static BigDecimal of(long units) {
        return round(BigDecimal.valueOf(units));
    }
}
